package game;

import environment.Board;
import environment.Cell;

/** Class for a remote snake, controlled by a human 
 * 
 * @author luismota
 *
 */

public class HumanSnake extends Snake {

	public HumanSnake(int id, Board board) {
		super(id,board);
	}

	@Override
	public void move(Cell cell) throws InterruptedException { 	

		if(!getBoard().isFinished()) {
			if(!isOwnSnake(cell) && !(cell.isOcupied())) {
				cell.request(this);
				if(cell.isOcupiedByGoal()) {
					int value = cell.getGoal().getValue();
					setSize(getSize() + value);
					cell.getGoal().captureGoal();
				}
				super.move(cell);	
			}
		}
	}
}
