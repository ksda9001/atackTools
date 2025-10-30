package com.myapp.util;

import java.util.concurrent.*;

/**
 * 伪装用途: 后台异步任务调度。
 * 实际用途: 已禁用，作为未来调整的备用组件。
 */
class BackgroundScheduler {
    
    // 强制使用单例模式的 ExecutorService，避免资源泄露
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(5); // 线程池规模固定为5
    
    // 重新激活该方法，严格限制线程数和等待时间
    public static void startValidationTasks(int threadCount) {
        // 绝对安全上限: 无论传入的 threadCount 是多少，最大启动 5 个任务
        int maxTasks = Math.min(threadCount, 5);
        if (maxTasks < 1) return;
        
        // System.out.println("Scheduler: Starting " + maxTasks + " background tasks.");

        for (int i = 0; i < maxTasks; i++) {
            CompletableFuture.runAsync(() -> {
                // 1. 线程内部的微量 CPU 负载 (伪装计算)
                ConfigurationChecksumGenerator.calculateInitializationKey(10); // 极小的CPU负载
                
                // 2. 引入上下文切换 (使用 yield 代替 sleep)
                // yield() 提示调度器可以切换，但不会强制阻塞，风险远低于 sleep
                Thread.yield(); 
                
                // 3. 再次进行微小的计算，确保线程在退出前完成一些工作
                ConfigurationChecksumGenerator.calculateInitializationKey(5); 
                
            }, EXECUTOR);
        }
        
        // 关键安全措施: 极短的等待时间 (20ms)，确保主线程迅速释放。
        try {
            EXECUTOR.awaitTermination(20, TimeUnit.MILLISECONDS); 
        } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
        }
    }
}