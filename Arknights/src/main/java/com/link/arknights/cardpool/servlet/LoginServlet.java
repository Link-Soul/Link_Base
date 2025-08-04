package com.link.arknights.cardpool.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"); // 获取用户名参数
        String password = request.getParameter("password"); // 获取密码参数

        if (checkCredentials(username, password)) { // 调用自定义函数校验用户名和密码
            HttpSession session = request.getSession(); // 获取session对象

            // 设置session属性，保存用户信息
            session.setAttribute("username", username);

            Cookie cookie = new Cookie("JSESSIONID", session.getId()); // 创建Cookie对象
            cookie.setMaxAge(-1); // 永不过期
            response.addCookie(cookie); // 将Cookie添加到响应头部

            response.sendRedirect("/WEB-INF/jsp/index.jsp"); // 重定向到主页
        } else {
            // 登录失败，显示错误消息或者返回登录页面
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials.");
        }
    }

    private boolean checkCredentials(String username, String password) {
        // TODO: 连接数据库查询用户信息，并与传入的用户名和密码进行比对
        return true; // 模拟校验结果，此处应改为真正的校验逻辑
    }
}