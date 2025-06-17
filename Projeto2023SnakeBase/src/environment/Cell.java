package environment;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.midi.SysexMessage;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;

/** Main class for game representation. 
 * 
 * @author luismota
 *
 */
public class Cell implements Serializable{
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement=null;
	private Lock lock = new ReentrantLock();
	private Condition notOcupied = lock.newCondition();
	private Condition notEmpty = lock.newCondition();


	public Cell(BoardPosition position) {
		super();
		this.position = position;
	}

	public GameElement getGameElement() {
		return gameElement;
	}

	public BoardPosition getPosition() {
		return position;
	}

	public void request(Snake snake) throws InterruptedException {
		//TODO coordination and mutual exclusion
		lock.lock();
		try {
			while(isOcupied()) {
				notOcupied.await();
			}
			ocuppyingSnake=snake;
			notEmpty.signalAll(); //notifica que não está vazia
		} finally {
			lock.unlock();
		}
	}

	public void release() throws InterruptedException {
		//TODO
		lock.lock();
		try {
			while(!isOcupied()) {
				notEmpty.await();
			}
			ocuppyingSnake= null;
			notOcupied.signalAll(); //notifica que não está ocupada
		} finally {
			lock.unlock();
		}
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake!=null;
	}


	public boolean setGameElement(GameElement element) throws InterruptedException {
		// TODO coordination and mutual exclusion
		lock.lock();
		try {
			if(isOcupied() || isOcupiedByGoal()) {
				//notOcupied.await(); //nao e suposto ficar em espera
				return false;
			}
			gameElement=element;
			//notEmpty.signalAll();
			return true;
		} finally {
			lock.unlock();
		}

	}

	public boolean isOcupied() {
		return isOcupiedBySnake() || (gameElement!=null && gameElement instanceof Obstacle);
	}

	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}

	public  Goal removeGoal() {
		// TODO
		if(gameElement instanceof Goal) {
			Goal goalRemoved = (Goal) gameElement;
			gameElement = null;
			return goalRemoved;
		}
		return null;
	}

	public void removeObstacle() {
		//TODO
		if(gameElement instanceof Obstacle) {
			gameElement = null;
		}
	}

	public Goal getGoal() {
		return (Goal)gameElement;
	}

	public Obstacle getObstacle() {
		return (Obstacle)gameElement;
	}

	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}



}
