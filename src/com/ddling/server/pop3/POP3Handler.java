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

package com.ddling.server.pop3;

import com.ddling.server.pop3.state.POP3AuthPass;
import com.ddling.server.pop3.state.POP3AuthUser;
import com.ddling.server.pop3.state.POP3Operater;
import com.ddling.server.pop3.state.State;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by lingdongdong on 15/1/3.
 */
public class POP3Handler {

    private boolean isAuthOK = false;
    private Queue<State> stateQueue = null;

    public POP3Handler() {
        stateQueue = new LinkedList<State>();
        stateQueue.add(new POP3AuthUser());
    }

    public void handle(POP3ServerThread pop3ServerThread, String str) {

        if (!stateQueue.isEmpty()) {

            State state = stateQueue.element();

            if (state instanceof POP3AuthUser) {
                if (state.process(pop3ServerThread, str)) {
                    stateQueue.remove();
                    stateQueue.add(new POP3AuthPass());
                }
            }

            if (state instanceof POP3AuthPass) {
                if (state.process(pop3ServerThread, str)) {
                    stateQueue.remove();
                    isAuthOK = true;
                }
            }
        }

        if (isAuthOK) {
            State state = new POP3Operater();
            state.process(pop3ServerThread, str);
        }

        if (str.equalsIgnoreCase("noop")) {
            pop3ServerThread.printToClient("+OK Pop3 mail server");
        }
    }
}
