package es.udc.redes.tutorial.tcp.server;
import java.io.IOException;
import java.net.*;

/** Multithread TCP echo server. */

public class TcpServer {

  public static void main(String [] argv) {
    ServerSocket serverSocket = null;
    Socket socket;
    ServerThread fio;

    if (argv.length != 1) {
      System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
      System.exit(-1);
    }
    try {
      // Create a server socket

      serverSocket = new ServerSocket(Integer.parseInt(argv[0]));

      // Set a timeout of 300 secs
      serverSocket.setSoTimeout(300_000);
      while (true) {
        // Wait for connections
        socket = serverSocket.accept();
        // Create a ServerThread object, with the new connection as parameter
        fio = new ServerThread(socket);
        // Initiate thread using the start() method
        fio.start();
      }
     } catch (SocketTimeoutException e) {
      System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
     } finally{
      try {
        //Close the socket
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
