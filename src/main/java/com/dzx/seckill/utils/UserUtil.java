package com.dzx.seckill.utils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.dzx.seckill.persistence.bean.User;
import com.dzx.seckill.vo.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 生成用户工具类
 * @author: dzx
 * @date: 2022/3/10
 */


public class UserUtil {
    private static void createUser(int count) throws Exception{
        List<User> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(13000000000L+i);
            user.setNickname("user"+i);
            user.setPassword(MD5Util.inputPassToDBPass("123456",String.valueOf(user.getId())));
            users.add(user);
        }
        System.out.println("create user");
//        插入数据库
//        Connection connection = getConn();
//        String sql = "insert into seckill.t_user(id,nickname,password) values(?,?,?)";
//        PreparedStatement preparedStatement=connection.prepareStatement(sql);
//        for(int i=0;i< users.size();i++){
//            User user = users.get(i);
//            preparedStatement.setLong(1,user.getId());
//            preparedStatement.setString(2,user.getNickname());
//            preparedStatement.setString(3, user.getPassword());
//            preparedStatement.addBatch();
//        }
//        preparedStatement.executeBatch();
//        preparedStatement.clearParameters();
//        connection.close();
        System.out.println("insert to db");
//        生成user Ticket
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("D:\\IdeaProjects\\seckill/config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        for (int i = 0; i < count; i++) {
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream outputStream = co.getOutputStream();
            String prams = "mobile=" + user.getId() + "&password=" + MD5Util.encryptForm("123456");
            outputStream.write(prams.getBytes());
            outputStream.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) > 0) {
                byteArrayOutputStream.write(bytes,0,len);
            }
            inputStream.close();
            byteArrayOutputStream.close();
            String response = byteArrayOutputStream.toString();
            System.out.println(response);
            ObjectMapper objectMapper = new ObjectMapper();
            RespBean respBean = objectMapper.readValue(response, RespBean.class);
            String userTicket = (String) respBean.getObject();
            System.out.println("create userTicket："+user.getId());
            String row = user.getId() + "." + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes(StandardCharsets.UTF_8));
            System.out.println("write to file :" + user.getId());
        }
        raf.close();

    }

    private static Connection getConn() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/seckill?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8";
        String username = "root";
        String passwd = "510510";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, passwd);
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }
}
