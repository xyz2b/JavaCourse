package Thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CachedThreadPool {
    public static void main(String[] args) throws Exception {
        Callable<Integer> task = () -> {
            return sum();
        };

        ExecutorService executorService = Executors.newCachedThreadPool();

        Future<Integer> future = executorService.submit(task);

        try {
            System.out.println("异步计算结果为：" + future.get());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            executorService.shutdown();
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
