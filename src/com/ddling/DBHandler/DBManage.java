/*
 * Copyright (C) 2014 lingdongdong
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ddling.DBHandler;

import com.ddling.utils.Constants;
import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lingdongdong on 14/12/26.
 */
public class DBManage {

    public Logger logger = LoggerFactory.getLogger(DBManage.class);

    private volatile static DBManage dbManageInstance = null;
    private volatile Connection connection = null;

    /**
     * 双重检查加锁，防止多个用户同时修改数据库文件出错
     * 单件模式
     * @return 返回DBManage的实例
     */
    public static DBManage getDbManageInstance() {

        if (dbManageInstance == null) {
            synchronized (DBManage.class) {
                if (dbManageInstance == null) {
                    dbManageInstance = new DBManage();
                }
            }
        }

        return dbManageInstance;
    }

    /**
     * 构造函数，初始化数据库连接
     */
    public DBManage() {

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + Constants.DB_FILE_NAME);
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            logger.error("org.sqlite.JDBC class Not Found!");
        } catch (SQLException e) {
            logger.error("sqlite connection fail!");
        }

        logger.info("open db successfully!");
    }

    /**
     * 执行数据库查询
     * @param sql 执行查询的sql语句
     * @return  返回一个结果集
     */
    public ResultSet executeQuery(String sql) {

        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

        } catch (SQLException e) {
            logger.error("Create Query Statement fail!");
        }

        return resultSet;
    }

    /**
     * 执行数据库的Update操作
     * @param sql 执行数据库Update的sql语句
     */
    public void executeUpdate(String sql) {

        synchronized (DBManage.class) {

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);

                connection.commit();

            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Create Update Statement sql fail!");
            }

            this.close();
        }

    }

    /**
     * 关闭数据库连接
     */
    public void close() {

        try {
            if (connection != null && !connection.isClosed()) {

                dbManageInstance = null;
                connection.close();
                connection = null;

                logger.info("Close the Sqlite connection Successfully!");
            }
        } catch (SQLException e) {
            logger.error(e);
        }
    }
}
