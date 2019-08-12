package com.longxw.demo.updater;

import com.longxw.demo.common.CloseUtil;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ScriptUpdater {

    private String scriptUrl = "classpath:script/sql";

    private String queryVersion = "SELECT VERSION FROM script_control_updater";

    private String initUpdater;

    private DataSource dataSource;

    private Connection connection;

    public ScriptUpdater(String initUpdater,DataSource dataSource){
        this.initUpdater = initUpdater;
        this.dataSource = dataSource;
    }

    public void initVersion() throws Exception{
        String initVersionSql = "";
        this.dataSource.getConnection().prepareStatement(initVersionSql).executeUpdate();
    }

    public void updateScript(){
        try {
            initVersion();
            doUpdateScript();
            updateVersion();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(this.connection !=null ){
                CloseUtil.closeConnection(connection);
            }
        }
    }

    public void updateVersion(){

    }

    public int compareVersion(String source,String target){
        return 0;
    }

    public List<File> findVersion(File file,String version){
        File[] fileArr = file.listFiles();
        if(fileArr == null){
            return null;
        }
        List<File> sqlDirs = Arrays.stream(fileArr)
                .filter(dir -> compareVersion(dir.getName(), version) <= 0 )
                // 排序
                .sorted((dir1, dir2) -> compareVersion(dir1.getName(), dir2.getName()))
                .collect(Collectors.toList());
        return sqlDirs;
    }

    public void doUpdateScript() throws Exception{
        URL url = ResourceUtils.getURL(scriptUrl);
        try{
            File sqlDir;
            if (ResourceUtils.isJarURL(url)){

            }else{
                sqlDir = new File(url.getPath()).getCanonicalFile();
                String version = "";
                ResultSet resultSet = this.dataSource.getConnection().prepareStatement(this.queryVersion).executeQuery();
                if (resultSet.next() && resultSet.getMetaData().getColumnCount() > 0){
                    version = (String) resultSet.getObject(1);
                }

                List<File> list = findVersion(sqlDir,version);

                if( !list.isEmpty() ){
                    list.forEach( dir -> {
                        File[] sqlFiles = dir.listFiles(file -> getFileType(file).equals("sql"));//查找sql后缀的文件
                        if( sqlFiles != null && sqlFiles.length > 0 ){
                            Arrays.sort(sqlFiles, Comparator.comparing(File::getName));
                            list.forEach( file -> {
                                List<String> sqls = Arrays.stream(readText(file).split(";"))
                                        .map(String::trim)
                                        .filter(sql -> sql != null && !"".equals(sql))
                                        .collect(Collectors.toList());
                                sqls.forEach( sql -> {
                                    try {
                                        this.update(sql);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                });
                            });
                        }
                    });
                }
            }
        }catch (Exception e){

        }
    }

    private String getFileType(File file){
        int index = file.getName().lastIndexOf(".");
        if(index > 0){
            return file.getName().substring(index+1);
        }else{
            return "";
        }
    }

    private String readText(File file) {
        try {
            return new String(readBytes(file), Charset.forName("UTF-8"));
        }catch (Exception e){
            return "";
        }
    }

    private byte[] readBytes(File file) throws IOException {
        InputStream input = new FileInputStream(file);
        int offset = 0;

        int remaining = new Long(file.length()).intValue();

        byte[] result = new byte[remaining];
        while (remaining > 0){
            int read = input.read(result, offset, remaining);
            if (read < 0) break;
            remaining -= read;
            offset += read;
        }

        if (remaining == 0) return result;
        else {
            return Arrays.copyOf(result, offset);
        }
    }

    public int update(String sql) throws SQLException{
        return this.connection.prepareStatement(sql).executeUpdate();
    }

}
