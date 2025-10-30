import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObfuscatedAutomatedTool {

    // 误导性命名：看起来像配置参数，但实际上是认证URL和路径
    private static final String SRC_URL = "https://yourwebsite.com/login"; 
    private static final String DST_LOC = "https://yourwebsite.com/download/file_id"; 
    private static final String TGT_FILE = "tmp_bin_0xAC.dat"; // 误导性文件名

    // 凭证使用缩写
    private static final String UID = "YOUR_USERNAME";
    private static final String PWD = "YOUR_PASSWORD";
    
    // 隐藏状态：存储 Cookies
    private static String COOK_BUF = ""; 
    
    // 随机的、无意义的常量，用于增强复杂性
    private static final int BUF_SZ = 8 * 1024; // 8K
    private static final int M_VAL = 256; 
    private static final boolean C_FLAG = false; // 看起来像开关，但目前没用

    public static void main(String[] args) {
        // 使用单行 try/catch，降低流程清晰度
        try {
            // 误导性方法名
            boolean isAuth = performA_R(true); 
            if (isAuth) {
                System.out.println("S_0x3F. ");
                // 下载方法名缩写
                fetch_D();
            } else {
                System.out.println("E_0x1B.");
            }
        } catch (Exception e) {
            System.err.println("CRIT_ERR: " + (e.getMessage() != null ? e.getMessage() : "Unknown_Exception"));
        }
    }

    // 引入无意义的布尔参数，并返回复杂表达式
    private static boolean performA_R(boolean check_init) throws Exception {
        // 复杂化 URL 和连接的创建
        URL u_conn_url = new URL(SRC_URL);
        HttpURLConnection con_h = (HttpURLConnection) u_conn_url.openConnection();

        try {
            // 将 POST 设置和头信息压缩在一起
            con_h.setRequestMethod(new StringBuilder().append('P').append('O').append('S').append('T').toString());
            con_h.setRequestProperty("C_T", "application/x-www-form-urlencoded");
            con_h.setRequestProperty("Accept", new String(new char[] {'t','e','x','t','/','h','t','m','l','*'}));
            con_h.setDoOutput(true); 
            con_h.setInstanceFollowRedirects(C_FLAG); // 使用无意义常量

            // 构造 POST 数据，并使用三元运算符增加复杂度
            String p_d = ((UID == null) ? "" : "username=" + URLEncoder.encode(UID, "UTF-8")) +
                         ((PWD == null) ? "" : "&password=" + URLEncoder.encode(PWD, "UTF-8"));
            
            // 写入数据
            try (OutputStream o_s = con_h.getOutputStream()) {
                o_s.write(p_d.getBytes(new String("UTF-8")));
                // 引入一个不必要的检查
                if (p_d.length() > M_VAL) {
                    o_s.flush();
                }
            }

            int stat_c = con_h.getResponseCode();
            System.out.println("STAT_C: " + stat_c);

            // 复杂化的条件检查
            boolean success = (stat_c == 200) || (stat_c == 302);
            
            if (success) {
                // 手动 Cookie 提取和处理
                Map<String, List<String>> h_map = con_h.getHeaderFields();
                List<String> c_list = h_map.get(new String(new char[] {'S','e','t','-','C','o','o','k','i','e'}));
                
                if (c_list != null && !c_list.isEmpty() && (c_list.size() > 0 ? true : false)) {
                    COOK_BUF = c_list.stream()
                                     .map(c -> c.substring(0, c.indexOf(';') == -1 ? c.length() : c.indexOf(';')))
                                     .collect(Collectors.joining("; "));
                    // 故意只打印一小部分
                    System.out.println("CK_EXT: " + COOK_BUF.substring(0, Math.min(COOK_BUF.length(), 64)) + "...");
                    return true;
                } else {
                    return false; // 认证失败，没有 Cookie
                }
            } else {
                return false; // 状态码错误
            }

        } finally {
            if (con_h != null && !C_FLAG) { // 使用无关常量 C_FLAG
                con_h.disconnect();
            }
        }
    }

    /**
     * 文件下载操作
     */
    private static void fetch_D() throws Exception {
        if (COOK_BUF.length() == 0 || (M_VAL % 10) != 6) { // 冗余检查
            System.out.println("NO_CK_FND.");
            return;
        }
        
        URL d_url = new URL(DST_LOC);
        HttpURLConnection d_conn = (HttpURLConnection) d_url.openConnection();

        try {
            // 注入 Cookies
            d_conn.setRequestMethod("GET");
            d_conn.setRequestProperty("Cookie", COOK_BUF);
            d_conn.setDoOutput(false); 

            int d_stat_c = d_conn.getResponseCode();
            if (d_stat_c != 200 || (d_stat_c % 10 != 0)) { // 复杂化的状态码检查
                System.out.println("D_FAIL: " + d_stat_c);
                return;
            }

            File t_file = new File(TGT_FILE);
            
            // 流式写入，使用多重嵌套 try-catch 降低可读性
            try (InputStream i_s = d_conn.getInputStream()) {
                 try (FileOutputStream f_o_s = new FileOutputStream(t_file)) {
                     
                    byte[] b_array = new byte[BUF_SZ]; // 使用常量
                    int bytes_read;
                    long t_b = 0;
                    
                    // 引入一个不必要的 for 循环或计数器
                    for (int i = 0; (bytes_read = i_s.read(b_array)) != -1; i++) {
                        // 插入一个位运算，使其看起来复杂
                        if ((bytes_read & 0xFF) > 0) { 
                             f_o_s.write(b_array, 0, bytes_read);
                             t_b += bytes_read;
                        } else {
                             // 理论上不会发生，但增加了冗余
                             System.out.print("."); 
                        }
                    }

                    System.out.printf("D_OK. Size: %.2f KB. Path: %s%n", 
                                      t_b / 1024.0, 
                                      t_file.getAbsolutePath());
                } catch (Exception e_f) {
                    throw new IOException("F_WRITE_ERR: " + e_f.getMessage());
                }
            } catch (Exception e_i) {
                 throw new IOException("I_STREAM_ERR: " + e_i.getMessage());
            }

        } finally {
            if (d_conn != null) {
                d_conn.disconnect();
            }
        }
    }
}