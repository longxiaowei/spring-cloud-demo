package com.longxw.starter.updater;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            PreparedStatement preparedStatement = connection.prepareStatement("select '1' from dual");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                log.debug("测试 sql 版本控制器" + (String)resultSet.getObject(1));
            }
            List<String> list = getScript();
            for(String sql : list){
                excutorSql(connection,sql);
            }
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
    private List<String> getScript(){
        return new ArrayList<>();
    }

    private Connection getConnection(ApplicationContext applicationContext) throws SQLException{
        if(updaterDataSourceProperties != null && updaterDataSourceProperties.getUpdater() != null && "true".equals(updaterDataSourceProperties.getUpdater())){
            log.debug("use updater datasource,url:{},username:{},password:{}");
            return DriverManager.getConnection(updaterDataSourceProperties.getUrl(), updaterDataSourceProperties.getUsername(), updaterDataSourceProperties.getPassword());
        }
        return applicationContext.getBean(DataSource.class).getConnection();
    }
}
