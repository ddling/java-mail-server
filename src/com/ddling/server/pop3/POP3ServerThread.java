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

package com.ddling.server.pop3;

import com.ddling.UserHandler.User;
import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Created by lingdongdong on 15/1/3.
 */
public class POP3ServerThread implements Runnable {

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private static int SO_TIME_OUT = 30000;
    private User authUser = null;
    private ArrayList<Integer> delMailIds = null;

    public static Logger logger = LoggerFactory.getLogger(POP3ServerThread.class);

    public POP3ServerThread(Socket socket) {
        this.socket = socket;
        authUser = new User();
        delMailIds = new ArrayList<Integer>();
    }

    public void setDelMailIds(ArrayList<Integer> delMailIds) {
        this.delMailIds = delMailIds;
    }

    public ArrayList<Integer> getDelMailIds() {
        return delMailIds;
    }

    public User getAuthUser() {
        return authUser;
    }

    public void setAuthUser(User authUser) {
        this.authUser = authUser;
    }

    @Override
    public void run() {
        try {
            socket.setSoTimeout(SO_TIME_OUT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (SocketException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }

        this.printToClient("+OK Welcome to ddling Mail Pop3 server");
        POP3Handler pop3Handler = new POP3Handler();

        while (socket.isConnected()) {
            try {
                String line = in.readLine();
                pop3Handler.handle(this, line);
                if (socket == null) {
                    break;
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }

        this.close();
    }

    /**
     * 输出响应值给客户端
     * @param response 响应字符串
     */
    public void printToClient(String response) {
        out.println(response);
        out.flush();
    }

    public void close() {
        if (socket != null) {
            try {
                socket.close();
                logger.info("Connection " + socket.getInetAddress().toString() + " Closed!");
            } catch (IOException e) {
                logger.error(e);
            }
        }

        socket = null;
    }
}
