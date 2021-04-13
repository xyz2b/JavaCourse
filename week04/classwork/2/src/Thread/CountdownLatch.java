package Thread;

import java.util.concurrent.CountDownLatch;

public class CountdownLatch {
    static class Task implements Runnable {
        public static int sum;
        private final CountDownLatch countDownLatch;

        public Task(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            sum = sum();
            countDownLatch.countDown();
        }

        private int sum() {
            return fibo(36);
        }

        private int fibo(int a) {
            if ( a < 2)
                return 1;
            return fibo(a-1) + fibo(a-2);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        for (int i = 0; i < 2; i++) {
            new Thread(new Task(countDownLatch)).start();
        }

        countDownLatch.await();
        System.out.println("异步计算结果为：" + Task.sum);
    }
}
