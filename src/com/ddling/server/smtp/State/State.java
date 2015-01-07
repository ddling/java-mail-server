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

package com.ddling.server.smtp.State;

import com.ddling.server.smtp.SMTPThread;

/**
 * Created by ddling on 2014/12/24.
 */
public abstract class State {

    private String[] commands = {"helo", "ehlo", "auth", "mail", "rcpt", "data", "quit"};

    /**
     * This abstract function is to process the command from client
     * @param smtpThread The current smtp thread
     * @param cmd The command from client
     */
    public abstract boolean process(SMTPThread smtpThread, String cmd);

    protected boolean isValidCommand(String str) {
        if ("".equals(str))
            return false;

        String cmd = getCommandStr(str);

        for (int i = 0; i < commands.length; i++) {
            if (commands[i].equals(cmd)) {
                return true;
            }
        }
        return false;
    }

    protected String getCommandStr(String str) {
        int spacePos = str.indexOf(" ");
        if (spacePos == -1) {
            return str.toLowerCase();
        }
        return str.substring(0, spacePos).toLowerCase();
    }

    protected String getArgumentStr(String str) {
        int spacePos = str.indexOf(" ");
        if (spacePos == -1) {
            return "";
        }
        return str.substring(spacePos + 1, str.length());
    }
}
