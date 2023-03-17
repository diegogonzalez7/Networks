package es.udc.redes.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer {
    
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket;
        ServerThread fio;

        if (args.length != 1) {
            System.err.println("Format: es.udc.redes.webserver.WebServer <port>");
            System.exit(-1);
        }
        try {
            //Crea un ServerSocket
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            //Establece un temporizador de 300 segundos
            serverSocket.setSoTimeout(300000);
            while (true) {
                //espera a las conexiones
                socket = serverSocket.accept();
                //crea un ServerThread, con el socket como parámetro
                fio = new ServerThread(socket);
                //Inicia el thread
                fio.start();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                assert serverSocket != null;
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
