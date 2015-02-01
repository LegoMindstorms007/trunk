package Programs;

/**
 * Program interface, every program should implement this interface
 * 
 * @author Dominik Muth
 * 
 */
public interface Program extends Runnable {

	/**
	 * stops the thread and does a clean up (e.g. driving the sensor arm to the
	 * center position)
	 */
	public void halt();

	/**
	 * check if the thread is running
	 * 
	 * @return whether the thread is running or not
	 */
	public boolean isRunning();
}
