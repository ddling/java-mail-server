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

package com.ddling.utils;

import com.ddling.server.smtp.State.State;
import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created by ddling on 2014/12/24.
 */
public class Constants {

    public static int   SERVER_TYPE_FOR_SERVER = 0;
    public static int   SERVER_TYPE_FOR_CLIENT = 1;
    public static String DB_FILE_NAME = "db/mail.db";
    public static String LOCAL_SMTP_SERVER_ADDRESS = "127.0.0.1";
    public static int LOCAL_SMTP_SERVER_PORT = 1198;
    public static int CLIENT_SEND_TO_LOCAL = 0;
    public static int CLIENT_SEND_TO_FOREIGN = 1;
    public static String SETTING_FILE_PATH = "setting.json";

    // 本机地址
    public static final String LOCAL_ADDRESS = "localhost.cn";

    public static final String LOCAL_EMAIL = "local";

    public static final String FOREIGN_EMAIL = "foreign";

    // 用户管理服务器地址和端口
    public static final int USER_MANAGE_PORT = 1140;
}
