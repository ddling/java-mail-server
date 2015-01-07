package com.ddling.server.smtp;

import com.ddling.mailmanage.Mail;
import com.ddling.mailmanage.MailManage;
import com.ddling.utils.Constants;

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
