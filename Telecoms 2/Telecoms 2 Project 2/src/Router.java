import java.io.*;
import java.net.*;

public class Router extends Node implements Runnable {

	private Terminal terminal;
	private int port;
	private String message;
	private int destination;

	Router(Terminal terminal, int port) {
		try {
			this.terminal= terminal;
			this.port = port;
			socket = new DatagramSocket(port);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	public synchronized void onReceipt(DatagramPacket packet) {
		String content;
		byte[] data;
		byte[] buffer;

		data = packet.getData();

		buffer = new byte[data[CONSTANT.LENGTH_POS]];
		System.arraycopy(data, CONSTANT.HEADER_LENGTH, buffer, 0, buffer.length);
		content = new String(buffer);
		int packetPort = packet.getPort();

		if (packetPort == CONSTANT.CONTROLLERPORT || packetPort >= CONSTANT.STARTROUTER && packetPort < CONSTANT.STARTENDUSERONE) {
			if (content.equals(CONSTANT.HELLO)) {
				terminal.println("The Controller sent hello");
			} else {
				sendMessage(message, Integer.parseInt(Controller.next(port, destination)));
			}
		} else if (packetPort >= CONSTANT.STARTENDUSERONE) {
			if (packetPort == CONSTANT.STARTENDUSERONE)
				destination = CONSTANT.STARTENDUSERONE + 1;
			else destination = CONSTANT.STARTENDUSERONE;

			message = content;
			sendMessage(CONSTANT.NEXT + destination, CONSTANT.CONTROLLERPORT);
		}
	}

	private synchronized void sendMessage(String input, int port) {
		byte[] buffer;
		byte[] data;
		DatagramPacket packet;

		buffer = input.getBytes();
		data = new byte[CONSTANT.HEADER_LENGTH + buffer.length];
		data[CONSTANT.TYPE_POS] = CONSTANT.STRING_TYPE;
		data[CONSTANT.LENGTH_POS] = (byte)buffer.length;

		System.arraycopy(buffer, 0, data, CONSTANT.HEADER_LENGTH, buffer.length);

		packet = new DatagramPacket(data, data.length);
		packet.setSocketAddress(new InetSocketAddress(CONSTANT.DSTHOST, port));

		try {
			socket.send(packet);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	private synchronized void sayHello() {
			sendMessage(CONSTANT.HELLO, CONSTANT.CONTROLLERPORT);
	}

	public synchronized void run() {
		sayHello();

		try { this.wait(); }
		catch (InterruptedException e) { e.printStackTrace(); }
	}

	public static void main(String[] args) 
	{
		try
		{
			Terminal terminal = new Terminal("Router:");
			(new Router(terminal, CONSTANT.STARTROUTER)).run();
			terminal.println("Program Completed");
		}
		catch(java.lang.Exception e)
		{
			e.printStackTrace();
		}
	}
}