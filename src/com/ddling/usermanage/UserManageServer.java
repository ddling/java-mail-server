package com.ddling.usermanage;

import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by lingdongdong on 15/1/4.
 */
public class UserManageServer implements Runnable {

    private ServerSocket serverSocket = null;
    private int port = 1138;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Queue<User> regUserQueue = null;
    private int SO_TIME_OUT = 30000;

    public static Logger logger = LoggerFactory.getLogger(UserManageServer.class);

    public UserManageServer() {
        regUserQueue = new LinkedList<User>();
    }

    public UserManageServer(int port) {
        this.port = port;
        regUserQueue = new LinkedList<User>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("User Manage Server start at port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                logger.info("A client connect at " +
                        socket.getInetAddress().toString()
                        + " port " + socket.getPort());

                socket.setSoTimeout(SO_TIME_OUT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());

                String readData = in.readLine();
                String response = handle(readData);

                sendData(response);

                socket.close();
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private String handle(String readData) {

        UserManage usermanage = new UserManage();
        String response = "false";

        String cmd = getCommandStr(readData);

        // 判断用户是否存在
        if (cmd.equalsIgnoreCase("Exist")) {
            String username = readData.split(" ")[1];
            if (usermanage.hasUser(username)) {
                response = "true";
            }
        } else if (cmd.equalsIgnoreCase("Auth")) {
            String arg = readData.split(" ")[1];
            String username = arg.split(":")[0];
            String password = arg.split(":")[1];

            if (usermanage.authUser(username, password)) {
                response = "true";
            }
        } else if (cmd.equalsIgnoreCase("Register")) {
            String arg = readData.split(" ")[1];
            String username = arg.split(":")[0];
            String password = arg.split(":")[1];

            usermanage.insertNewUser(new User(username, password));
            response = "true";
        }
        return response;
    }

    private String getCommandStr(String str) {
        String cmd = str.split(" ")[0];
        if (cmd != null) {
            return cmd;
        }
        return "";
    }

    private void sendData(String data) {
        out.println(data);
        out.flush();
    }

    public static void main(String[] args) {
        new UserManageServer().run();
    }
}
