
public class Ex3A {

	static boolean ans;
	static boolean flag = false;

	public boolean isPrime(long n, double maxTime) throws RuntimeException {

		FirstThread t = new FirstThread(n, maxTime);
		t.start();

		try {
			Thread.sleep((long) (maxTime * 1000));

			if (!flag) {
				throw new RuntimeException("maxTime passed");
			}

		} catch (InterruptedException e) {}
		
		return ans;
	}

	static class FirstThread extends Thread {

		private double time;
		private long n;

		public FirstThread(long n, double maxTime) {
			this.time = maxTime;
			this.n = n;
		}

		public void run() {
			ans = isPrime(n);
			flag = true;
		}

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
