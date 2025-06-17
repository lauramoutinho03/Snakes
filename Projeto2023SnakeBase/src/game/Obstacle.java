package game;

import java.io.Serializable;

import environment.Board;
import environment.BoardPosition;

public class Obstacle extends GameElement implements Serializable{

	private BoardPosition obstaclePosition; 

	private static final int NUM_MOVES=3;
	private static final int OBSTACLE_MOVE_INTERVAL = 600; 
	private int remainingMoves=NUM_MOVES;
	private Board board;

	public Obstacle(Board board) {
		super();
		this.board = board;
	}

	public void setRemainingMoves(int remainingMoves) {
		this.remainingMoves = remainingMoves;
	}

	public int getRemainingMoves() {
		return remainingMoves;
	}

	public void setObstaclePosition(BoardPosition obstaclePosition) {
		this.obstaclePosition = obstaclePosition;
	}

	public BoardPosition getObstaclePosition() {
		return obstaclePosition;
	}

	public static int getObstacleMoveInterval() {
		return OBSTACLE_MOVE_INTERVAL;
	}

}
