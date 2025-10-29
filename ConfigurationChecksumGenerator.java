import java.util.Random;
import java.time.Instant;

/**
 * 伪装用途: 系统初始化配置、校验和计算、或安全相关的随机数生成。
 * 实际用途: 负载参数的随机生成。
 */
class ConfigurationChecksumGenerator {

    static final Random GLOBAL_RANDOM = new Random(Instant.now().getEpochSecond());
    private static final int BASE_SEED = 1234567;

    // 伪装方法: 计算一个"安全阈值"或"初始化密钥"
    public static long calculateInitializationKey(int complexityFactor) {
        long key = BASE_SEED;
        for (int i = 0; i < complexityFactor; i++) {
            // 引入 CPU 负载: 无意义的位运算和乘法
            key = (key * 31 + GLOBAL_RANDOM.nextInt(997)) ^ (key >> 5);
        }
        // 返回一个看似有意义的值，但其随机性和计算量已经消耗了CPU时间
        return key;
    }

    // 伪装方法: 根据"密钥"计算出"缓存大小" (MB)
    public static int getRequiredCacheSizeMB(long initializationKey) {
        // 使用 KEY 的 LSBs 引入随机性和不确定性
        int base = (int) (initializationKey % 4) + 1; // 1-4 MB base
        if (GLOBAL_RANDOM.nextBoolean()) {
            return base * 2 + GLOBAL_RANDOM.nextInt(3); // 2-11 MB
        }
        return base; // 1-4 MB
    }

    // 伪装方法: 根据"缓存"和"密钥"计算出"预热线程数" (T_num)
    public static int getPreloadThreadCount(int cacheSizeMB) {
        // T_num = (CoreCount * 2) + 随机偏移量
        int coreCount = Runtime.getRuntime().availableProcessors();
        return (coreCount * 2) + GLOBAL_RANDOM.nextInt(coreCount * 2) + 1;
    }
}