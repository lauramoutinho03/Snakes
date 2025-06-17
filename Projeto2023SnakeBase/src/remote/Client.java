package remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import environment.Board;
import gui.SnakeGui;


/** Remore client, only for part II
 * 
 * @author luismota
 *
 */

public class Client {
	private Socket connection; 
	private ObjectInputStream in; 
	private PrintWriter out; 

	private InetAddress serverAddress;
	private int port;

	private RemoteBoard rboard;

	public Client(InetAddress serverAddress, int port) {
		this.serverAddress = serverAddress;
		this.port = port;

		rboard = new RemoteBoard(this);
		SnakeGui game = new SnakeGui(rboard,600,0);
		game.init();
	}

	public void runClient() {
		try {
			// 1. Connect to server
			connection = new Socket(serverAddress, port);
			// 2. Get streams
			getStreams();
			// 3. Process connection
			processConnection();
		} catch(IOException e) {
			//handle exception
		}finally {
			// 4. Close connection
			closeConnection();
		}
	}

	private void getStreams() throws IOException {
		// Writer
		out = new PrintWriter(connection.getOutputStream(), true); 
		// Reader
		in = new ObjectInputStream(connection.getInputStream()); 
		System.out.println("[ready to proceed connection]");

	}
	private void processConnection() {
		//dar update à board com o objeto recebido
		while(true) {
			try {  
				rboard.updateRemoteBoard((Board) in.readObject());

			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void closeConnection() {
		try {
			if(in != null)
				in.close();
			if(out != null)
				out.close();
			if(connection != null)
				connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sendDirection(String msg) {
		out.println(msg);
	}

	public static void main(String[] args) throws UnknownHostException {		
		Client clientApp = new Client(InetAddress.getByName("localhost"), 12345); 
		clientApp.runClient();
	}
}
