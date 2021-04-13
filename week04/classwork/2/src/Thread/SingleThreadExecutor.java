package Thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SingleThreadExecutor {
    public static void main(String[] args) throws Exception {
        // 实现Callable接口的实例可以直接丢给线程池submit，并且可以获取返回值(Callable::call方法)，但不能直接丢给Thread::start执行
        // 使用FutureTask可以把Callable封装成一个带返回值的任务，FutureTask可以直接丢给Thread::start执行，也可以直接丢给线程池submit执行

        // 实现Runnable接口的实例可以直接丢给Thread::start执行，也可以直接丢给线程池submit，但不能获取返回值(Runnable::run方法)
        Callable<Integer> task = () -> {
            return sum();
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();

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
