
import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class Worker extends Node {
	
	static final int DEFAULT_SRC_PORT = 50003; // Port of the worker
	static final int DEFAULT_DST_PORT = 50001; // Port of the broker
	static final String DEFAULT_DST_NODE = "localhost";
	
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
	
	boolean hasConnected = false;
	
	Worker(Terminal terminal, String dstHost, int dstPort, int srcPort)
	{
		try {
			this.terminal = terminal;
			dstAddress= new InetSocketAddress(dstHost, dstPort);
			socket= new DatagramSocket(srcPort);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}
	
	public synchronized void onReceipt(DatagramPacket packet) 
	{
		byte[] data = null;
		String content;
		
		data = packet.getData();
		content= new String(data);
		String message;
		
		
	
		DatagramPacket response;
		response = new DatagramPacket(data, data.length);
		response.setSocketAddress(packet.getSocketAddress());
		try {
			socket.send(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(content.contains("Assignment:"))
		{
			terminal.println(content.toString());
			if(workerTakeTask())
			{
				while(!finishedTask())
				{
					
				}
				message = "Assignment finished sucsessfully.";
				sendAssignment(message);
			}
			else
			{
				terminal.println("The worker failed to start the task and has given up.");
				message = "Assignment failed.";
				sendAssignment(message);
			}
		}
		else
		{
			this.notify();
			terminal.println(content.toString());
		}
	}
	
	private void sendWorkerName(String workerName)
	{
		byte[] data = null;
		byte[] dataToSend = null;
		
		DatagramPacket packet = null;
		
		String workerString = "Worker :"+workerName;
		dataToSend = workerString.getBytes();			
		data = new byte[HEADER_LENGTH+dataToSend.length];
		data[TYPE_POS] = TYPE_STRING;
		data[LENGTH_POS] = (byte)dataToSend.length;
		System.arraycopy(dataToSend, 0, data, HEADER_LENGTH, dataToSend.length);

		terminal.println("Sending packet...");
		packet= new DatagramPacket(data, data.length);
		packet.setSocketAddress(dstAddress);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		terminal.println("Packet sent");
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void sendAssignment(String message)
	{
		byte[] data = null;
		byte[] dataToSend = null;
		
		DatagramPacket packet = null;
		
		String confoirmationThatFinished = "Assignment :"+message;
		dataToSend = confoirmationThatFinished.getBytes();			
		data = new byte[HEADER_LENGTH+dataToSend.length];
		data[TYPE_POS] = TYPE_STRING;
		data[LENGTH_POS] = (byte)dataToSend.length;
		System.arraycopy(dataToSend, 0, data, HEADER_LENGTH, dataToSend.length);

		terminal.println("Sending packet...");
		packet= new DatagramPacket(data, data.length);
		packet.setSocketAddress(dstAddress);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		terminal.println("Packet sent");
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean finishedTask()
	{
		String answer = terminal.read("Are you finished your task yet, \"yes\" or \"no\"?\n\n");
		if(answer.contains("yes"))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	public boolean workerTakeTask()
	{
		String answer = terminal.read("Do you wish to take this job? \"yes\" or \"no\".");
		if(answer.contains("yes"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
		
	private synchronized void start(int workerDstAddress) throws Exception
	{
		String workerName;
		
		workerName = (terminal.read("What is your name: "));
		terminal.println("Worker name: "+workerName);
		
		String dataString = (terminal.read("Please send \"yes\" to volunteer for work."));
		dataString += "\n";
		if(dataString.contains("yes"))
		{
			sendWorkerName(workerName);
		}
		else
		{
			terminal.println("Worker is not working.");
		}
		while(hasConnected)
		{
			this.wait();
			terminal.println("\nAwaiting network.");
		}
	}
	
	public static void main(String[] args) {
		int numberOfWorkers = 0;
		int workerDstAddress;
		
		try {
			Terminal terminal1= new Terminal("Worker");
			workerDstAddress = DEFAULT_SRC_PORT + numberOfWorkers;
			(new Worker(terminal1, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT + numberOfWorkers++)).start(workerDstAddress);
			terminal1.println("Program completed");

			Terminal terminal2= new Terminal("Worker");
			workerDstAddress = DEFAULT_SRC_PORT + numberOfWorkers;
			(new Worker(terminal2, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT + numberOfWorkers++)).start(workerDstAddress);
			terminal2.println("Program completed");
			
			Terminal terminal3= new Terminal("Worker");
			workerDstAddress = DEFAULT_SRC_PORT + numberOfWorkers;
			(new Worker(terminal3, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT + numberOfWorkers++)).start(workerDstAddress);
			terminal1.println("Program completed");
			
			Terminal terminal4= new Terminal("Worker");
			workerDstAddress = DEFAULT_SRC_PORT + numberOfWorkers;
			(new Worker(terminal4, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT + numberOfWorkers++)).start(workerDstAddress);
			terminal1.println("Program completed");
			
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
