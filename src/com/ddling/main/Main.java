package com.ddling.main;

import com.ddling.server.pop3.POP3Server;
import com.ddling.server.smtp.SMTPServer;
import com.ddling.UserHandler.UserManageServer;

public class Main {

    public static void main(String[] args) {
        Thread smtpServer = new Thread(new SMTPServer(1, 1198));
        Thread usermanageServer = new Thread(new UserManageServer(1138));
        Thread pop3Server = new Thread(new POP3Server(1120));

        smtpServer.start();
        usermanageServer.start();
        pop3Server.start();

        try {
            smtpServer.join();
            usermanageServer.join();
            pop3Server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
