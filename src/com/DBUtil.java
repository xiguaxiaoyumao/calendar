package com;
/**
 * 1.这个类实现了对数据库操作的所有方法
 * 2.可以通过这个类对数据库实现增删改查等操作
 * 3.这个类是通过java反射来实现的
 * 4.不定参数的使用
 *
 * @date 2020/7/14 15:35
 */

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DBUtil {

    //1.实例化连接池
    private static Vector<Connection> connectionPool = new Vector<Connection>();

    //2.初始化连接池    往连接池中添加连接对象
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            for (int i = 0; i < 2; i++) {
                Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/calendar", "root", "root");
                connectionPool.add(connection);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //3.取连接
    public static Connection getConnection() {
        Connection connection = connectionPool.get(0);
        connectionPool.remove(0);
        return connection;
    }

    //4.还连接
    public static void releaseConnection(Connection connection) {
        connectionPool.add(connection);
    }

    //增删改
    public static int addDeleteUpdate(String sql, Object... p) {
        Connection connection = getConnection();
        int n = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (p != null) {
                for (int i = 0; i < p.length; i++) {
                    ps.setObject(i + 1, p[i]);
                }
            }
            try {
                n = ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(connection);
        }
        return n;
    }

    //查询
    public static List query(String sql, Class c, Object... p) {
        Connection connection = getConnection();
        ResultSet rs = null;
        List<Object> list = new ArrayList<Object>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (p != null) {
                for (int i = 0; i < p.length; i++) {
                    ps.setObject(i + 1, p[i]);
                }
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            while (rs.next()) {
                Object object = c.newInstance();
                for (int i = 1; i <= count; i++) {
                    String fieldName = rsmd.getColumnLabel(i);
                    Field field = c.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(object, rs.getObject(i));
                }
                list.add(object);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(connection);
        }
        return list;
    }

    //聚合查询
    public static double uniqueQuery(String sql, Object... p) {
        Connection connection = getConnection();
        double d = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            if (p != null) {
                for (int i = 0; i < p.length; i++) {
                    ps.setObject(i + 1, p[i]);
                }
            }
            ResultSet rs = ps.executeQuery();
            rs.next();
            d = rs.getDouble(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            releaseConnection(connection);
        }
        return d;
    }
}
