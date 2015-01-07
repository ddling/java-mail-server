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

import com.ddling.dbmanage.DBManage;
import com.ddling.server.smtp.State.SendEmail;
import com.ddling.utils.Constants;
import com.ddling.utils.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.*;

/**
 * Created by lingdongdong on 14/12/29.
 */
public class MailManage {

    public Logger logger = LoggerFactory.getLogger(MailManage.class);

    public enum MAIL_ROLE {SENDER_EMAIL, RECEIVER_EMAIL};

    /**
     * 得到收件箱中的邮件
     * @param email 隶属于某个邮箱的邮件
     * @return
     */
    public List<Mail> getReceiveEmailInBox(String email) {

        List<Mail> mailList = new LinkedList<Mail>();

        mailList = getEmailList("from", email);

        return mailList;
    }

    /**
     * 得到发件箱中的邮件
     * @param email 隶属于某个邮箱的邮件
     * @return
     */
    public List<Mail> getSendEmailInBox(String email) {

        List<Mail> mailList = new LinkedList<Mail>();

        mailList = getEmailList("to", email);

        return mailList;
    }

    /**
     * 将新的邮件保存到数据库中
     * @param mail 新邮件
     */
    public void storeEmail(Mail mail) {
        insertNewEmail(mail);
    }

    private List<Mail> getEmailList(String fromOrTO, String email) {
        List<Mail> mailList = new LinkedList<Mail>();

        DBManage dbManage = DBManage.getDbManageInstance();

        String sql = String.format(
                "SELECT * FROM EMAIL WHERE '%s' = '%s'",
                fromOrTO, email);

        ResultSet resultSet = dbManage.executeQuery(sql);

        try {
            while (resultSet.next()) {

                Mail mail = new Mail();
                String from = resultSet.getString("from");
                String to = resultSet.getString("to");
                String subject = resultSet.getString("subject");
                String content = resultSet.getString("content");
                int bytes = resultSet.getInt("bytes");

                mail.setMail_from(from);
                mail.setMail_to(to);
                mail.setSubject(subject);
                mail.setContent(content);
                mail.setMail_bytes(bytes);

                mailList.add(mail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mailList;
    }

    public Queue<Mail> getForeignEmails() {
        Queue<Mail> mailQueue = new LinkedList<Mail>();

        DBManage dbManage = DBManage.getDbManageInstance();

        String sql = String.format(
                "SELECT * FROM EMAIL WHERE localOrForeign = '%s'",
                Constants.FOREIGN_EMAIL);

        ResultSet resultSet = dbManage.executeQuery(sql);

        try {
            while (resultSet.next()) {
                Mail mail = new Mail();
                mail.setMail_from(resultSet.getString("mail_from"));
                System.out.println(resultSet.getString("mail_from"));
                mail.setMail_to(resultSet.getString("mail_to"));
                mail.setContent(resultSet.getString("content"));

                mailQueue.add(mail);
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return mailQueue;
    }

    private void insertNewEmail(Mail mail) {

        DBManage dbManage = DBManage.getDbManageInstance();

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm");
        String dateStr = simpleDateFormat.format(date);

        int bytes = mail.getMail_from().length() + mail.getMail_to().length()
                + mail.getSubject().length() + mail.getContent().length();

        String localOrForeign = getLocalMailOrForeign(mail.getMail_to());

        String sql = String.format(
                "INSERT INTO EMAIL VALUES (NULL, '%s', '%s', '%s', '%s', %d, '%s', '%s')",
                mail.getMail_from(), mail.getMail_to(), mail.getSubject(),
                mail.getContent(), bytes, dateStr, localOrForeign);

        dbManage.executeUpdate(sql);

    }

    private String getLocalMailOrForeign(String receiver) {
        String localOrForeign = Constants.LOCAL_EMAIL;

        String append = receiver.split("@")[1];

        if (!append.equalsIgnoreCase(Constants.LOCAL_ADDRESS)) {
            localOrForeign = Constants.FOREIGN_EMAIL;
        }

        return localOrForeign;
    }

    // POP3

    public String handlePop3Stat(String username) {

        String stat = "0 0";

        DBManage dbManage = DBManage.getDbManageInstance();

        String sql = String.format("SELECT count(*), sum(mail_bytes) FROM EMAIL WHERE username = '%s'", username);
        System.out.println(sql);

        ResultSet resultSet = dbManage.executeQuery(sql);

        try {
            if (resultSet.next()) {
                String count = resultSet.getString("count(*)");
                String sum = resultSet.getString("sum(mail_bytes)");

                if (count == null) {
                    count = "0";
                }

                if (sum == null) {
                    sum = "0";
                }

                stat = String.format("%s %s", count, sum);
            }
        } catch (SQLException e) {
            logger.error(e);
            stat = "0 0";
        }

        return stat;
    }

    public String handlePop3List(String username, MAIL_ROLE mailRole) {

        DBManage dbManage = DBManage.getDbManageInstance();

        String role = "";

        if (mailRole == MAIL_ROLE.SENDER_EMAIL) {
            role = "mail_from";
        } else if (mailRole == MAIL_ROLE.RECEIVER_EMAIL) {
            role = "mail_to";
        }

        String sql = String.format(
                "SELECT mail_id, mail_bytes FROM EMAIL WHERE %s LIKE '%s@%%'", role, username);
        System.out.println(sql);

        StringBuffer stringBuffer = new StringBuffer();

        ResultSet resultSet = dbManage.executeQuery(sql);

        try {
            while (resultSet.next()) {
                int mail_id = resultSet.getInt("mail_id");
                int bytes = resultSet.getInt("mail_bytes");
                stringBuffer.append(mail_id + " " + bytes + "\n");
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return stringBuffer.toString();
    }

    public String handlePop3Retr(String username, int mail_id) {

        DBManage dbManage = DBManage.getDbManageInstance();
        StringBuffer mail = new StringBuffer();

        String sql = String.format("SELECT * FROM EMAIL WHERE mail_id = %d", mail_id);

        ResultSet resultSet = dbManage.executeQuery(sql);

        try {
            if (resultSet.next()) {
                String mail_from = resultSet.getString("mail_from");
                mail.append("from:" + mail_from + "\n");

                String rcpt_to = resultSet.getString("mail_to");
                mail.append("to:" + rcpt_to + "\n");

                String subject = resultSet.getString("subject");
                mail.append("subject:" + subject + "\n");

                String mail_content = resultSet.getString("content");
                mail.append("content:" + mail_content + "\n");

                String sendTime = resultSet.getString("sendTime");
                mail.append("sendTime:" + sendTime + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mail.toString();
    }

    public void handlePop3Del(int mail_id) {

        String sql = String.format("DELETE FROM EMAIL WHERE mail_id = '%d'", mail_id);

        DBManage dbManage = DBManage.getDbManageInstance();

        dbManage.executeUpdate(sql);
    }

    public ArrayList<Integer> getMailIds(String username, MAIL_ROLE mailRole) {

        ArrayList<Integer> ids = new ArrayList<Integer>();

        String role = "";

        if (mailRole == MAIL_ROLE.SENDER_EMAIL) {
            role = "mail_from";
        } else if (mailRole == MAIL_ROLE.RECEIVER_EMAIL) {
            role = "mail_to";
        }

        String sql = String.format("SELECT mail_id FROM EMAIL WHERE %s LIKE '%s%%'", role, username);

        DBManage dbManage = DBManage.getDbManageInstance();

        ResultSet resultSet = dbManage.executeQuery(sql);

        try {
            while (resultSet.next()) {
                int mail_id = resultSet.getInt("mail_id");
                ids.add(mail_id);
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return ids;
    }

    /**
     * 得到POP3 STAT所需的邮件个数和总字节数
     * @param username 已经认证登陆的用户名
     * @param hostname 服务器后缀
     * @return 一个map， 其中mailNum里面保存的是邮件个数，allBytes里面保存的是总字节数
     */
    public Map<String, Integer> pop3STAT(String username, String hostname) {

        DBManage dbManage = DBManage.getDbManageInstance();

        String mailAddr = username + "@" + hostname;
        String sql = String.format("SELECT mail_bytes FROM EMAIL WHERE mail_to = '%s'", mailAddr);

        ResultSet result = dbManage.executeQuery(sql);

        int mailNum = 0;
        int allBytes = 0;

        try {
            while (result.next()) {
                mailNum ++;
                int bytes = result.getInt("mail_bytes");
                allBytes += bytes;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("mailNum", mailNum);
        map.put("allBytes", allBytes);
        System.out.print(mailNum);
        System.out.print(allBytes);

        return map;
    }

    /**
     * 得到POP3 LIST所需的邮件id以及对应邮件的字节数
     * @param username 已经认证登陆的用户名
     * @param hostAddr 对应的服务器后缀
     * @return
     */
    public List<Map<String, Integer>> POP3LIST(String username, String hostAddr) {
        DBManage dbmanage = DBManage.getDbManageInstance();
        List<Map<String, Integer>> list = new LinkedList<Map<String, Integer>>();

        String mailAddr = username + "@" + hostAddr;

        String sql = String.format("SELECT mail_id, mail_bytes FROM EMAIL WHERE mail_to = '%s'", mailAddr);

        ResultSet result = dbmanage.executeQuery(sql);

        try {
            while (result.next()) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                int mail_id = result.getInt("mail_id");
                int bytes = result.getInt("mail_bytes");
                map.put("mail_id", mail_id);
                map.put("bytes", bytes);

                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * POP3 得到邮件id为mail_id的那封邮件
     * @param username 以及认证的用户名
     * @param hostAddr 对应的服务器后缀
     * @param mail_id 要得到的邮件
     * @return
     */
    public Mail POP3RETR(String username, String hostAddr, int mail_id) {
        DBManage dbManage = DBManage.getDbManageInstance();
        Mail mail = new Mail();

        String mailAddr = username + "@" + hostAddr;

        String sql = String.format("SELECT * FROM EMAIL WHERE mail_id = %d", mail_id);

        ResultSet resultSet = dbManage.executeQuery(sql);

        try {
            if (resultSet.next()) {
                mail.setMail_from(resultSet.getString("mail_from"));
                mail.setMail_to(resultSet.getString("mail_to"));
                mail.setSubject(resultSet.getString("subject"));
                mail.setContent(resultSet.getString("content"));
                mail.setMail_bytes(resultSet.getInt("mail_bytes"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mail;
    }

    private String getUsername(String sender) {
        String username = "";
        username = sender.split("@")[0];
        return username;
    }

    public static void main(String[] args) {
        MailManage mailManage = new MailManage();
//        List<Map<String, Integer>> list = mailManage.POP3LIST("ddl", "qq.com");
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i).get("mail_id") + "\t" + list.get(i).get("bytes"));
//        }
        Mail mail = mailManage.POP3RETR("ddl", "qq.com", 2);
        System.out.println(mail.getContent());
    }
}
