<%@ taglib
		prefix="c"
		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page
contentType="text/html;charset=UTF-8"
language="java" %>
<html>
<head>
	<meta charset="UTF-8">
	<title>订单管理</title>
	<%--静态包含base，css文件，jquery--%>
	<%@include file="/pages/common/head.jsp"%>
</head>
<body>
	
	<div id="header">
			<img class="logo_img" alt="" src="static/img/logo.gif" >
			<span class="wel_word">订单管理系统</span>
		<%--静态包含管理模块--%>
		<%@include file="/pages/common/manage_menu.jsp"%>
	</div>
	
	<div id="main">
		<table>
			<tr>
				<td>用户</td>
				<td>日期</td>
				<td>时间</td>
				<td>金额</td>
				<td>详情</td>
				<td>发货</td>
			</tr>

			<c:if test="${empty requestScope.orders}">
				<tr>
					<td colspan="5"><a href="index.jsp">当前没有订单</a>
					</td>
				</tr>
			</c:if>

			<c:forEach items="${requestScope.orders}" var="order">
				<tr>
					<td>${order.userId}</td>
					<td>${order.createTime.toLocalDate()}</td>
					<td>${order.createTime.toLocalTime()}</td>
					<td>${order.price}</td>
					<td><a href="#">查看详情</a></td>
					<td>
						<c:choose>
							<c:when test="${order.status == 0}">
								<a href="manager/managerOrderServlet?action=sendOrder&orderId=${order.orderId}">发货</a>
							</c:when>
							<c:when test="${order.status == 1}">
								已发货
							</c:when>
							<c:otherwise>
								已签收
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>


		</table>
	</div>

	<%@include file="/pages/common/footer.jsp"%>
</body>
</html>