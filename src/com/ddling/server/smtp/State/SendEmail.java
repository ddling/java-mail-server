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

import com.ddling.MailHandler.MailManage;
import com.ddling.server.smtp.SMTPService;
import com.ddling.server.smtp.SMTPThread;

import java.util.Date;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class SendEmail extends State {

    private StringBuffer stringBuffer = new StringBuffer();

    public boolean process(SMTPThread smtpThread, String str) {

        if (".".equals(str)) {
            smtpThread.getMail().setContent(stringBuffer.toString());
            if (stringBuffer.toString().length() > 10) {
                smtpThread.getMail().setSubject(stringBuffer.toString().substring(0, 10));
            } else {
                smtpThread.getMail().setSubject(stringBuffer.toString());
            }
            SMTPService smtpService = new SMTPService();
            MailManage mailManage = new MailManage();
            mailManage.storeEmail(smtpThread.getMail());

            long date = new Date().getTime();
            String dateStr = date + "";
            smtpThread.printToClient("250 Mail OK queued as smtp12,EMCowECp3SK+FqJUzWGdBw--.997S2 " + dateStr);
            return true;
        }

        stringBuffer.append(str + "\r\n");
        return false;
    }
}
