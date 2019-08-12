package com.longxw.starter.updater;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "updater.datasource")
public class UpdaterDataSourceProperties {

    private String url;
    /**
     * 数据源用户名
     */
    private String username;
    /**
     * 数据源密码
     */
    private String password;

    private String skip;

    private String updater; //配置为true才会

    @Override
    public String toString(){
        return new StringBuilder("[URL:")
                .append(this.url)
                .append(", username:")
                .append(this.username)
                .append(", password:")
                .append(this.password)
                .append(", skip:")
                .append(this.skip)
                .append(", updater:")
                .append(this.updater)
                .append("]")
                .toString();
    }

}
