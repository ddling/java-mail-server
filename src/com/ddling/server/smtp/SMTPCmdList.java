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

package com.ddling.server.smtp;

import com.ddling.server.smtp.State.*;
import com.ddling.utils.Constants;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by ddling on 2014/12/24.
 * 命令队列，对客户端发来的消息进行分析并处理，返回特定信息
 */
public class SMTPCmdList {

    private Queue<State> cmdQueue = null;

    public SMTPCmdList() {
        cmdQueue = new LinkedList<State>();
        cmdQueue.add(new Helo());
    }

    public void process(SMTPThread smtpThread, String cmd) {

        // 当命令队列不为空时，处理位于队列头的客户端发来的指令
        if (!cmdQueue.isEmpty()) {

            // 队列头部的状态指令
            State currentState = cmdQueue.element();

            // 处理HELO指令
            if (currentState instanceof Helo) {
                if (currentState.process(smtpThread, cmd)) {
                    processHelo(smtpThread);
                }
            }

            // 处理AUTH指令
            if (currentState instanceof Auth) {
                if (currentState.process(smtpThread, cmd)) {
                    processAuth();
                }
            }

            // 处理LOGIN指令
            if (currentState instanceof Login) {
                if (currentState.process(smtpThread, cmd)) {
                    processLogin();
                }
            }

            // 处理MAIL指令
            if (currentState instanceof MailFrom) {
                if (currentState.process(smtpThread, cmd)) {
                    processMail();
                }
            }

            // 处理RCPT指令
            if (currentState instanceof RcptTo) {
                if (currentState.process(smtpThread, cmd)) {
                    processRcpt();
                }
            }

            // 处理DATA指令
            if (currentState instanceof Data) {
                if (currentState.process(smtpThread, cmd)) {
                    processData();
                }
            }

            // 处理SENDMAIL
            if (currentState instanceof SendEmail) {
                if (currentState.process(smtpThread, cmd)) {
                    cmdQueue.remove();
                    System.out.println("send Email OK");
                }
            }
        }
    }

    /**
     * 处理HELO指令，判断当前服务器类型
     * 如果是作为服务器使用，则需要验证当前用户
     * 如果是作为发送邮件的客户端使用，则跳过验证用户
     * @param smtpThread
     */
    private void processHelo(SMTPThread smtpThread) {
        int serverType = smtpThread.getCurrentServerType();
        if (serverType == Constants.SERVER_TYPE_FOR_CLIENT) {
            cmdQueue.remove();
            cmdQueue.add(new Auth());
        } else if (serverType == Constants.SERVER_TYPE_FOR_SERVER) {
            cmdQueue.remove();
            cmdQueue.add(new MailFrom());
        }
    }

    /**
     * 处理Auth指令
     */
    private void processAuth() {
        cmdQueue.remove();
        cmdQueue.add(new Login());
    }

    /**
     * 处理Login指令
     */
    private void processLogin() {
        cmdQueue.remove();
        cmdQueue.add(new MailFrom());
    }

    /**
     * 处理Mail指令
     */
    private void processMail() {
        cmdQueue.remove();
        cmdQueue.add(new RcptTo());
    }

    /**
     * 处理Rcpt指令
     */
    private void processRcpt() {
        cmdQueue.remove();
        cmdQueue.add(new Data());
    }

    /**
     * 处理Data指令
     */
    private void processData() {
        cmdQueue.remove();
        cmdQueue.add(new SendEmail());
    }
}
