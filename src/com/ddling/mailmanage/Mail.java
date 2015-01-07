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

package com.ddling.mailmanage;

/**
 * Created by lingdongdong on 14/12/29.
 */
public class Mail {

    private String mail_from = "";
    private String mail_to = "";
    private String subject = "";
    private String content = "";
    private String localOrForeign = "";
    private int mail_bytes = 0;

    public Mail() {

    }

    public Mail(String from, String to, String subject, String content) {
        this.mail_from = from;
        this.mail_to = to;
        this.subject = subject;
        this.content = content;
    }

    public String getMail_from() {
        return mail_from;
    }

    public void setMail_from(String mail_from) {
        this.mail_from = mail_from;
    }

    public String getMail_to() {
        return mail_to;
    }

    public void setMail_to(String mail_to) {
        this.mail_to = mail_to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMail_bytes() {
        return mail_bytes;
    }

    public void setMail_bytes(int mail_bytes) {
        this.mail_bytes = mail_bytes;
    }

    public void setLocalOrForeign(String localOrForeign) {
        this.localOrForeign = localOrForeign;
    }

    public String getLocalOrForeign() {
        return localOrForeign;
    }
}
