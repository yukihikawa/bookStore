<%--
  Created by IntelliJ IDEA.
  User: emg
  Date: 2022/5/14
  Time: 16:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page
        contentType="text/html;charset=UTF-8"
        language="java" %>

<div>
    <c:if test="${empty requestScope.msg}">
        <a href="manager/page/1">图书管理</a>
        <a href="manager/showAllOrders">订单管理</a>

    </c:if>
    <a href="index.jsp">返回商城</a>


</div>