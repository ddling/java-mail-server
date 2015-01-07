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

package com.ddling.server.smtp;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.ddling.utils.Constants;
import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

/**
 * Created by ddling on 2014/12/24.
 */
public class SMTPServer implements Runnable {

    private int          port                   = 25;
    private ServerSocket serverSocket           = null;
    private Executor     service                = null;
    private int          cureent_server_Type    = -1;

    public static Logger logger = LoggerFactory.getLogger(SMTPServer.class);

    /**
     * 默认构造函数
     * @param server_type 服务器类型
     * @param port 监听的端口号
     */
    public SMTPServer(int server_type, int port) {
        cureent_server_Type = server_type;
        this.port           = port;
    }

    public int getCurrentServerType() {
        return cureent_server_Type;
    }

    /**
     * 在指定端口打开Smtp服务器并等待客户端连接
     */
    private void startSmtpServer() {
        try {
            serverSocket = new ServerSocket(port);
            service      = Executors.newCachedThreadPool();
            logger.info("SMTP server start at port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Client " + clientSocket.getInetAddress() + " Connected to SMTP server");
                service.execute(new SMTPThread(clientSocket, cureent_server_Type));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.startSmtpServer();
        this.stop();
    }

    /**
     * 关闭Smtp服务器
     */
    private void stop() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                logger.info("SMTP server stop at port " + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new SMTPServer(Constants.SERVER_TYPE_FOR_CLIENT, 1198).run();
    }
}
