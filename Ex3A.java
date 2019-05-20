/**
 * Ex3A
 * This class computes whether a number is prime or not, using a Thread, 
 * if the calculation is not completed at the given time - the program will stop and throw exception.
 * @author Olga and Daniel
 */
public class Ex3A {

	static boolean ans;
	static boolean flag = false;

	/**
	 * isPrime
	 * Calculates whether the number is a prime number, if the maxTime passed the function throws an error.
	 * @param n - the number
	 * @param maxTime - the maximum time to check whether the number is a prime or not
	 * @return - true if its a prime number, false otherwise
	 * @throws RuntimeException - run time exception
	 */
	public boolean isPrime(long n, double maxTime) throws RuntimeException {

		//create a new thread for the examination
		FirstThread t = new FirstThread(n, maxTime);
		
		//start the new thread (run function will be activated)
		t.start(); 

		try {
			
			//Stop the current thread that run for the specified maxTime.
			//now the created thread will be activated.
			Thread.sleep((long) (maxTime * 1000));

			//if the time expires before the function returns an answer throw exception
			if (!flag) {
				throw new RuntimeException("maxTime passed");
			}

		} catch (InterruptedException e) {}
		
		return ans;
	}

	/**
	 * FirstThread Inner class that represents a Thread that calculate if number is prime or not.
	 * @author Olga and Daniel
	 */
	static class FirstThread extends Thread {

		private double time;
		private long n;

		/**
		 * FirstThread constructor
		 * @param n - the number the need to check if it prime
		 * @param maxTime - the time that the function has to check
		 */
		public FirstThread(long n, double maxTime) {
			this.time = maxTime;
			this.n = n;
		}

		/**
		 * run
		 * this function begin the Thread. When it stars the calculation begin
		 */
		public void run() {
			ans = isPrime(n);
			flag = true;
		}

		/**
		 * isPrime
		 * Calculates whether the number is a prime or not.
		 * @param n - the number
		 * @return - true if its a prime number, false otherwise
		 */
		public static boolean isPrime(long n) {
			boolean ans = true;
			if (n < 2)
				throw new RuntimeException("ERR: the parameter to the isPrime function must be > 1 (got " + n + ")!");
			int i = 2;
			double ns = Math.sqrt(n);
			while (i <= ns && ans) {
				if (n % i == 0)
					ans = false;
				i = i + 1;
			}
			if (Math.random() < Ex3_tester.ENDLESS_LOOP)
				while (true)
					;
			return ans;
		}
	}
}
