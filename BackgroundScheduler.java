import java.util.concurrent.*;

/**
 * 伪装用途: 后台异步任务调度、并行数据处理。
 * 实际用途: 大量上下文切换、增加调度器负担。
 */
class BackgroundScheduler {
    
    // 伪装: 专用线程池用于"轻量级异步通知"
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    
    // 伪装方法: 启动一组"验证任务"
    public static void startValidationTasks(int threadCount) {
        if (threadCount < 2) return;
        
        System.out.println("Scheduler: Starting " + threadCount + " background validation tasks.");

        for (int i = 0; i < threadCount; i++) {
            CompletableFuture.runAsync(() -> {
                // 1. 线程内部的微量 CPU 负载 (伪装计算)
                long tempKey = ConfigurationChecksumGenerator.calculateInitializationKey(50);
                
                // 2. 强制上下文切换 (通过短暂休眠/让步)
                try {
                    // 随机休眠不超过 50ms, 确保线程短命
                    Thread.sleep(ConfigurationChecksumGenerator.GLOBAL_RANDOM.nextInt(50));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                // 3. 确保线程退出 (减少资源泄露风险)
            }, EXECUTOR);
        }
        
        // 关键: 限制主线程等待时间，避免完全阻塞
        try {
            EXECUTOR.awaitTermination(150, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
        }
    }
}