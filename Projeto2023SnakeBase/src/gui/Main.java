package gui;

import java.io.Console;
import java.io.IOException;
import javax.net.ssl.StandardConstants;
import environment.LocalBoard;
import game.Server;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		LocalBoard board=new LocalBoard();
		SnakeGui game = new SnakeGui(board,600,0);
		game.init();
		// Launch server
		Server serverApp = new Server(board);
		serverApp.runServer();
	}
}
