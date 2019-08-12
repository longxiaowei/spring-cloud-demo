package com.longxw.starter.updater.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbTool {

    private Connection connection;

    public DbTool(Connection connection){
        this.connection = connection;
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        statement.close();
        return resultSet;
    }

    public ResultSet executeQuery(String sql,Object... args) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(sql);
        this.bindArgs(statement, args);
        ResultSet resultSet = statement.executeQuery();
        statement.close();
        return resultSet;
    }

    public void executeUpdate(String sql) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.executeUpdate();
        statement.close();
    }

    public void executeUpdate(String sql, Object... args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        this.bindArgs(statement, args);
        statement.executeUpdate();
        statement.close();
    }

    private void bindArgs(PreparedStatement statement, Object[] args) throws SQLException {
        if (args != null && args.length > 0){
            for (int i = 0, length = args.length; i < length; i ++){
                statement.setObject(i + 1, args[i]);
            }
        }
    }

}
