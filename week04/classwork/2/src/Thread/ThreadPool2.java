package Thread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;


public class ThreadPool2 {
    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();

        // Future的实现或者Future子类的实现都可以直接丢给线程执行，并可以通过future.get获取返回值
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
