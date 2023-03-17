package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String [] argv) {
        ServerSocket SSocket = null;
        Socket Socket;

        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;

        String eco;

        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        try {
            // Create a server socket
            SSocket = new ServerSocket(Integer.parseInt(argv[0]));
            // Set a timeout of 300 secs
            SSocket.setSoTimeout(300_000);
            while (true) {
                // Wait for connections

                Socket = SSocket.accept();

                // Set the input channel

                bufferedReader = new BufferedReader(new InputStreamReader(Socket.getInputStream()));

                // Set the output channel

                bufferedWriter = new BufferedWriter(new OutputStreamWriter(Socket.getOutputStream()));

                // Receive the client message

                eco = bufferedReader.readLine();
                System.out.println("SERVER: Received «" + eco + "» from " + Socket.getInetAddress() + ":" + Socket.getPort());

                // Send response to the client

                bufferedWriter.write(eco);

                // Close the streams

                bufferedWriter.close();
                bufferedReader.close();
                Socket.close();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
	        //Close the socket
            try {
                SSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
