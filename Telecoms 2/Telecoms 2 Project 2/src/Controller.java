import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.*;
import java.lang.System;

public class Controller extends Node implements Runnable {

    private Terminal terminal;
    private ArrayList<Integer> routers = new ArrayList<>();
    private static HashMap<Integer, int[]> flowTable = new HashMap<>();

    Controller(Terminal terminal, int port) {
        try {
            this.terminal = terminal;
            socket = new DatagramSocket(port);
            listener.go();
        } catch (java.lang.Exception e) { e.printStackTrace(); }
    }

    public synchronized void onReceipt(DatagramPacket packet) {
        byte[] data;
        String content;
        byte[] buffer;

        data = packet.getData();

        buffer = new byte[data[CONSTANT.LENGTH_POS]];
        System.arraycopy(data, CONSTANT.HEADER_LENGTH, buffer, 0, buffer.length);
        content = new String(buffer);
        int packetPort = packet.getPort();

        if (packetPort >= CONSTANT.STARTROUTER) {
            if (content.equals(CONSTANT.HELLO)) {
                routers.add(packetPort);
                sendMessage(CONSTANT.HELLO, packetPort);
            } else if (content.contains(CONSTANT.NEXT)) {
                sendMessage(next(packetPort, Integer.parseInt(content.replaceAll("[^0-9]", ""))), packetPort);
            }
        }
    }

    static String next(int source, int destination) {
        int[] route = flowTable.get(destination);

        for (int i = 0; i < route.length; i++) {
            if (i + 1 == route.length)
                return Integer.toString(destination);
            if (source == route[i])
                return Integer.toString(route[i+1]);
        }
        return "";
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

    private synchronized void generateFlowTable() {
        for (int i = 0; i < CONSTANT.ENDUSERS; i++) {
            int[] routerPorts = new int[routers.size()];
            for (int j = 0; j < routers.size(); j++) {
                routerPorts[j] = routers.get(j);
            }
            flowTable.put(CONSTANT.STARTENDUSERONE + i, routerPorts);
        }
    }

    public synchronized void run() {
        try { this.wait(2500); }
        catch (InterruptedException e) { e.printStackTrace();
        }
        generateFlowTable();

        try { this.wait(); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    public static void main(String[] args) 
    {
    	try
    	{
    		Terminal terminal = new Terminal("Controller:");
    		(new Controller(terminal, CONSTANT.CONTROLLERPORT)).run();
    		terminal.println("Program Complete.");
    	}
		catch(java.lang.Exception e)
		{
			e.printStackTrace();
		}
    }
}