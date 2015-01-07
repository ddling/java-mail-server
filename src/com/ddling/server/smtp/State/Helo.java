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
public class Helo extends State{

    @Override
    public boolean process(SMTPThread smtpThread, String str) {

        // 得到指令的命令字符串以及命令参数
        String cmd = this.getCommandStr(str);
        String arg = this.getArgumentStr(str);

        // 判断命令是否合法
        if (!isValidCommand(str)) {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }

        // Helo命令需要添加参数
        if (arg == "") {
            smtpThread.printToClient("500 Error: bad syntax");
            return false;
        }

        // 需要先向服务器发送Helo命令
        if (!cmd.equals("helo") && !cmd.equals("ehlo")) {
            smtpThread.printToClient("503 Error: send HELO/EHLO first");
            return false;
        } else {
            smtpThread.printToClient("250 OK");
            return true;
        }
    }
}
