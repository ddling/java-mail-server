package com.ddling.server.userManage;

import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by lingdongdong on 15/1/7.
 */
public class UserManageThread implements Runnable {

    // 客户端Socket
    private Socket clientSocket       = null;
    // 客户端Socket的输入端
    private BufferedReader in         = null;
    // 客户端Socket的输出端
    private PrintWriter out           = null;
    // 设置客户端Socket的超时时间
    private int SO_TIME_OUT           = 30000;
    // 日志
    public static Logger logger       = LoggerFactory.getLogger(UserManageThread.class);

    public UserManageThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            clientSocket.setSoTimeout(SO_TIME_OUT);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());

            UserCommandHandler userCommandHandler = new UserCommandHandler();
            userCommandHandler.handle(this, getClientCommand());
            close();
        } catch (IOException e) {
            logger.error(e);
        } finally {
            close();
        }

    }

    /**
     * 读取客户端Socket输入端的内容
     * @return 读入的内容
     */
    private String getClientCommand() {

        String command = "";
        try {
            command = in.readLine();
            if (command == null) {
                command = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return command;
    }

    /**
     * 向客户端Socket的输出端输出内容
     * @param data 输出的内容
     */
    public void printToClient(String data) {
        out.println(data);
        out.flush();
    }

    /**
     * 关闭该客户端Socket
     */
    public void close() {

        if (clientSocket != null) {
            try {
                clientSocket.close();
                clientSocket = null;
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }
}
