package com.hy.myfile.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by hy on 2019/10/16 11:48
 * @author hy
 */
@Component
@ConfigurationProperties(prefix = "file.server")
@Getter
@Setter
@ToString
public class WebConfig {

    /**
     * 文件根目录
     */
    private static String basePath;

    private static String baseHost;

    public static String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        WebConfig.basePath = basePath;
    }

    public static String getBaseHost() {
        return baseHost;
    }

    public void setBaseHost(String baseHost) {
        WebConfig.baseHost = baseHost;
    }
}
