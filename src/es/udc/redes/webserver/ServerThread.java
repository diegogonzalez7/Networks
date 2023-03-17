package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.util.Date;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;




public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    public void run() {
        String eco;
        BufferedReader bufferedReader;
        BufferedOutputStream bufferedOutputStream;
        String fichero;
        String tmp;
        boolean error;
        boolean date = true;

        try {
            // Establece el canal de entrada y salida
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

            // recibe el mensaje del cliente
            eco = bufferedReader.readLine();
            if(eco==null){
                return;
            }
            error = !eco.split(" ")[0].equals("HEAD") && !eco.split(" ")[0].equals("GET");


            fichero="p1-files/" + eco.split(" ")[1];

            while((tmp=bufferedReader.readLine())!= null && !tmp.equals("")){
                if(tmp.split(":")[0].equals("If-Modified-Since")){
                    date=Date.from(Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(tmp.split(": ",2)[1]))).compareTo(new Date(Files.getLastModifiedTime(Path.of(fichero)).toMillis()))>=0;
                }
            }

            FileInputStream in = null;      //Get

            if(Files.isDirectory(Path.of(fichero))) {
                fichero = fichero + "/index.html";
            }

            if(error){
                bufferedOutputStream.write("HTTP/1.0 400 Bad Request\r\n".getBytes());
            }
            else if(date) {
                if (!Files.exists(Path.of(fichero))) {
                    bufferedOutputStream.write("HTTP/1.0 404 Not Found\r\n".getBytes());
                    fichero = "p1-files/error404.html";
                } else {
                    bufferedOutputStream.write("HTTP/1.0 200 OK\r\n".getBytes());
                }
            }
            else{
                bufferedOutputStream.write("HTTP/1.0 304 Not Modified\r\n".getBytes());
            }
            bufferedOutputStream.write(("Date: "+ OffsetDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME)+"\r\n").getBytes());
            bufferedOutputStream.write("Server: WebServer_353\r\n".getBytes());

            if(!error) {
                bufferedOutputStream.write(("Last-Modified: " + DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneId.systemDefault()).format(Files.getLastModifiedTime(Path.of(fichero)).toInstant()) + "\r\n").getBytes());
                bufferedOutputStream.write(("Content-Length: " + Files.size(Path.of(fichero)) + "\r\n").getBytes());
                bufferedOutputStream.write(("Content-Type: " + Files.probeContentType(Path.of(fichero)) + "\r\n\r\n").getBytes());
            } else {
                bufferedOutputStream.write(("Last-Modified: " + OffsetDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\r\n").getBytes());
                bufferedOutputStream.write("Content-Length: 0\r\n".getBytes());
                bufferedOutputStream.write("Content-Type: text/plain\r\n\r\n".getBytes());
            }


            if(eco.split(" ")[0].equals("GET") && date && !error) {       //Head
                try {
                    in = new FileInputStream(fichero);
                    int c;

                    while ((c = in.read()) != -1) {
                        bufferedOutputStream.write(c);
                    }
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
            // Close the streams
            bufferedOutputStream.close();
            bufferedReader.close();

         } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the client socket
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();        //muestra porqué ocurre el error
            }
        }
    }
}
