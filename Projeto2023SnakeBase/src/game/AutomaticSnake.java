package game;

import java.util.List;


import environment.LocalBoard;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);

	}

	public Cell getCloserCell(Cell cell) { // funcao p/encontrar a cell p/onde vai
		List<BoardPosition> possibleCells = getBoard().getNeighboringPositions(cell);
		double min = 100000;
		Cell closerCell = null;
		for (BoardPosition b : possibleCells) {
			if(b.distanceTo(getBoard().getGoalPosition()) < min && !isOwnSnake(getBoard().getCell(b))) { // menor distancia e que n/e a propria snake
				min = b.distanceTo(getBoard().getGoalPosition());
				closerCell = getBoard().getCell(b);
			}	
		}
		return closerCell;
	}

	public Cell getNewCloserCell(Cell cell) { // funcao utilizada depois de clicar no botao p/encontra a nova cell
		List<BoardPosition> possibleCells = getBoard().getNeighboringPositions(cell);
		double min = 100000;
		Cell newCloserCell = null;
		for (BoardPosition b : possibleCells) {
			if(b.distanceTo(getBoard().getGoalPosition()) < min && !isOwnSnake(getBoard().getCell(b)) && !(getBoard().getCell(b).isOcupied())) {
				min = b.distanceTo(getBoard().getGoalPosition());
				newCloserCell = getBoard().getCell(b);
			}	
		}
		return newCloserCell;
	}


	@Override
	public void run() {
		doInitialPositioning();
		System.err.println("initial size:"+cells.size());

		//TODO: automatic movement
		while(!interrupted()) { // enquanto n/esta interrompido
			Cell cell = getCells().getFirst();
			Cell closerCell = getCloserCell(cell);
			try {
				if(closerCell == null) {
					System.out.println("A cobra " + getIdentification() + " está presa");
				}
				closerCell.request(this);
				if(closerCell.isOcupiedByGoal()) {
					int value = closerCell.getGoal().getValue();
					setSize(getSize() + value);
					closerCell.getGoal().captureGoal();
				}
				move(closerCell);
				getBoard().setChanged();
				sleep(Board.PLAYER_PLAY_INTERVAL);

			} catch (InterruptedException e) { // quando esta interrompido
				// TODO Auto-generated catch block
				if(getBoard().isFinished()) { // verifica se entrou no catch porque o jogo acabou
					System.out.println("O jogo acabou para a cobra " + getIdentification());
					break;
				} else {
					Cell newCell = getCells().getFirst();
					Cell newCloserCell = getNewCloserCell(newCell);
					try {
						if(newCloserCell == null) {
							System.out.println("A cobra " + getIdentification() + " está presa");
						}
						newCloserCell.request(this);
						if(closerCell.isOcupiedByGoal()) {
							int value = closerCell.getGoal().getValue();
							setSize(getSize()+value);
							closerCell.getGoal().captureGoal();
						} 
						move(newCloserCell);
						getBoard().setChanged();
						sleep(Board.PLAYER_PLAY_INTERVAL);

					} catch (InterruptedException e1) {

					}
				}

			}	
		}

	}

}
