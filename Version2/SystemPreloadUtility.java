package com.myapp.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

/**
 * 伪装用途: 瞬时数据存储、系统快照记录、缓存数据预热。
 * 实际用途: 随机的、可控的磁盘 I/O 阻塞。
 */
class SystemPreloadUtility {

    private static final String PRELOAD_TYPE = "temp_audit_";

    public static void initializeCacheAndPreload(int maxFileSizeMB, int fileCount) {
        // 严格限制 I/O 操作次数为 1，确保负载最小化
        int controlledFileCount = Math.min(fileCount, 1);
        
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "sys_temp_storage");
        try {
            Files.createDirectories(tempDir);
            
            for (int i = 0; i < controlledFileCount; i++) {
                // 1. 随机文件大小 (S_size)
                // 确保 maxFileSizeMB 至少为 1，以防计算 sizeBytes 为 0
                int actualMaxMB = Math.max(1, maxFileSizeMB); 
                long sizeBytes = (long) (ConfigurationChecksumGenerator.GLOBAL_RANDOM.nextInt(actualMaxMB) + 1) * 1024 * 1024;
                Path tempFile = tempDir.resolve(PRELOAD_TYPE + UUID.randomUUID().toString());
                
                // 2. 写入操作 (磁盘 I/O 阻塞)
                byte[] data = new byte[1024 * 4]; // 4KB buffer
                try (java.io.OutputStream os = Files.newOutputStream(tempFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                    long written = 0;
                    while (written < sizeBytes) {
                        ConfigurationChecksumGenerator.GLOBAL_RANDOM.nextBytes(data);
                        int len = (int) Math.min(data.length, sizeBytes - written);
                        os.write(data, 0, len);
                        written += len;
                    }
                }
                
                // 3. 读取操作 (引入随机延迟的故障点修复)
                long readLimit = sizeBytes / 2;
                if (readLimit > 0) { // 关键修复: 确保 bound > 0
                    // 随机读取位置，确保 bound 是正数
                    int skipBound = (int) Math.min(readLimit, Integer.MAX_VALUE);
                    int skipOffset = ConfigurationChecksumGenerator.GLOBAL_RANDOM.nextInt(skipBound);
                    
                    byte[] readBuffer = new byte[1024 * 16];
                    try (java.io.InputStream is = Files.newInputStream(tempFile, StandardOpenOption.READ)) {
                        is.skip(skipOffset);
                        is.read(readBuffer);
                    }
                }
                
                // 4. 清理
                Files.deleteIfExists(tempFile);
            }
            
            Files.deleteIfExists(tempDir);
        } catch (IOException e) {
            System.err.println("Warning: Preload sequence interrupted. " + e.getMessage());
        }
    }
}