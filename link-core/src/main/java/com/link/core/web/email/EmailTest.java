package com.link.core.web.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import java.io.IOException;


@RestController
@RequestMapping("/")
@Slf4j
public class EmailTest {

    @RequestMapping("/sendEmail")
    public void sendEmail() throws Exception {
        try {
            SendEmailUtil.sendMail("测试邮件", "测试邮件内容", "测试", "1749449185@qq.com");
        } catch (MessagingException e) {
            log.error("发送邮件信息内容错误", e);
            return;
        } catch (IOException e) {
            log.error("发送邮件IO错误", e);
            return;
        }
        System.out.println("发送成功");
    }
}
