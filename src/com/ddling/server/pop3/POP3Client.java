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

import com.ddling.MailHandler.Mail;
import com.ddling.UserHandler.User;
import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by lingdongdong on 15/1/4.
 */
public class POP3Client {

    private String hostAddr = null;
    private int port = 110;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private User authUser = null;
    private static int SO_TIME_OUT = 30000;
    private static String RESPONSE_OK = "+OK";

    private Queue<Mail> mailQueue = null;

    public static Logger logger = LoggerFactory.getLogger(POP3Client.class);

    public POP3Client(String hostAddr, User user) {
        this.authUser = user;
        this.hostAddr = hostAddr;
        mailQueue = new LinkedList<Mail>();
        initializeThePOP3Client();
    }

    public POP3Client(String hostAddr, int port, User user) {
        this.authUser = user;
        this.hostAddr = hostAddr;
        this.port = port;
        initializeThePOP3Client();
    }

    private void initializeThePOP3Client() {
        try {
            socket = new Socket("pop3." + hostAddr, port);
            socket.setSoTimeout(SO_TIME_OUT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            authLogin();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void authLogin() throws IOException{
        String data = "user " + authUser.getUsername() + "@" + hostAddr;
        sendData(data);

        String response = getCommand(getResponse());

        if (!response.equalsIgnoreCase(RESPONSE_OK)) {
            throw new IOException("Response fail!");
        }

        data = "pass " + authUser.getPassword();
        sendData(data);

        response = getCommand(getResponse());

        if (!response.equalsIgnoreCase(RESPONSE_OK)) {
            throw new IOException("Can not log on");
        }
    }

    public void POP3State() throws IOException {
        String data = "stat";

        sendData(data);

        String response = getCommand(getResponse());

        if (!response.equalsIgnoreCase(RESPONSE_OK)) {
            throw new IOException("Stat fail");
        }
    }

    public void POP3List() throws IOException {
        String data = "list";

        sendData(data);

        String response = getCommand(getResponse());

        if (!response.equalsIgnoreCase(RESPONSE_OK)) {
            throw new IOException("List fail");
        }
    }

    public void POP3Retr(int mail_id) throws IOException {
        String data = "retr " + mail_id;

        sendData(data);

        System.out.println(data);

        String response = getCommand(getResponse());

        if (!response.equalsIgnoreCase(RESPONSE_OK)) {
            throw new IOException("Retr fail");
        }
    }

    /**
     * 发送字符串给邮件服务器
     * @param s 要发送的字符串
     */
    private void sendData(String s) {
        out.println(s);
        out.flush();
    }

    /**
     * 得到服务器的响应信息
     * @return
     */
    private String getResponse() {

        String line = "";
        try {
            line = in.readLine();
        } catch (IOException e) {
            logger.error(e);
        }

        logger.debug(line);
        return line;
    }

    private String getCommand(String str) {
        String cmd = str.toLowerCase().split(" ")[0].toUpperCase();
        return cmd;
    }

    public static void main(String[] args) {
        User user = new User("sysuyezhiqi", "wgxldd1992");
        POP3Client pop3Client = new POP3Client("163.com", user);
        try {
            pop3Client.POP3Retr(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
