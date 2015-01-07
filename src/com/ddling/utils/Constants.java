package com.ddling.utils;

import com.ddling.server.smtp.State.State;
import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created by ddling on 2014/12/24.
 */
public class Constants {

    public static int   SERVER_TYPE_FOR_SERVER = 0;
    public static int   SERVER_TYPE_FOR_CLIENT = 1;
    public static String DB_FILE_NAME = "mail.db";
    public static String LOCAL_SMTP_SERVER_ADDRESS = "127.0.0.1";
    public static int LOCAL_SMTP_SERVER_PORT = 1198;
    public static int CLIENT_SEND_TO_LOCAL = 0;
    public static int CLIENT_SEND_TO_FOREIGN = 1;
    public static String SETTING_FILE_PATH = "setting.json";

    // 本机地址
    public static final String LOCAL_ADDRESS = "localhost.cn";

    public static final String LOCAL_EMAIL = "local";

    public static final String FOREIGN_EMAIL = "foreign";
}
