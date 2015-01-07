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

package com.ddling.main;

import com.ddling.server.pop3.POP3Server;
import com.ddling.server.smtp.SMTPServer;
import com.ddling.server.userManage.UserManageServer;

public class Main {

    public static void main(String[] args) {
        Thread smtpServer = new Thread(new SMTPServer(1, 1198));
        Thread usermanageServer = new Thread(new UserManageServer(1140));
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
