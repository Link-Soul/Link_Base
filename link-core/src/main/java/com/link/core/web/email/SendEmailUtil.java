package com.link.core.web.email;

import com.link.core.common.properties.EmailProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * @author Link
 * @Description 发送邮件工具类
 * @since 2025/8/18 08:53
 **/
@Slf4j
@Component
public class SendEmailUtil {

    @Resource
    private EmailProperties emailProperties;

    // 初始化时从配置中读取并赋值给静态变量
    @PostConstruct
    public void init() {
        // 获取当前激活的邮箱配置
        EmailProperties.MailConfig currentConfig = emailProperties.getCurrentConfig();

        if (currentConfig == null) {
            log.warn("邮箱配置未初始化");
            return;
        }

        // 赋值给静态变量
        senderEmailAccount = currentConfig.getUsername();
        senderEmailPassword = currentConfig.getPassword();
        myEmailSMTPHost = currentConfig.getHost();
        if (currentConfig.getUsername() == null || currentConfig.getHost() == null || currentConfig.getPassword() == null) {
            log.error("请检查邮箱配置");
        }
    }

    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
    //     对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
    // 163的密码持续180天，byd得实时更新。
    private static String senderEmailAccount;
    private static String senderEmailPassword;
    private static String myEmailSMTPHost;
    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址


    /**
     * 发送邮件，默认发件人
     *
     * @param subject   邮件主题
     * @param content   邮件内容
     * @param personal  发件人昵称 —— 显示在最上边的跟标题一样的东西
     * @param addressee 收件人邮箱
     * @throws MessagingException 邮件发送异常
     * @throws IOException        IO异常
     */
    public static void sendMail(String subject, String content, String personal, String addressee) throws MessagingException, IOException {
        // 1、初始化参数
        Properties props = getProps();

        // 2、根据参数配置，创建会话对象（为了发送邮件准备的）
        Session session = Session.getInstance(props);
        // 3、根据 Session 获取邮件传输对象
        MimeMessage message = createMailMessage(subject, content, personal, addressee, session);

        try {
            // 4、发送邮件
            sendMsg(session, message);
        } catch (Exception e) {
            log.error("发送邮件失败错误信息：{}", e.getMessage());
        }
    }

    /**
     * 初始化邮件参数
     *
     * @author Link
     * @since 2025/9/2 08:25
     */
    private static Properties getProps() {
        if (myEmailSMTPHost == null) {
            log.error("请检查邮箱配置");
            throw new RuntimeException("please init email config");
        }
        // 1. 创建一封邮件
        Properties props = new Properties();                // 用于连接邮件服务器的参数配置（发送邮件时才需要用到）
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.ssl.enable", "true");      // 启用SSL加密
        props.setProperty("mail.smtp.port", "465");             // 启用加密后的端口为 465，而非默认的 25 端口
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        return props;
    }

    /**
     * 发送一封邮件
     *
     * @param session 和服务器交互的会话
     * @param message 邮件对象
     * @throws MessagingException 邮件发送异常
     */
    private static void sendMsg(Session session, MimeMessage message) throws MessagingException {
        Transport transport = session.getTransport();

        //    1、使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        //
        //    PS_01: 如果连接服务器失败, 都会在控制台输出相应失败原因的log。
        //    仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接,
        //    根据给出的错误类型到对应邮件服务器的帮助网站上查看具体失败原因。
        //
        //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
        //           (1) 邮箱没有开启 SMTP 服务;
        //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
        //           (3) 邮箱服务器要求必须使用 SSL 安全连接;  **
        //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
        //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
        //
        transport.connect(senderEmailAccount, senderEmailPassword);

        // 2、 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());

        // 3、 关闭连接
        transport.close();
    }

    /**
     * 创建一封邮件，可以增加收件人、抄送人、密送人
     *
     * @param subject   邮件主题
     * @param content   邮件内容
     * @param personal  发件人昵称
     * @param addressee 收件人邮箱
     * @param session   和服务器交互的会话
     * @return 邮件对象
     * @throws MessagingException           邮件发送异常
     * @throws UnsupportedEncodingException 编码错误
     */
    private static MimeMessage createMailMessage(String subject, String content, String personal, String addressee, Session session) throws MessagingException, UnsupportedEncodingException {
        // 1. 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        /*
         * 也可以根据已有的eml邮件文件创建 MimeMessage 对象。模板方式
         * MimeMessage message = new MimeMessage(session, new FileInputStream("myEmail.eml"));
         */
        // 2. From: 发件人
        //    其中 InternetAddress 的三个参数分别为: 邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
        message.setFrom(new InternetAddress(senderEmailAccount, personal, "UTF-8"));

        // 3. To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(addressee, "USER_CC", "UTF-8"));
        //    To: 增加收件人（可选）
//        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress("dd@receive.com", "USER_DD", "UTF-8"));
        //    Cc: 抄送（可选）
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("ee@receive.com", "USER_EE", "UTF-8"));
        //    Bcc: 密送（可选）
//        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress("ff@receive.com", "USER_FF", "UTF-8"));

        // 4. Subject: 邮件主题
        message.setSubject(subject, "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(content, "text/html;charset=UTF-8");

        // 6. 设置显示的发件时间
        message.setSentDate(new Date());

        // 7. 保存前面的设置
        message.saveChanges();

        // 8. 将该邮件保存到本地
//        OutputStream out = new FileOutputStream("myEmail.eml");
//        message.writeTo(out);
//        out.flush();
//        out.close();

        return message;
    }
}
