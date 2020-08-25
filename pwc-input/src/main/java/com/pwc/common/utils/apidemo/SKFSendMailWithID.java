package com.pwc.common.utils.apidemo;


import com.pwc.modules.input.entity.SKfNet;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SKFSendMailWithID {

    public static void main(String [] args)
    {
        // 收件人电子邮箱
        String to = "1980242157@qq.com";

        // 发件人电子邮箱
        String from = SKfNet.From;
        String user = SKfNet.User;
        String pwd = SKfNet.Pwd;
        // 指定发送邮件的主机为 localhost
        String host = SKfNet.Host;

        String port = "25";
        String is_AUTH = "true";

        // 获取系统属性
        Properties properties = System.getProperties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.port",port);
        properties.setProperty("mail.smtp.auth",is_AUTH);

        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties);

        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段
            message.setFrom(new InternetAddress(from));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(to));
            message.setSentDate(new Date());
            // Set Subject: 头部头字段
            message.setSubject("This is the Subject Line!");

            // 设置消息体
            message.setText("This is actual message");

            // 发送消息
            Transport transport = session.getTransport("smtp");
            transport.connect(host,user,pwd);

            transport.sendMessage(message,message.getAllRecipients());
            transport.close();
            System.out.println("Sent message successfully....");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
