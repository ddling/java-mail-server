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

import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by lingdongdong on 15/1/3.
 */
public class POP3Server implements Runnable {

    private ServerSocket serverSocket = null;
    private Executor service = null;
    private int port = 1120;

    public static Logger logger = LoggerFactory.getLogger(POP3Server.class);

    public POP3Server(int port) {
        this.port = port;

        try {
            serverSocket = new ServerSocket(port);
            service = Executors.newCachedThreadPool();

            logger.info("POP3 Server run at port " + port);


        } catch (IOException e) {
            logger.error("Something Wrong with the pop3 server");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();

                logger.info("A client connect at " + socket.getInetAddress().toString() + " port " + socket.getPort());

                service.execute(new POP3ServerThread(socket));
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    private void stop() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public static void main(String[] args) {
        new POP3Server(1120).run();
    }
}
