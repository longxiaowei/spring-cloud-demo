package com.longxw.demo.common;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class CloseUtil {
    public static boolean closeInputStream(InputStream inputStream){
        if(inputStream == null){
            return true;
        }
        try {
            inputStream.close();
            return true;
        }catch (IOException e){
            return false;
        }
    }

    public static boolean closeConnection(Connection connection){
        if(connection == null){
            return true;
        }
        try {
            connection.close();
            return true;
        }catch (SQLException e){
            return false;
        }
    }

}
