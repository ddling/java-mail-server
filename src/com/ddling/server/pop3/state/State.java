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
