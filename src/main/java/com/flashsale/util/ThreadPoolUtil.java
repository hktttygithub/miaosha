package com.flashsale.util;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.*;

@Component
public class ThreadPoolUtil {
    private Executor pool;

    {
        pool = new ThreadPoolExecutor(15, 15, 1000, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5));

    }

    public Executor getPool() {
        return pool;
    }

    ThreadPoolExecutor tpe = ((ThreadPoolExecutor) pool);
    private static volatile boolean stop = true;

    @RequestMapping("/stop")
    @ResponseBody
    public String stop(){
        stop = !stop;
        return "启动/停止";
    }


    @RequestMapping("/poolStatus")
    @ResponseBody
    public String status() {
        stop = false;
        new Thread(() -> {
            try {
                poolStatus();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return "启动线程池监控";
    }

    private void poolStatus() throws InterruptedException {
        while (!stop) {
            System.out.println();
            int queueSize = tpe.getQueue().size();
            System.out.println("当前排队线程数：" + queueSize);
            int activeCount = tpe.getActiveCount();
            System.out.println("当前活动线程数：" + activeCount);
            long completedTaskCount = tpe.getCompletedTaskCount();
            System.out.println("执行完成线程数：" + completedTaskCount);
            long taskCount = tpe.getTaskCount();
            System.out.println("总线程数：" + taskCount);
            Thread.sleep(2000);
        }
    }
}


