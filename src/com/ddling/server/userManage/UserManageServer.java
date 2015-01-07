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

package com.ddling.server.userManage;

import com.ddling.utils.Constants;
import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by lingdongdong on 15/1/4.
 * 用户管理服务器，用于判断用户是否存在，用户验证以及新用户的注册。
 */
public class UserManageServer implements Runnable {

    private ServerSocket serverSocket = null;
    private int port                  = Constants.USER_MANAGE_PORT;
    private Executor service          = null;

    public static Logger logger = LoggerFactory.getLogger(UserManageServer.class);

    public UserManageServer() {
        service = Executors.newCachedThreadPool();
    }

    public UserManageServer(int port) {
        this.port       = port;
        service         = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("User Manage Server start at port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("A client connect at " +
                        clientSocket.getInetAddress().toString()
                        + " port " + clientSocket.getPort());
                service.execute(new UserManageThread(clientSocket));
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
