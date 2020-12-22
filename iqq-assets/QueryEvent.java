package me.iqq.server.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Deprecated
public class QueryEvent {
    private Connection connection = null;
    private PreparedStatement pStatement = null;
    private ResultSet resultSet = null;
    private boolean autoCommit = true;

    public QueryEvent(Connection connection) {
        assert (connection != null);
        this.connection = connection;
    }

    public QueryEvent prepare(String sql) throws SQLException {
        assert (connection != null && !connection.isClosed());
        pStatement = connection.prepareStatement(sql);
        return this;
    }

    public QueryEvent setParams(Object... params) throws SQLException {
        assert (pStatement != null && !pStatement.isClosed());
        if (params != null) {
            for (int i = 0; i < params.length; ++i)
                pStatement.setObject(i + 1, params[i]);
        }
        return this;
    }

    /**
     * 查询
     *
     * @return result set
     */
    public ResultSet executeQuery() throws SQLException {
        assert (pStatement != null && !pStatement.isClosed());
        resultSet = pStatement.executeQuery();
        return resultSet;
    }

    /**
     * 更新
     */
    public int executeUpdate() throws SQLException {
        assert (pStatement != null && !pStatement.isClosed());
        return pStatement.executeUpdate();
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
        connection.setAutoCommit(autoCommit);
    }

    public void rollback() throws SQLException {
        if (!autoCommit)
            connection.rollback();
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void close() {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (SQLException sqlE) {
            System.out.println("结果集关闭异常: " + sqlE.getMessage());
            sqlE.printStackTrace();
        }
        try {
            if (pStatement != null && !pStatement.isClosed())
                pStatement.close();
        } catch (SQLException e) {
            System.out.println("更新集关闭异常：" + e.getMessage());
        }
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            System.out.println("数据库连接关闭异常：" + e.getMessage());
        }

    }

}
