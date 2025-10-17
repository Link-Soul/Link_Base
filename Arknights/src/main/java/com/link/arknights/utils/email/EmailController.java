package com.link.arknights.utils.email;

import com.link.core.web.email.SendEmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Link
 * @Description 邮件发送控制类
 * @since 2025/9/1 15:12
 **/
@RequestMapping("/email")
@RestController
@Slf4j
public class EmailController {

    // 咱的邮箱
    private final String email = "1749449185@qq.com";

    @RequestMapping("/MaaSendComplete")
    public String MaaSendComplete() {
        try {
            SendEmailUtil.sendMail("任务完成", "Maa任务完成", "Maa", email);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
            return e.getMessage();
        }
        return "success";
    }

    @RequestMapping("/MAAsendComplete/{sendTo}")
    public String MaaSendComplete(@PathVariable("sendTo") String sendTo) {
        if (sendTo == null || sendTo.isEmpty()) {
            return "请输入收件人邮箱";
        }
        try {
            SendEmailUtil.sendMail("Maa任务完成", "Maa任务完成", "Maa", sendTo);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
            return "false";
        }
        return "success";
    }
}
