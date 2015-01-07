package com.ddling.server.userManage;

import com.ddling.UserHandler.User;
import com.ddling.UserHandler.UserManage;

/**
 * Created by lingdongdong on 15/1/7.
 */
public class UserCommandHandler {

    // 用户管理可用的命令集合
    private String commands[]     = {"Auth", "Reg", "Exist"};
    private UserManage userManage = null;

    public UserCommandHandler() {
        userManage = UserManage.getUserManageInstance();
    }

    /**
     * 判断用户的指令是否合法
     * @param cmd 用户指令
     * @return 合法则返回true。 非法则返回false
     */
    private boolean isValidCmd(String cmd) {
        boolean isValid = false;

        for (int i = 0; i < commands.length; i++) {
            if (cmd.equalsIgnoreCase(commands[i])) {
                isValid = true;
                break;
            }
        }

        return isValid;
    }

    /**
     * 处理用户指令
     * @param userManageThread 该用户与服务器相连的线程
     * @param commandStr 用户指令
     */
    public void handle(UserManageThread userManageThread, String commandStr) {

        String cmd = getCommand(commandStr);
        if (!isValidCmd(cmd)) {
            userManageThread.printToClient("-ERR Command not valid!");
        }

        if (cmd.equalsIgnoreCase("Exist")) {
            handleExist(userManageThread, commandStr);
        } else if (cmd.equalsIgnoreCase("Auth")) {
            handleAuth(userManageThread, commandStr);
        } else if (cmd.equalsIgnoreCase("Reg")) {
            handleReg(userManageThread, commandStr);
        } else {
            userManageThread.printToClient("-ERR Not implement command!");
        }
    }

    /**
     * 判断用户是否存在
     * @param userManageThread 该用户的线程
     * @param commandStr 用户指令
     */
    private void handleExist(UserManageThread userManageThread, String commandStr) {

        String username = commandStr.split(" ")[1];
        if (userManage.hasUser(username)) {
            userManageThread.printToClient("+TRUE Has user!");
        } else {
            userManageThread.printToClient("-FALSE Not user!");
        }

    }

    /**
     * 判断用户是否验证成功
     * @param userManageThread 该用户的线程
     * @param commandStr 用户指令
     */
    private void handleAuth(UserManageThread userManageThread, String commandStr) {

        String arg = commandStr.split(" ")[1];

        if (arg.split(":").length == 2) {
            String username = arg.split(":")[0];
            String password = arg.split(":")[1];

            if (userManage.authUser(username, password)) {
                userManageThread.printToClient("+TRUE Auth user done!");
            } else {
                userManageThread.printToClient("-ERR Auth user fail!");
            }
        } else {
            userManageThread.printToClient("-ERR Syntax Error!");
        }

    }

    /**
     * 注册新用户
     * @param userManageThread 该用户的线程
     * @param commandStr 用户指令
     */
    private void handleReg(UserManageThread userManageThread, String commandStr) {

        String arg = commandStr.split(" ")[1];
        String username = arg.split(":")[0];
        String password = arg.split(":")[1];

        if (!(username.equals("") && password.equals(""))) {
            userManage.insertNewUser(new User(username, password));
            userManageThread.printToClient("+TRUE Reg User done!");
        } else {
            userManageThread.printToClient("-ERR Reg User fail!");
        }

    }

    /**
     * 得到用户指令中的命令
     * @param str 用户指令
     * @return 命令
     */
    private String getCommand(String str) {
        String cmd = str.split(" ")[0];
        if (cmd != null) {
            return cmd;
        }
        return "";
    }
}
