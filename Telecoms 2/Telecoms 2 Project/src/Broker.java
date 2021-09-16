import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Broker extends Node {
	static final int DEFAULT_PORT = 50001;

	static final int HEADER_LENGTH = 2;
	static final int TYPE_POS = 0;
	
	static final byte TYPE_UNKNOWN = 0;
	
	static final byte TYPE_STRING = 1;
	static final int LENGTH_POS = 1;
	
	static final byte TYPE_ACK = 2;
	static final int ACKCODE_POS = 1;
	static final byte ACK_ALLOK = 10;

	static final int MAX_WORKERS = 10;
	
	Terminal terminal;
	
	String[] workerNames = new String[MAX_WORKERS];
	DatagramPacket[] workerData = new DatagramPacket[MAX_WORKERS];
	public static boolean[] readyToWork = new boolean[MAX_WORKERS];
	
	
	int count = 0;
	int numberOfAssignments = 0;
	
	/*
	 * 
	 */
	Broker(Terminal terminal, int port) {
		try {
			this.terminal= terminal;
			socket= new DatagramSocket(port);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}
	
	public void whatWorkersAvailable()
	{
		for(int counter = 0; counter<count; counter++)
		{
			if(readyToWork[counter])
			{
				terminal.println(workerNames[counter]+" worker is available to work.");
			}
		}
	}
	
	public int seeIfWorkerIsBusy()
	{
		for(int counter = 0; counter<MAX_WORKERS; counter++)
		{
			
			if(readyToWork[counter])
			{
				return counter;
			}
		}
		return -1;
	}
	
	public void sendAssignment(DatagramPacket[] workerData, int availableWorker, String reply) 
	{
		byte[] data = null;
		byte[] dataToSend = null;
		
		DatagramPacket response = null;
		
		String assignment = "Assignment: "+reply;
		dataToSend = assignment.getBytes();			
		data = new byte[HEADER_LENGTH+dataToSend.length];
		data[TYPE_POS] = TYPE_STRING;
		data[LENGTH_POS] = (byte)dataToSend.length;
		System.arraycopy(dataToSend, 0, data, HEADER_LENGTH, dataToSend.length);

		terminal.println("Sending packet...");
		response = new DatagramPacket(data, data.length);
		response.setSocketAddress(workerData[availableWorker].getSocketAddress());
		readyToWork[availableWorker] = false;
		try {
			socket.send(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		try {
			String content;
			byte[] data;
			byte[] buffer;
			
			data = packet.getData();			
			switch(data[TYPE_POS]) {
			case TYPE_STRING:
				buffer= new byte[data[LENGTH_POS]];
				System.arraycopy(data, HEADER_LENGTH, buffer, 0, buffer.length);
				content= new String(buffer);
				if(content.contains("Worker"))
				{
					terminal.println(content + " is volunteering for work.");
					workerNames[count] = content;
					workerData[count] = packet;
					readyToWork[count] = true;
					count++;
				}
				else if(content.contains("Command and Control:"))
				{
					if(content.contains("Command and Control: Option 1"))
					{
						whatWorkersAvailable();
					}
					else if(content.contains("Command and Control: Option 2"))
					{
						numberOfAssignments=1;
						String assignment = terminal.read("What assignment do you wish to give the workers?");
						sendAssignment(workerData, seeIfWorkerIsBusy(), assignment);
					}
					else if(content.contains("Command and Control: Option 3"))
					{
						String assignment = terminal.read("What assignment do you wish to give the workers?");
						for(int workerCounter = 0; workerCounter<count; workerCounter++)
						{
							if(readyToWork[workerCounter])
							{
								numberOfAssignments++;
								sendAssignment(workerData, seeIfWorkerIsBusy(), assignment);
							}
						}
					}
					else if(content.contains("Command and Control: Option 4"))
					{
						String text = terminal.read("How many wokrers do you want?");
						int amountOfWorkersNeeded = Integer.parseInt(text);
						int counter2 = 0;
						for(int counter1 = 0; counter1<count; counter1++)
						{
							if(readyToWork[counter1])
							{
								counter2++;
							}
						}
						if(counter2<amountOfWorkersNeeded)
						{
							terminal.println("Not enough workers available for this task. Please restart the program.");
						}
						else
						{
							numberOfAssignments=amountOfWorkersNeeded;
							String assignment = terminal.read("What assignment do you wish to give the workers?");
							for(int workerCounter = 0; amountOfWorkersNeeded>0; workerCounter++)
							{
								if(readyToWork[workerCounter])
								{
									amountOfWorkersNeeded--;
									sendAssignment(workerData, seeIfWorkerIsBusy(), assignment);
								}
							}
						}
					}
				}
				else if(content.contains("Assignment:"))
				{
					if(content.contains("Assignment finished sucsessfully."))
					{
						terminal.println(content);
						numberOfAssignments--;
					}
					else if(content.contains("Assignment failed."))
					{
						terminal.println(content);
						numberOfAssignments--;
					}
				}
				// You could test here if the String says "end" and terminate the
				// program with a "this.notify()" that wakes up the start() method.
				if(numberOfAssignments == 0)
				{
					data = new byte[HEADER_LENGTH];
					data[TYPE_POS] = TYPE_ACK;
					data[ACKCODE_POS] = ACK_ALLOK;
				
					DatagramPacket response;
					response = new DatagramPacket(data, data.length);
					response.setSocketAddress(packet.getSocketAddress());
					socket.send(response);
				}
				break;
			default:
				terminal.println("Unexpected packet" + packet.toString());
			}

		}
		catch(Exception e) {e.printStackTrace();}
	}

	
	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact");
		this.wait();
	}
	
	/*
	 * 
	 */
	public static void main(String[] args) {
		try {	
			for(int count1 = 0; count1 < readyToWork.length; count1++)
			{
				readyToWork[count1] = true;
			}
			
			Terminal terminal= new Terminal("Broker:");
			(new Broker(terminal, DEFAULT_PORT)).start();
			terminal.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}