package com.hoolai.choosewife;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigServlet extends HttpServlet {

    private static Map<String, String> map = new HashMap<>();

    @PostConstruct
    public void loadIn() {
        initCfg();
        System.out.println("已加载baseConfig.properties!");
    }

    @PreDestroy
    public void close() {
        System.out.println("已销毁！");
        map.clear();
        map = null;
    }

    private void initCfg() {
        InputStream inputStream = ConfigServlet.class
                .getResourceAsStream("/baseConfig.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            map.put("filePath", properties.getProperty("filePath"));
            map.put("userName", properties.getProperty("userName"));
            map.put("password", properties.getProperty("password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return map.get(key);
    }

}
