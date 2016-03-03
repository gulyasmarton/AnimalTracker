package hu.elte.animaltracker.model;

/**
 * This is a universal paralleling processor class (Multi-core CPU). Use this as
 * an anonymous class.
 * 
 */
public abstract class Parallel {
	private int processors;
	private int size;

	/**
	 * Creates a new Parallel process.
	 * 
	 * @param size
	 *            sets the number of processed elements.
	 */
	public Parallel(int size) {
		this.processors = Runtime.getRuntime().availableProcessors();
		this.size = size;
	}

	/**
	 * This procedure starts the parallel processing and returns when all
	 * threads are finished.
	 */
	public void start() {
		Thread[] threads = new Thread[processors];
		for (int cpu = 0; cpu < threads.length; cpu++) {
			threads[cpu] = new Thread(new Runnable() {

				@Override
				public void run() {
					int threadId = (int) (Thread.currentThread().getId() % processors);
					for (int idx = threadId; idx < size; idx += processors) {
						coreProcess(idx);
					}

				}

			});
			threads[cpu].start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Converts a 1D array index to a 2D array index and returns the first
	 * index.
	 * 
	 * @param idx
	 *            1D index
	 * @param width
	 *            width of 2D array
	 * @return first index of 2D array
	 */
	public int getX(int idx, int width) {
		return idx % width;
	}

	/**
	 * Converts a 1D array index to a 2D array index and returns the last index.
	 * 
	 * @param idx
	 *            1D index
	 * @param width
	 *            width of 2D array
	 * @return last index of 2D array
	 */
	public int getY(int idx, int width) {
		return idx / width;
	}

	/**
	 * Carries out the processing of a given thread.
	 * 
	 * @param idx
	 *            index of given thread.
	 */
	public abstract void coreProcess(int idx);
}
