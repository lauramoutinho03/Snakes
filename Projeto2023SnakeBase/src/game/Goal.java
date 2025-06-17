package game;

import java.io.Serializable;

import environment.Board;
import environment.Cell;

public class Goal extends GameElement implements Serializable{
	private int value=1;
	private Board board;
	public static final int MAX_VALUE=10;

	public Goal( Board board2) {
		this.board = board2;
	}

	public int getValue() {
		return value;
	}

	public void incrementValue() throws InterruptedException {
		//TODO
		if(value < MAX_VALUE - 1) {
			value++;
		}
	}

	public int captureGoal() throws InterruptedException {
		//		TODO
		Cell cell = board.getCell(board.getGoalPosition());

		if (cell == null || !(cell.getGameElement() instanceof Goal)) { //se for null ou se não for um Goal
			return -1;
		}

		Goal capturedGoal = cell.removeGoal();

		if(capturedGoal.getValue() == MAX_VALUE - 1) { //Se o valor for igual a 9
			board.setFinished(true);
			board.shutDown();
		} else {
			incrementValue();
			board.addGameElement(this);
			board.setChanged();
		}
		return 0;
	}
}
