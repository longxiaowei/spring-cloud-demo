package com.longxw.starter.updater;

import com.longxw.starter.updater.tool.DbTool;
import com.longxw.starter.updater.tool.FileTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class UpdaterListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    UpdaterDataSourceProperties updaterDataSourceProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        doUpdater(applicationContext);
    }

    private void doUpdater(ApplicationContext applicationContext){
        Connection connection = null;
        try {
            connection = getConnection(applicationContext);
            DbTool dbTool = new DbTool(connection);
            String version = (String)dbTool.executeQuery("select v_updater from updater_version").getObject(1);
            Map<String,String> map = getScript(version);
            String lastVersion = null;
            for(String key : map.keySet()){
                String[] sqls = map.get(key).split(";");
                for(String sql : sqls){
                    try {
                        dbTool.executeUpdate(sql);
                        lastVersion = key;
                    }catch (Exception e){

                    }

                }
            }
            if(lastVersion == null){
                return;
            }
            dbTool.executeUpdate("update updater_version set version = ?",new String[]{lastVersion});//更新版本信息
        }catch (SQLException e){

        }finally {
            if(connection != null){
                try {
                    connection.close();
                }catch (SQLException e){

                }

            }
        }
    }

    /**执行 sql
     * @author longxw
     * @since 2019-8-12
     */
    private void excutorSql(Connection connection,String sql) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeQuery();
        preparedStatement.close();
    }

    /** 获取符合条件的 sql
     * @author longxw
     * @since 2019-8-12
     */
    private Map<String,String> getScript(String version) {
        try {
            URL url = ResourceUtils.getURL("classpath:script/sql");
            File sqlDir = new File(url.getPath()).getCanonicalFile();
            File[] files = sqlDir.listFiles();
            List<File> fileList = Arrays.stream(files)
                    .filter(file -> compareVersion(file.getName(),version)>0 )
                    .collect(Collectors.toList());
            Map<String,String> map = new TreeMap();
            fileList.forEach( file -> {
                map.put(file.getName(),FileTool.readText(file));
            });
            return map;
        }catch (Exception e){
            e.printStackTrace();
            return new TreeMap();
        }
    }

    private Connection getConnection(ApplicationContext applicationContext) throws SQLException{
        if(updaterDataSourceProperties != null && updaterDataSourceProperties.getUpdater() != null && "true".equals(updaterDataSourceProperties.getUpdater())){
            log.debug("use updater datasource,url:{},username:{},password:{}");
            return DriverManager.getConnection(updaterDataSourceProperties.getUrl(), updaterDataSourceProperties.getUsername(), updaterDataSourceProperties.getPassword());
        }
        return applicationContext.getBean(DataSource.class).getConnection();
    }

    private int compareVersion(String t1,String t2){
        int result = t1.compareTo(t2);
        log.debug("{} compare to {} is {}", t1, t2, result);
        return result;
    }
}
