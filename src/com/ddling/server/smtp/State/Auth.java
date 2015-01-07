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

import org.apache.commons.codec.binary.Base64;

/**
 * Created by ddling on 2014/12/24.
 */
public class Auth extends State {

    public boolean process(SMTPThread smtpThread, String str) {

        // 得到指令的命令字符串以及命令参数
        String cmd = this.getCommandStr(str);
        String arg = this.getArgumentStr(str);

        // 判断命令是否合法
        if (!isValidCommand(cmd)) {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }

        // Auth命令需要添加参数login
        if (arg == "") {
            smtpThread.printToClient("");
            return false;
        }

        if (cmd.equalsIgnoreCase("auth") && !arg.equalsIgnoreCase("login")) {
            smtpThread.printToClient("504 Unrecognized authentication type");
            return false;
        } else if (cmd.equals("auth") && arg.equals("login")) {
            smtpThread.printToClient("334 " + Base64.encodeBase64String("username:".getBytes()));
            return true;
        } else {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }
    }
}
