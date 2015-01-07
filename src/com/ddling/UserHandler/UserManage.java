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

package com.ddling.UserHandler;

import com.ddling.DBHandler.DBManage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lingdongdong on 14/12/26.
 */
public class UserManage {

    /**
     * 得到数据库中的用户列表，返回用户列表
     * @return  用户列表
     * @throws java.sql.SQLException
     */
    public List<User> getUsers() throws SQLException {

        DBManage dbManage = DBManage.getDbManageInstance();
        List<User> userList = new LinkedList<User>();
        String sql = "SELECT * FROM USER;";
        ResultSet resultSet = dbManage.executeQuery(sql);
        while (resultSet.next()) {
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            User newUser = new User(username, password);
            userList.add(newUser);
        }

        return userList;
    }

    /**
     * 往数据库中添加新用户
     * @param newUser 要添加的新用户
     */
    public void insertNewUser(User newUser) {

        DBManage dbManage = DBManage.getDbManageInstance();

        String username = newUser.getUsername();
        String password = newUser.getPassword();
        long date = new Date().getTime();
        String registerTime = date + "";

        String sql = String.format("INSERT INTO USER VALUES (" +
                                   "NULL, '%s', '%s', '%s')",
                                   username, password, registerTime);

        dbManage.executeUpdate(sql);
    }

    /**
     * 判断数据库中是否有名为username的用户
     * @param username 用户名
     * @return 存在这个用户，返回true，否则返回false
     */
    public boolean hasUser(String username) {

        boolean hasThisUser = false;

        DBManage dbManage = DBManage.getDbManageInstance();

        String sql = String.format("SELECT * FROM USER WHERE username == '%s'", username);

        ResultSet resultSet = dbManage.executeQuery(sql);

        try {
            if (resultSet.next()) {
                hasThisUser = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hasThisUser;
    }

    /**
     * 删除用户名为username的用户
     * @param username 要删除的用户名
     */
    public void deleteUser(String username) {

        DBManage dbManage = DBManage.getDbManageInstance();

        String sql = String.format("DELETE FROM USER WHERE username = '%s'", username);

        dbManage.executeUpdate(sql);

    }

    /**
     * 判断用户名和密码是否验证成功
     * @param username 要验证的用户名
     * @param password 要验证的用户密码
     * @return
     */
    public boolean authUser(String username, String password) {

        boolean authUserOK = false;

        String sql = String.format(
                "SELECT * FROM USER WHERE username = '%s' AND password = '%s'", username, password);

        DBManage dbManage = DBManage.getDbManageInstance();
        ResultSet resultSet = dbManage.executeQuery(sql);

        try {
            if (resultSet.next()) {
                authUserOK = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authUserOK;
    }

    public static void main(String[] args) {
        UserManage userManage = new UserManage();
        userManage.insertNewUser(new User("ddd", "xxx"));
    }
}
