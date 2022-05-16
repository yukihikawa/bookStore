<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
<meta charset="UTF-8">
<title>图书管理</title>
	<%--静态包含base，css文件，jquery--%>
	<%@include file="/pages/common/head.jsp"%>
	<script type="text/javascript">
		$(function (){
			$(".deleteBookClass").click(function (){
				return confirm("你确定要删除【" + $(this).parent().parent().find("td:first").text() + "】?");
			});
		});
	</script>
</head>
<body>
	
	<div id="header">
			<img class="logo_img" alt="" src="static/img/logo.gif" >
			<span class="wel_word">图书管理系统</span>
		<%--静态包含管理模块--%>
		<%@include file="/pages/common/manage_menu.jsp"%>
	</div>
	
	<div id="main">
		<table>
			<tr>
				<td>名称</td>
				<td>价格</td>
				<td>作者</td>
				<td>销量</td>
				<td>库存</td>
				<td colspan="2">操作</td>
			</tr>

			<c:forEach items="${requestScope.page.items}" var="book">
				<tr>
					<td>${book.name}</td>
					<td>${book.price}</td>
					<td>${book.author}</td>
					<td>${book.sales}</td>
					<td>${book.stock}</td>
					<td><a href="manager/bookServlet?action=getBook&id=${book.id}">修改</a></td>
					<td><a class = "deleteBookClass" href="manager/bookServlet?action=delete&id=${book.id}">删除</a></td>
				</tr>
			</c:forEach>


			

			
			<tr>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td><a href="pages/manager/book_edit.jsp">添加图书</a></td>
			</tr>	
		</table>
		<div id = "page_nav">
			<a herf="#">首页</a>
			<a herf="#">上一页</a>
			<a href="#">3</a>
			【${ requestScope.page.pageNo }】
			<a href="#">5</a>
			<a href="#">下一页</a>
			<a href="#">末页</a>
			共${requestScope.page.pageTotal}页，${requestScope.page.pageTotalCount}条记录
			到第<input value="4" name="pn" id="pn_input"/>页
			<input type="button" value="确定">
		</div>
	</div>

	<%@include file="/pages/common/footer.jsp"%>
</body>
</html>