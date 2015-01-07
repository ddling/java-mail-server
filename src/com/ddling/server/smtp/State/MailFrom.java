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
import java.util.regex.*;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class MailFrom extends State {

    public boolean process(SMTPThread smtpThread, String str) {

        String cmd = this.getCommandStr(str);
        String arg = this.getArgumentStr(str);

        System.out.println(str);

        if (!isValidCommand(cmd)) {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }

        if (arg.equals("")) {
            smtpThread.printToClient("500 Error: bad syntax");
            return false;
        }

        if (validEmailAddress(arg)) {
            smtpThread.getMail().setMail_from(getMailAddress(arg));
            smtpThread.printToClient("250 Mail OK");
            return true;
        } else {
            smtpThread.printToClient("500 Bad Email address!");
            return false;
        }
    }

    private boolean validEmailAddress(String arg) {

        boolean flag =  Pattern.matches("^\\w+:<\\w+@\\w+(\\.\\w+)*>$", arg);

        if (!flag) {
            return false;
        }

        String args[] = arg.split(":");
        if (args[0].equals("from") && isEmailAddress(getMailAddress(arg))) {
            flag = true;
        }
        return flag;
    }

    private boolean isEmailAddress(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private String getMailAddress(String arg) {
        String mailAddress = arg.substring(arg.indexOf("<") + 1, arg.indexOf(">"));
        return mailAddress;
    }
}
