package com.ddling.server.pop3.state;

import com.ddling.server.pop3.POP3ServerThread;

/**
 * Created by lingdongdong on 15/1/3.
 */
public abstract class State {

    private String[] commands = {"user", "pass", "stat", "list", "slist", "retr", "setr", "dele", "rset", "quit", "noop"};

    public abstract boolean process(POP3ServerThread pop3ServerThread, String str);

    protected boolean isValidCommand(String str) {
        if ("".equals(str))
            return false;

        String cmd = getArg0(str);

        for (int i = 0; i < commands.length; i++) {
            if (commands[i].equals(cmd)) {
                return true;
            }
        }
        return false;
    }

    protected String getArg0(String str) {
        int spacePos = str.indexOf(" ");
        if (spacePos == -1) {
            return str.toLowerCase();
        }
        return str.substring(0, spacePos).toLowerCase();
    }

    protected String getArg1(String str) {
        int spacePos = str.indexOf(" ");
        if (spacePos == -1) {
            return "";
        }
        return str.substring(spacePos + 1, str.length());
    }
}
