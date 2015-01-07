package com.ddling.server.pop3.state;

import com.ddling.MailHandler.MailManage;
import com.ddling.server.pop3.POP3ServerThread;

import java.util.ArrayList;

/**
 * Created by lingdongdong on 15/1/3.
 */
public class POP3Operater extends State {

    @Override
    public boolean process(POP3ServerThread pop3ServerThread, String str) {

        String cmd = getArg0(str).toLowerCase();

        MailManage manage = new MailManage();

        if (!isValidCommand(cmd)) {
            pop3ServerThread.printToClient("-ERR Unknown command " + cmd);
            return false;
        }

        if (cmd.equalsIgnoreCase("stat")) {
            String stat = manage.handlePop3Stat(pop3ServerThread.getAuthUser().getUsername());
            pop3ServerThread.printToClient("+OK " + stat);
            return true;
        }

        if (cmd.equalsIgnoreCase("list")) {
            String list = manage.handlePop3List(pop3ServerThread.getAuthUser().getUsername(), MailManage.MAIL_ROLE.RECEIVER_EMAIL);
            pop3ServerThread.printToClient("+OK");
            pop3ServerThread.printToClient(list);
            pop3ServerThread.printToClient(".");
            return true;
        }

        if (cmd.equalsIgnoreCase("slist")) {
            System.out.println("sender");
            String list = manage.handlePop3List(pop3ServerThread.getAuthUser().getUsername(), MailManage.MAIL_ROLE.SENDER_EMAIL);
            pop3ServerThread.printToClient("+OK");
            pop3ServerThread.printToClient(list);
            pop3ServerThread.printToClient(".");
            return true;
        }

        if (cmd.equalsIgnoreCase("retr")) {
            ArrayList<Integer> mail_ids = manage.getMailIds(pop3ServerThread.getAuthUser().getUsername(), MailManage.MAIL_ROLE.RECEIVER_EMAIL);
            String arg1 = getArg1(str);
            int mail_id = Integer.parseInt(arg1);
            if (!mail_ids.contains(mail_id)) {
                pop3ServerThread.printToClient("-ERR Not such mail id");
                return false;
            }
            String content = manage.handlePop3Retr(pop3ServerThread.getAuthUser().getUsername(),
                    mail_id);
            pop3ServerThread.printToClient("+OK");
            pop3ServerThread.printToClient(content);
            pop3ServerThread.printToClient(".");
            return true;
        }

        if (cmd.equalsIgnoreCase("setr")) {
            ArrayList<Integer> mail_ids = manage.getMailIds(pop3ServerThread.getAuthUser().getUsername(), MailManage.MAIL_ROLE.SENDER_EMAIL);
            String arg1 = getArg1(str);
            int mail_id = Integer.parseInt(arg1);
            if (!mail_ids.contains(mail_id)) {
                pop3ServerThread.printToClient("-ERR Not such mail id");
                return false;
            }
            String content = manage.handlePop3Retr(pop3ServerThread.getAuthUser().getUsername(),
                    mail_id);
            pop3ServerThread.printToClient("+OK");
            pop3ServerThread.printToClient(content);
            pop3ServerThread.printToClient(".");
            return true;
        }

        if (cmd.equalsIgnoreCase("dele")) {
            String arg1 = getArg1(str);
            int mail_id = Integer.parseInt(arg1);
            pop3ServerThread.getDelMailIds().add(mail_id);
            pop3ServerThread.printToClient("+OK dele " + mail_id);
            return true;
        }

        if (cmd.equalsIgnoreCase("rset")) {
            pop3ServerThread.getDelMailIds().clear();
            pop3ServerThread.printToClient("+OK");
            return true;
        }

        if (cmd.equalsIgnoreCase("top")) {
            // top
        }

        if (cmd.equalsIgnoreCase("quit")) {
            System.out.println("ddd " + pop3ServerThread.getDelMailIds().size());
            for (int i = 0; i < pop3ServerThread.getDelMailIds().size(); i++) {
                System.out.print("del sss");
                manage.handlePop3Del(pop3ServerThread.getDelMailIds().get(i));
            }
            pop3ServerThread.printToClient("+OK Pop3 mail server");
            pop3ServerThread.close();
            return true;
        }

        return false;
    }
}
