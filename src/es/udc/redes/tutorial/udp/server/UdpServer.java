package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String [] argv) {
        DatagramSocket datagramSocket = null;
        DatagramPacket datagramPacket;
        int puerto;


        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        try {
            // Create a server socket

            puerto = Integer.parseInt(argv[0]);
            datagramSocket = new DatagramSocket(puerto);

            // Set maximum timeout to 300 secs
            datagramSocket.setSoTimeout(300000);
            while (true) {
                // Prepare datagram for reception
                datagramPacket = new DatagramPacket(new byte [20], 20);
                // Receive the message
                datagramSocket.receive(datagramPacket);
                // Prepare datagram to send response
                System.out.println("SERVER: Received «" + (new String(datagramPacket.getData())) + "» from "
                        + datagramPacket.getAddress() + ":" + datagramPacket.getPort());
                // Send response
                datagramSocket.send(datagramPacket);
            }
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            datagramSocket.close();
        }
    }
}
