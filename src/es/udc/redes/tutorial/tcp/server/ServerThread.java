package es.udc.redes.tutorial.tcp.server;
import java.net.*;
import java.io.*;

/** Thread that processes an echo server connection. */

public class ServerThread extends Thread {

  private Socket socket;

  public ServerThread(Socket s) {
    // Store the socket s
    socket = s;
  }

  public void run() {
    String eco;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    try {
      // Set the input channel

      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      // Set the output channel

      bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      // Receive the message from the client

      eco = bufferedReader.readLine();
      System.out.println("SERVER: Received «" + eco + "» from " + socket.getInetAddress() + ":" + socket.getPort());

      // Sent the echo message to the client

      bufferedWriter.write(eco);

      // Close the streams

      bufferedWriter.close();
      bufferedReader.close();

     } catch (SocketTimeoutException e) {
      System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      } finally {
	// Close the socket
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
