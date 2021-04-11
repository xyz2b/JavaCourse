package Thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureTaskThread {
    public static void main(String[] args) {
        FutureTask<Integer> task = new FutureTask<>((Callable<Integer>) () -> {
            return sum();
        });

        new Thread(task, "又返回值的线程").start();

        try {
            System.out.println("异步计算结果为：" + task.get());
        } catch (Exception ex) {
            ex.printStackTrace();
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
