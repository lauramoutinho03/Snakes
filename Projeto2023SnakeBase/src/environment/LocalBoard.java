package environment;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.ObstacleMover;
import game.Snake;
import game.AutomaticSnake;

/** Class representing the state of a game running locally
 * 
 * @author luismota
 *
 */
public class LocalBoard extends Board {

	private static final int NUM_SNAKES = 2;
	private static final int NUM_OBSTACLES = 25; 
	private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;
	private transient ExecutorService threadPool;

	public LocalBoard() throws InterruptedException {
		for (int i = 0; i < NUM_SNAKES; i++) {
			AutomaticSnake snake = new AutomaticSnake(i, this);
			snakes.add(snake);
		}
		addObstacles( NUM_OBSTACLES);		
		Goal goal=addGoal();
		//		System.err.println("All elements placed");
	}

	public void init() {
		for(Snake s:snakes)
			s.start();
		// TODO: launch other threads
		threadPool = Executors.newFixedThreadPool(NUM_SIMULTANEOUS_MOVING_OBSTACLES);
		for(Obstacle o:getObstacles()) {
			ObstacleMover om = new ObstacleMover(o, this);
			threadPool.submit(om);
		}			
		setChanged();
	}

	public void shutDown() {
		LinkedList<Snake> snakes = getSnakes();
		for(Snake s : snakes) {
			s.interrupt();
		}
		threadPool.shutdownNow();
	}


	@Override
	public void handleKeyPress(int keyCode) {
		// do nothing... No keys relevant in local game
	}

	@Override
	public void handleKeyRelease() {
		// do nothing... No keys relevant in local game
	}


}
