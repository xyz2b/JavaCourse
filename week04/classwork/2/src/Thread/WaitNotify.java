package Thread;

class Task2 implements Runnable {
    public int sum;
    private final Object signal;

    public Task2(Object signal) {
        this.signal = signal;
    }

    @Override
    public void run() {
        this.sum = sum();
        synchronized (signal) {
            signal.notify();
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

public class WaitNotify {
    private static final Object signal = new Object();

    public static void main(String[] args) throws InterruptedException {
        Task2 task = new Task2(signal);

        new Thread(task, "task").start();
        synchronized (signal) {
            signal.wait();
        }

        System.out.println("异步计算结果为：" + task.sum);
    }
}
