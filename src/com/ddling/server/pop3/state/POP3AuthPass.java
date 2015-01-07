package com.ddling.server.pop3.state;

import com.ddling.server.pop3.POP3ServerThread;
import com.ddling.usermanage.UserManage;

/**
 * Created by lingdongdong on 15/1/3.
 */
public class POP3AuthPass extends State {
    @Override
    public boolean process(POP3ServerThread pop3ServerThread, String str) {

        String arg0 = getArg0(str);
        String arg1 = getArg1(str);

        if (!isValidCommand(arg0)) {
            pop3ServerThread.printToClient("-ERR Unknown command " + arg0);
        }

        if (arg0.toLowerCase().equals("pass")) {
            UserManage userManage = new UserManage();
            if (!userManage.authUser(pop3ServerThread.getAuthUser().getUsername(), arg1)) {
                pop3ServerThread.printToClient("-ERR Unable to log on");
                return false;
            }
            pop3ServerThread.printToClient("+OK login successful");
            return true;
        } else {
            pop3ServerThread.printToClient("-ERR Command not valid in this state");
            return false;
        }
    }
}
