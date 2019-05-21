package juc;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * @Author: 刘艳明
 * @Date: 19-5-21 上午9:16
 */
public class TestForkJoin {
    public static void main(String[] args) {
        Instant start = Instant.now();

        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinSumCalculate(0, 100000000000L);

        long sum = pool.invoke(task);
        System.out.println(sum);

        Instant end = Instant.now();
        System.out.println("duration:" + Duration.between(start,end).toMillis());
    }

    @Test
    public void test(){
        Instant start = Instant.now();
        long sum = 0;
        for (long i =0; i <= 100000000000L;i++){
            sum += i;
        }
        System.out.println(sum);
        Instant end = Instant.now();
        System.out.println("duration:" + Duration.between(start,end).toMillis());
    }

    @Test
    public void testJava8(){
        Instant start = Instant.now();

        Long sum = LongStream.rangeClosed(0L, 100000000000L)
                .parallel()
                .reduce(0L, Long::sum);
        System.out.println(sum);

        Instant end = Instant.now();
        System.out.println("duration:" + Duration.between(start,end).toMillis());
    }
}

class ForkJoinSumCalculate extends RecursiveTask<Long>{

    private long start;
    private long end;

    private static final long HOLD = 1000000L;//临界值

    public ForkJoinSumCalculate(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * The main computation performed by this task.
     *
     * @return the result of the computation
     */
    @Override
    protected Long compute() {
        long len = end - start;
        if (len <= HOLD) {
            long sum = 0;
            for (long i = start; i<= end; i++){
                sum += i;
            }

            return sum;
        } else {
//            System.out.println("fork");
            long middle = (start + end) / 2;
            ForkJoinSumCalculate left = new ForkJoinSumCalculate(start, middle);
            left.fork();//进行拆分,同时压入线程队列

            ForkJoinSumCalculate right = new ForkJoinSumCalculate(middle+1, end);
            right.fork();

            return left.join() + right.join();
        }

    }
}
