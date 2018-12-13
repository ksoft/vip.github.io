package com.jlt.common.util;

import com.jlt.common.constant.APIConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * http连接工具
 *
 * @author Dick
 */
public class HttpTool {

    private static Log logger = LogFactory.getLog(HttpTool.class);

    private HttpTool() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * 通过get获取http信息
     *
     * @param getUrl 访问的路径
     * @return
     */
    public static String httpGet(String getUrl) {
        BufferedReader reader = null;
        StringBuilder lines = new StringBuilder();
        try {
            URL url = new URL(getUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStreamReader in = new InputStreamReader(connection.getInputStream(), APIConstant.UTF_8);
            reader = new BufferedReader(in);
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                lines.append(tempStr);
            }
            reader.close();
            in.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return lines.toString();
    }


    /**
     * 通过post获取http信息
     *
     * @param postUrl 访问路径
     * @param info    post信息
     * @return
     */
    public static String httpPost(String postUrl, String info) {
        BufferedReader reader = null;
        StringBuilder lines = new StringBuilder();
        try {
            URL url = new URL(postUrl);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty(APIConstant.CONTENT_LENGTH, String.valueOf(info.getBytes().length));
            connection.setUseCaches(false);
            connection.getOutputStream().write(info.getBytes(APIConstant.UTF_8));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), APIConstant.UTF_8));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                lines.append(tempStr);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return lines.toString();
    }

    /**
     * 通过post获取http信息
     *
     * @param postUrl 访问路径
     * @param info    post信息
     * @return
     */
    public static String httpPostJson(String postUrl, String info) {
        BufferedReader reader = null;
        StringBuilder lines = new StringBuilder();
        try {
            URL url = new URL(postUrl);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(20 * 1000);
            connection.setReadTimeout(20 * 1000);
            connection.setRequestProperty(APIConstant.CONTENT_LENGTH, String.valueOf(info.getBytes().length));
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.setUseCaches(false);
            connection.getOutputStream().write(info.getBytes(APIConstant.UTF_8));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), APIConstant.UTF_8));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                lines.append(tempStr);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return lines.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！{}", e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.error("发送 POST 请求出现异常！{}", ex);
            }
        }
        return sb.toString();
    }
}
