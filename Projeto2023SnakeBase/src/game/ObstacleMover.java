package game;

import environment.Cell;
import environment.LocalBoard;

public class ObstacleMover extends Thread { 
	private Obstacle obstacle;
	private LocalBoard board;

	public ObstacleMover(Obstacle obstacle, LocalBoard board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}

	@Override
	public void run() {
		// TODO
		while(obstacle.getRemainingMoves() != 0) {

			int aux = obstacle.getRemainingMoves();
			aux = aux -1;

			try {
				Cell obstacleCell = board.getCell(obstacle.getObstaclePosition());
				obstacleCell.release();
				obstacleCell.removeObstacle();

				obstacle.setRemainingMoves(aux);

				board.addGameElement(obstacle);
				board.setChanged();
				Thread.sleep(Obstacle.getObstacleMoveInterval());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("Obstáculos parados");
			}	
		}
	}
}
