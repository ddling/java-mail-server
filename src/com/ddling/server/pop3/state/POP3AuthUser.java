package com.ddling.server.pop3.state;

import com.ddling.server.pop3.POP3ServerThread;
import com.ddling.usermanage.UserManage;

/**
 * Created by lingdongdong on 15/1/3.
 */
public class POP3AuthUser extends State {

    @Override
    public boolean process(POP3ServerThread pop3ServerThread, String str) {

        String arg0 = getArg0(str);
        String arg1 = getArg1(str);

        if (!isValidCommand(arg0)) {
            pop3ServerThread.printToClient("-ERR Unknown command " + arg0);
        }

        if (arg0.toLowerCase().equals("user")) {
            UserManage userManage = new UserManage();
            if (!userManage.hasUser(arg1)) {
                pop3ServerThread.printToClient("-ERR Local POP3 server has not user " + arg1);
                return false;
            }

            pop3ServerThread.printToClient("+OK ddling mail");
            pop3ServerThread.getAuthUser().setUsername(arg1);
            return true;
        }

        return false;
    }
}
