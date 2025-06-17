package game;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import environment.Board;
import environment.BoardPosition;
import environment.LocalBoard;

public class Server {
	private ServerSocket server;
	public LocalBoard localBoard;

	public Server(LocalBoard localBoard) {
		this.localBoard = localBoard;
	}


	public void runServer() {
		try {
			// 1. Create socket
			server = new ServerSocket(12345,1); 

			while(true) {
				// 2. Wait for a new connection
				waitForConnection();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void waitForConnection() throws IOException {
		Socket connection = server.accept(); // WAIT!!
		//Create a connection manager (ConnectionHandler)
		ConnectionHandler handler = new ConnectionHandler(connection);
		handler.start();
		System.out.println("[new connection] " + connection.getInetAddress().getHostName());
	}

	private class ConnectionHandler extends Thread{
		private Socket connection; 
		private Scanner in; 
		private ObjectOutputStream out; 
		private HumanSnake humanSnake;

		public ConnectionHandler(Socket connection) {
			this.connection = connection;
		}

		@Override
		public void run() {
			try {
				getStreams();
				humanSnake = new HumanSnake(5, localBoard);
				localBoard.addSnake(humanSnake);
				humanSnake.doInitialPositioning();
				processConnection();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				closeConnection();
			}
		}

		private void getStreams() throws IOException {
			// Writer
			out = new ObjectOutputStream(connection.getOutputStream()); 
			// Reader
			in = new Scanner(connection.getInputStream());
			System.out.println("[ready to proceed connection]");
		}

		private void processConnection() throws InterruptedException {
			String msg;

			while(true) {
				try {
					out.reset();
					out.writeObject(localBoard);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);

				msg = in.nextLine();
				BoardPosition positionHumanSnake = humanSnake.getCells().getFirst().getPosition();
				moveTo(positionHumanSnake, msg);
				localBoard.setChanged();
			}
		}

		private void moveTo(BoardPosition pHumanSnake, String msg) throws InterruptedException {
			switch(msg) {
			case("left"):
				if(pHumanSnake.x>0) {
					BoardPosition newbp = pHumanSnake.getCellLeft();
					humanSnake.move(localBoard.getCell(newbp));
				}
			break;
			case("right"):
				if(pHumanSnake.x<Board.NUM_COLUMNS-1) {
					BoardPosition newbp1 = pHumanSnake.getCellRight();
					humanSnake.move(localBoard.getCell(newbp1));
				}
			break;
			case("up"):
				if(pHumanSnake.y>0) {
					BoardPosition newbp2 = pHumanSnake.getCellAbove();
					humanSnake.move(localBoard.getCell(newbp2));
				}
			break;
			case("down"):
				if(pHumanSnake.y<Board.NUM_ROWS-1) {
					BoardPosition newbp3 = pHumanSnake.getCellBelow();
					humanSnake.move(localBoard.getCell(newbp3));
				}
			break;
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
	}
}
