package com.myapp.util;

import java.util.Random;
import java.time.Instant;

/**
 * 伪装用途: 系统初始化配置、校验和计算。
 * 实际用途: CPU负载生成和安全参数控制。
 */
class ConfigurationChecksumGenerator {

    // 使用系统时间作为种子，确保每次运行的参数具有随机性
    public static final Random GLOBAL_RANDOM = new Random(Instant.now().getEpochSecond());
    private static final int BASE_SEED = 1234567;
    
    // CPU 负载方法: 确保 ComplexityFactor 调整至安全值 (75)
    public static long calculateInitializationKey(int complexityFactor) {
        long key = BASE_SEED;
        // 确保 complexityFactor 至少为 1
        int actualComplexity = Math.max(1, complexityFactor); 
        
        for (int i = 0; i < actualComplexity; i++) {
            // 引入 CPU 负载: 无意义的位运算和乘法
            key = (key * 31 + GLOBAL_RANDOM.nextInt(997)) ^ (key >> 5);
        }
        return key;
    }

    // I/O 大小参数生成方法: 确保返回参数 > 0，且最大值被严格限制在 3MB
    public static int getRequiredCacheSizeMB(long initializationKey) {
        // 保证 base 最小值为 1 MB
        int base = (int) (initializationKey % 2) + 1; // 1-2 MB base
        
        // 限制随机上限：确保文件大小不超过 3MB
        if (GLOBAL_RANDOM.nextBoolean()) {
            // nextInt(1) 确保 bound > 0，结果为 0。所以结果是 base + 0
            // 结果范围: 1-2 MB
            return base; 
        }
        return base; // 结果范围: 1-2 MB
    }

    public static int getPreloadThreadCount(int cacheSizeMB) {
        // T_num = 1 + 随机偏移量 (保持语义，但实际值在 BackgroundScheduler 中被严格控制)
        return 1 + GLOBAL_RANDOM.nextInt(10); 
    }
}