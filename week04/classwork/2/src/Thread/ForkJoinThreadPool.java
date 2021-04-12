package Thread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;


public class ForkJoinThreadPool {
    public static void main(String[] args) {
        java.util.concurrent.ForkJoinPool pool = java.util.concurrent.ForkJoinPool.commonPool();

        Future<Integer> future = pool.submit(new CalTask());

        try {
            System.out.println("异步计算结果为：" + future.get());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pool.shutdown();
        }
    }

    static class CalTask extends RecursiveTask<Integer> {

        @Override
        protected Integer compute() {
            return sum();
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
