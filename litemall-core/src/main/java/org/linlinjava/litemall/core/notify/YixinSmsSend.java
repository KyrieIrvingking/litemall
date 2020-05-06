package org.linlinjava.litemall.core.notify;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.config.YixinSmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class YixinSmsSend {

    @Autowired
    private YixinSmsConfig yixinSmsConfig;
    private final Log logger = LogFactory.getLog(YixinSmsSend.class);

    public String SMS(String mobile,String smg) {
        try {
            String postData = "sname="+yixinSmsConfig.getSname()+"&spwd="+yixinSmsConfig.getSpwd()
                    +"&scorpid=&sprdid="+yixinSmsConfig.getSprdid()+"&sdst="+mobile
                    +"&smsg="+java.net.URLEncoder.encode(yixinSmsConfig.getSmsg()+smg,"utf-8");
            logger.info("短信："+postData);
            //发送POST请求
            URL url = new URL(yixinSmsConfig.getAddress());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();

            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("connect failed!");
                return "";
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return "";
    }
}
