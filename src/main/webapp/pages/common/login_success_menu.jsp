<%--
  Created by IntelliJ IDEA.
  User: emg
  Date: 2022/5/14
  Time: 14:56
  To change this template use File | Settings | File Templates.
  登陆成功后的菜单，使用静态包含替换
--%>
<%@ page
        contentType="text/html;charset=UTF-8"
        language="java" %>

<div>
    <span>欢迎<span class="um_span">${sessionScope.user.username}</span>光临尚硅谷书城</span>
    <a href="clientOrderServlet?action=showMyOrders">我的订单</a>
    <a href="userServlet?action=logout">注销</a>&nbsp;&nbsp;
    <a href="index.jsp">返回</a>
</div>