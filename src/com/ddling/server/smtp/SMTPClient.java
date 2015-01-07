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

import com.ddling.MailHandler.Mail;
import com.ddling.server.smtp.mx.MXExchange;
import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class SMTPClient {

    // 得到log实例
    public Logger logger = LoggerFactory.getLogger(SMTPClient.class);

    // 与服务器相连的Socket
    private Socket socket = null;
    // 从socket里面读取数据
    private BufferedReader in = null;
    // 往socket里面写入数据
    private PrintWriter out = null;
    // 得到合适的服务器地址
    private String server = null;
    // 与服务器地址相对应的端口号
    private int port;
    // 一条邮件信息，包含邮件头以及邮件正文
    private Mail mail = null;

    private static int SO_TIME_OUT = 30000;

    public SMTPClient() {
        logger.info("Start a smtp client");
    }

    public SMTPClient(Mail mail) {
        this.mail = mail;
        initilizeTheServer();
    }

    /**
     * 初始化邮件服务器信息，包括得到邮件地址以及对应的端口号
     */
    private void initilizeTheServer() {
        int serverPos = mail.getMail_to().indexOf("@");
        server = mail.getMail_to().substring(serverPos + 1);

        if (server.equals("gmail.com")) {
            port = 465;
        } else {
            port = 25;
        }

        try {
            server = MXExchange.getMxServer(server);
        } catch (Exception e) {
            logger.error(e);
            server = null;
        }
    }

    /**
     * 发送邮件
     * @return 发送成功返回true， 发送失败返回false
     */
    public boolean sendEmail() {
        boolean sendEmailOK = true;

        try {
            initClient();
            ehlo();
            sendEmailHeader();
            sendEmailContent();
            quit();
        } catch (Exception e) {
            logger.error(e);
            sendEmailOK = false;
        } finally {
            this.closeTheClient();
        }

        return sendEmailOK;
    }

    /**
     * 向邮件服务器请求退出
     * @throws java.io.IOException
     */
    private void quit() throws IOException {
        sendData("QUIT");

        int response = getResponse();

        if (response != 221) {
            throw new IOException("Quit fail");
        }

        logger.info("Quit Done!");
    }

    /**
     * 发送邮件正文
     * @throws java.io.IOException
     */
    private void sendEmailContent() throws IOException{

        sendData("DATA");

        int response = getResponse();

        System.out.println("Content" + response);

        if (response != 354) {
            throw new IOException("Send Email content fail");
        }

        sendData("Hello");
        sendData(".");

        response = getResponse();
        if (response != 250) {
            throw new IOException("Send Email Content fail");
        }

        logger.info("Send Email Content done!");
    }

    /**
     * 发送邮件头，即(mail from, rcpt to以及subject)
     * @throws java.io.IOException
     */
    private void sendEmailHeader() throws IOException{

        sendData("MAIL FROM:<" + mail.getMail_from() + ">");

        int response = getResponse();

        if (response != 250) {
            throw new IOException("Send Email Header fail!");
        }

        sendData("RCPT TO:<" + mail.getMail_to() + ">");

        response = getResponse();

        if (response != 250) {
            throw new IOException("Send Email Header fail!");
        }

        logger.info("Send Email Header done!");
    }

    /**
     * 向邮件服务器发送helo指令
     * @throws java.io.IOException
     */
    private void ehlo() throws IOException{
        sendData("helo " + server);

        System.out.println("helo");

        int response = getResponse();

        System.out.println(response);

        if (response != 250) {
            throw new IOException("ehlo fail!");
        }

        logger.info("ehlo done!");
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
     * 初始化smtp客户端
     */
    private void initClient() {

        try {
            socket = new Socket(server, port);
            socket.setSoTimeout(SO_TIME_OUT);

            logger.info("Connect to " + server + " At port " + port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            int response = getResponse();

            System.out.println(response);

            if (response == 220) {
                logger.info("Connect to " + server + " done!");
            }

        } catch (SocketTimeoutException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**
     * 得到服务器的响应信息
     * @return
     */
    private int getResponse() {

        String line = "";
        try {
            line = in.readLine();
        } catch (IOException e) {
            logger.error(e);
        }

        logger.debug("Response " + line);
        StringTokenizer get = new StringTokenizer(line, " ");
        return Integer.parseInt(get.nextToken());
    }

    /**
     * 关闭smtp客户端
     */
    public void closeTheClient() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public static void main(String[] args) {
        Mail mail = new Mail();
        mail.setMail_from("jtdx_159020@sohu.com");
        mail.setMail_to("465391062@qq.com");
        mail.setContent("Test");
        SMTPClient smtpClient = new SMTPClient(mail);
        smtpClient.sendEmail();
    }
}
