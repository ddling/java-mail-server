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
import com.ddling.UserHandler.UserManage;

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
