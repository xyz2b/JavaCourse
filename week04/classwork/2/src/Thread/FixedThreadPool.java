package Thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FixedThreadPool {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(6);

        FutureTask<Integer> task = new FutureTask<>((Callable<Integer>) () -> {
            return sum();
        });

        pool.submit(task);

        try {
            System.out.println("异步计算结果为：" + task.get());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
