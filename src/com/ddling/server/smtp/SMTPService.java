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
