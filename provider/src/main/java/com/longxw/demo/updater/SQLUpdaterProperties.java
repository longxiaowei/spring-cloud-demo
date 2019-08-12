package com.longxw.demo.updater;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;

@Data
public class SQLUpdaterProperties implements InitializingBean {
    /**
     * 数据源驱动
     */
    private String driverClassName;
    /**
     * 数据源URL
     */
    private String url;
    /**
     * 数据源用户名
     */
    private String username;
    /**
     * 数据源密码
     */
    private String password;

    // 校验必要属性
    @Override
    public void afterPropertiesSet() throws Exception {

        AssertNotEmpty(this.getDriverClassName());
        AssertNotEmpty(this.getUrl());
        AssertNotEmpty(this.getUsername());
        AssertNotEmpty(this.getPassword());
    }

    private boolean isEmpty(String string){
        if( string == null || string.trim().length() == 0){
            return true;
        }else{
            return false;
        }
    }

    private void AssertNotEmpty(String string){
        if(isEmpty(string))
            throw new RuntimeException("updater string can not be empty");
    }
}
