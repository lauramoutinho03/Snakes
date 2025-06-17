package remote;

import java.awt.event.KeyEvent;
import environment.Board;

/** Remote representation of the game, no local threads involved.
 * Game state will be changed when updated info is received from Srver.
 * Only for part II of the project.
 * @author luismota
 *
 */

public class RemoteBoard extends Board{
	private Client client;

	public RemoteBoard(Client client) {
		this.client = client;
	}

	@Override
	public void handleKeyPress(int keyCode) {
		//TODO
		switch(keyCode) {
		case(KeyEvent.VK_LEFT):
			client.sendDirection("left");

		break;
		case(KeyEvent.VK_RIGHT):
			client.sendDirection("right"); 

		break;
		case(KeyEvent.VK_UP):
			client.sendDirection("up"); 

		break;
		case(KeyEvent.VK_DOWN):
			client.sendDirection("down"); 

		break;
		default:
			System.out.println("usar setas");
		}
	}

	// update à board com o que é recebido do cliente (e que este recebeu do servidor)
	public void updateRemoteBoard(Board localBoard) {

		this.cells = localBoard.getCells();
		this.snakes = localBoard.getSnakes();
		this.isFinished = localBoard.isFinished();

		this.setChanged();
	}

	@Override
	public void handleKeyRelease() {
		// TODO
	}

	@Override
	public void init() {
		// TODO 		
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}
}
