package org.linlinjava.litemall.core.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.cache.interceptor.KeyGenerator;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class CacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        // TODO Auto-generated method stub
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("target", target.getClass().toGenericString());//放入target的名字
        map.put("method", method.getName());//放入method的名字
        if (params != null && params.length > 0) {//把所有参数放进去
            int i = 0;
            for (Object o : params) {
                map.put("params-" + i, o);
                i++;
            }
        }
        String str = JSONObject.toJSON(map).toString();
        byte[] hash = null;
        String s = null;
        try {
            hash = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        s=MD5Encoder.encode(hash);//使用MD5生成位移key
        return s;
    }

}
