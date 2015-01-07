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

import com.ddling.mailmanage.Mail;
import com.ddling.usermanage.User;
import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by ddling on 2014/12/24.
 */
public class SMTPThread implements Runnable {
    private static int     SO_TIMEOUT          = 30000;
    private BufferedReader in                  = null;
    private PrintWriter    out                 = null;
    private int            cureent_server_Type = -1;
    private Socket         clientSocket        = null;
    private SMTPCmdList smtpCmdQueue        = null;
    private Mail           mail         = null;
    private User authUser = null;

    public static Logger logger = LoggerFactory.getLogger(SMTPThread.class);

    public SMTPThread(Socket clientSocket, int server_type) {
        this.clientSocket   = clientSocket;
        cureent_server_Type = server_type;
        mail = new Mail();
        authUser = new User();
    }

    /**
     * 输出响应值给客户端
     * @param response 响应字符串
     */
    public void printToClient(String response) {
        out.println(response);
        out.flush();
    }

    public int getCurrentServerType() {
        return cureent_server_Type;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public User getAuthUser() {
        return authUser;
    }

    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }

    /**
     * 关闭当前客户端连接
     */
    public void close() {
        if (clientSocket != null) {
            printToClient("221 Bye");
            try {
                clientSocket.close();
                logger.info("Client " + clientSocket.getInetAddress() + " closed!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientSocket = null;
        }
    }

    /**
     * 主要流程是：
     * 读取客户端的指令，对客户端中的每一条指令，服务器返回对应的响应值。
     */
    public void run() {
        try {
            clientSocket.setSoTimeout(SO_TIMEOUT);
            in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
        } catch (SocketTimeoutException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

        printToClient("220 ddling mail system ");

        smtpCmdQueue = new SMTPCmdList();

        while (!clientSocket.isClosed()) {
            try {
                String cmd = in.readLine();

                if (cmd.toLowerCase().equals("quit")) {
                    close();
                    break;
                }

                if (cmd.toLowerCase().equals("noop")) {
                    // NOOP
                    printToClient("250 OK");
                    continue;
                }

                smtpCmdQueue.process(this, cmd);
                if (clientSocket == null) {
                    break;
                }
            } catch (SocketException e) {
                logger.error(e);
            } catch (IOException e) {
                logger.error(e);
            }
        }

        close();
    }
}
