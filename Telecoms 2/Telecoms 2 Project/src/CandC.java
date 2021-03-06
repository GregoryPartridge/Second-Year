import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

/**
 *
 * Client class
 *
 * An instance accepts input from the user, marshalls this into a datagram, sends
 * it to a server instance and then waits for a reply. When a packet has been
 * received, the type of the packet is checked and if it is an acknowledgement,
 * a message is being printed and the waiting main method is being notified.
 *
 */
public class CandC extends Node {
	static final int DEFAULT_SRC_PORT = 50000; // Port of the broker
	static final int DEFAULT_DST_PORT = 50001; // Port of the command and control
	static final String DEFAULT_DST_NODE = "localhost";	// Name of the host for the server

	static final int HEADER_LENGTH = 2; // Fixed length of the header
	static final int TYPE_POS = 0; // Position of the type within the header

	static final byte TYPE_UNKNOWN = 0;

	static final byte TYPE_STRING = 1; // Indicating a string pay load.
	static final int LENGTH_POS = 1;

	static final byte TYPE_ACK = 2;   // Indicating an acknowledgement
	static final int ACKCODE_POS = 1; // Position of the acknowledgement type in the header
	static final byte ACK_ALLOK = 10; // Indicating that everything is okay.

	Terminal terminal;
	InetSocketAddress dstAddress;

	/**
	 * Constructor
	 *
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 */
	CandC(Terminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal= terminal;
			dstAddress= new InetSocketAddress(dstHost, dstPort);
			socket= new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}


	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		byte[] data;

		data = packet.getData();
		switch(data[TYPE_POS]) {
		case TYPE_ACK:
			terminal.println("Received acknologment:");
			this.notify();
			try {
				sendMessage();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			terminal.println("Unexpected packet" + packet.toString());
		}
	}


	/**
	 * Sender Method
	 *
	 */
	public synchronized void sendMessage() throws Exception {
		byte[] data= null;
		byte[] buffer= null;
		DatagramPacket packet= null;
		String input;
		String option;
		boolean foundOption = false;
		while(!foundOption)
		{
			terminal.println("What do you wish to do:\n\n"
					+ "	Option 1)\"Check for free workers\"\n"
					+ "	Option 2)\"Send assignment to first available worker\"\n"
					+ "	Option 3)\"Send work to all available workers\"\n"
					+ "	Option 4)\"Send work to select amount of workers\"\n");
			
			option = terminal.read("Option select, please only input a number.");
			if(option.contains("1"))
			{
				input = "Command and Control: Option 1";
				buffer = input.getBytes();
				data = new byte[HEADER_LENGTH+buffer.length];
				data[TYPE_POS] = TYPE_STRING;
				data[LENGTH_POS] = (byte)buffer.length;
				System.arraycopy(buffer, 0, data, HEADER_LENGTH, buffer.length);

				terminal.println("Sending packet...");
				packet= new DatagramPacket(data, data.length);
				packet.setSocketAddress(dstAddress);
				socket.send(packet);
				terminal.println("Packet sent");
				foundOption = true;
				this.wait();
			}
			else if(option.contains("2"))
			{
				input = "Command and Control: Option 2";
				buffer = input.getBytes();
				data = new byte[HEADER_LENGTH+buffer.length];
				data[TYPE_POS] = TYPE_STRING;
				data[LENGTH_POS] = (byte)buffer.length;
				System.arraycopy(buffer, 0, data, HEADER_LENGTH, buffer.length);

				terminal.println("Sending packet...");
				packet= new DatagramPacket(data, data.length);
				packet.setSocketAddress(dstAddress);
				socket.send(packet);
				terminal.println("Packet sent");
				foundOption = true;
				this.wait();
			}
			else if(option.contains("3"))
			{
				input = "Command and Control: Option 3";
				buffer = input.getBytes();
				data = new byte[HEADER_LENGTH+buffer.length];
				data[TYPE_POS] = TYPE_STRING;
				data[LENGTH_POS] = (byte)buffer.length;
				System.arraycopy(buffer, 0, data, HEADER_LENGTH, buffer.length);

				terminal.println("Sending packet...");
				packet= new DatagramPacket(data, data.length);
				packet.setSocketAddress(dstAddress);
				socket.send(packet);
				terminal.println("Packet sent");
				foundOption = true;
				this.wait();
			}
			else if(option.contains("4"))
			{
				input = "Command and Control: Option 4";
				buffer = input.getBytes();
				data = new byte[HEADER_LENGTH+buffer.length];
				data[TYPE_POS] = TYPE_STRING;
				data[LENGTH_POS] = (byte)buffer.length;
				System.arraycopy(buffer, 0, data, HEADER_LENGTH, buffer.length);

				terminal.println("Sending packet...");
				packet= new DatagramPacket(data, data.length);
				packet.setSocketAddress(dstAddress);
				socket.send(packet);
				terminal.println("Packet sent");
				foundOption = true;
				this.wait();
			}
			else
			{
				terminal.println("You did not select a selectable option. Please try again.\n");
			}
		}
			
			input = "Command and Control: ";
			input = terminal.read("Payload: ");
			buffer = input.getBytes();
			data = new byte[HEADER_LENGTH+buffer.length];
			data[TYPE_POS] = TYPE_STRING;
			data[LENGTH_POS] = (byte)buffer.length;
			System.arraycopy(buffer, 0, data, HEADER_LENGTH, buffer.length);

			terminal.println("Sending packet...");
			packet= new DatagramPacket(data, data.length);
			packet.setSocketAddress(dstAddress);
			socket.send(packet);
			terminal.println("Packet sent");
			this.wait();
	}


	/**
	 * Test method
	 *
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {
			Terminal terminal= new Terminal("Command and Control:");
			(new CandC(terminal, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT)).sendMessage();
			terminal.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}