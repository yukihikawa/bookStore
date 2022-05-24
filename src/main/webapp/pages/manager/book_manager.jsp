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
					<td><a href="manager/getBook/${book.id}/${requestScope.page.pageNo}">修改</a></td>
					<td><a class = "deleteBookClass" href="manager/delete/${book.id}/${requestScope.page.pageNo}">删除</a></td>
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
		<%--<div id = "page_nav">

			<c:if test="${requestScope.page.pageNo > 1}">
				<a href="manager/bookServlet?action=page&pageNo=1">首页</a>
				<a href="manager/bookServlet?action=page&pageNo=${requestScope.page.pageNo - 1}">上一页</a>

			</c:if>
			&lt;%&ndash;<a href="#">${requestScope.page.pageNo - 1}</a>
			【${ requestScope.page.pageNo }】
			<a href="#">${requestScope.page.pageNo + 1}</a>&ndash;%&gt;
			&lt;%&ndash;页码输出的开始&ndash;%&gt;
			&lt;%&ndash;选择页码参数&ndash;%&gt;
			<c:choose>
				&lt;%&ndash;情况 1：如果总页码小于等于 5 的情况，页码的范围是：1-总页码&ndash;%&gt;
				<c:when test="${ requestScope.page.pageTotal <= 5 }">
					<c:set var="begin" value="1"/>
					<c:set var="end" value="${requestScope.page.pageTotal}"/>
				</c:when>
				&lt;%&ndash;情况 2：总页码大于 5 的情况&ndash;%&gt;
				<c:when test="${requestScope.page.pageTotal > 5}">
					<c:choose>
						&lt;%&ndash;小情况 1：当前页码为前面 3 个：1，2，3 的情况，页码范围是：1-5.&ndash;%&gt;
						<c:when test="${requestScope.page.pageNo <= 3}">
							<c:set var="begin" value="1"/>
							<c:set var="end" value="5"/>
						</c:when>
						&lt;%&ndash;小情况 2：当前页码为最后 3 个，8，9，10，页码范围是：总页码减 4 - 总页码&ndash;%&gt;
						<c:when test="${requestScope.page.pageNo > requestScope.page.pageTotal-3}">
							<c:set var="begin" value="${requestScope.page.pageTotal-4}"/>
							<c:set var="end" value="${requestScope.page.pageTotal}"/>
						</c:when>
						&lt;%&ndash;小情况 3：4，5，6，7，页码范围是：当前页码减 2 - 当前页码加 2&ndash;%&gt;
						<c:otherwise>
							<c:set var="begin" value="${requestScope.page.pageNo-2}"/>
							<c:set var="end" value="${requestScope.page.pageNo+2}"/>
						</c:otherwise>
					</c:choose>
				</c:when>
			</c:choose>

			&lt;%&ndash;开始输出&ndash;%&gt;
			<c:forEach begin="${begin}" end="${end}" var="i">
					<c:if test="${i == requestScope.page.pageNo}">
						【${i}】
					</c:if>
					<c:if test="${i != requestScope.page.pageNo}">
						<a href="manager/bookServlet?action=page&pageNo=${i}">${i}</a>
					</c:if>
				</c:forEach>
			&lt;%&ndash;页码输出的结束&ndash;%&gt;
			&lt;%&ndash;已经是最后一页，则不显示下一页&ndash;%&gt;
			<c:if test="${requestScope.page.pageNo < requestScope.page.pageTotal}">

				<a href="manager/bookServlet?action=page&pageNo=${requestScope.page.pageNo + 1}">下一页</a>
				<a href="manager/bookServlet?action=page&pageNo=${requestScope.page.pageTotal}">末页</a>
			</c:if>

			共${requestScope.page.pageTotal}页，${requestScope.page.pageTotalCount}条记录&nbsp;
			到第<input value="" name="pn" id="pn_input"/>页
			<input id="searchPageBtn" type="button" value="确定">
				<script type="text/javascript">
					$(function (){
						$("#searchPageBtn").click(function (){
							var pageNo = $("#pn_input").val();
							location.href = "${pageScope.basePath}manager/bookServlet?action=page&pageNo=" + pageNo;
						});
					});
				</script>
		</div>--%>
		<%@include file="/pages/common/page.jsp"%>
	</div>

	<%@include file="/pages/common/footer.jsp"%>
</body>
</html>