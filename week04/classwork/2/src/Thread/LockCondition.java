package Thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockCondition {
    final static Lock lock = new ReentrantLock();
    final static Condition signal  = lock.newCondition();

    static class Task implements Runnable {
        public int sum;
        private final Condition signal;
        private final Lock lock;

        public Task(Condition signal, Lock lock) {
            this.signal = signal;
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.lock();

            try {
                this.sum = sum();
                signal.signal();
            } finally {
                lock.unlock();
            }
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
        lock.lock();
        try {
            Task task = new Task(signal, lock);

            new Thread(task, "task").start();

            signal.await();

            System.out.println("异步计算结果为：" + task.sum);
        } finally {
            lock.unlock();
        }
    }
}