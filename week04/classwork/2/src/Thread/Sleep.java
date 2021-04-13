package Thread;

public class Sleep {
    static class Task implements Runnable {
        public int sum;

        @Override
        public void run() {
            this.sum = sum();
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
        Task task = new Task();
        new Thread(task).start();

        Thread.sleep(1000);

        System.out.println("异步计算结果为：" + task.sum);
    }


}
