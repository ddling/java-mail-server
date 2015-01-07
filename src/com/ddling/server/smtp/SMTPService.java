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
import com.ddling.MailHandler.MailManage;

import java.util.Queue;

/**
 * Created by lingdongdong on 14/12/30.
 */
public class SMTPService implements Runnable {

    Queue<Mail> waitToSendEmails = null;

    @Override
    public void run() {
        MailManage mailManage = new MailManage();
        waitToSendEmails = mailManage.getForeignEmails();


            Mail mail = waitToSendEmails.element();
        System.out.println(mail.getMail_from());
            SMTPClient smtpClient = new SMTPClient(mail);
            smtpClient.sendEmail();
    }
}
