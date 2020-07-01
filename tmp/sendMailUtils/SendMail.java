package sendMail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class SendMail {


    public void send(String to,String text){
        //创建链接对象到邮件服务器
        Properties properties = new Properties();
        properties.put("mail.transport.protocol","smtp");//连接协议
        properties.put("mail.smtp.host","smtp.qq.com");//主机名
        properties.put("mail.smtp.port","465");//端口号
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.ssl.enable","true");//设置是否使用ssl连接
        //得到回话
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("XXX@qq.com","password");
            }
        });
        //创建邮箱对象
        Message message = new MimeMessage(session);
        //设置发件人
        try {
            message.setFrom(new InternetAddress("XXX@qq.com"));
            //设置收件人
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            //设置邮箱标题
            message.setSubject("通知信息");
            message.setSentDate(new Date());
            //设置邮件内容
            message.setContent(text,"text/html;charset=utf-8");
            //得到邮差对象
            Transport transport = session.getTransport();
            transport.connect("XXX@qq.com","XXX");
            //发送
            transport.sendMessage(message,message.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){

        SendMail sendMail = new SendMail();
        sendMail.send("XXX@qq.com","hello world!");
    }
}
