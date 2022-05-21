<%@ taglib
        prefix="c"
        uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page
        contentType="text/html;charset=UTF-8"
        language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>
        我的订单</title>
    <%--静态包含base，css文件，jquery--%>
    <%@include
            file="/pages/common/head.jsp" %>
    <style type="text/css">
        h1 {
            text-align: center;
            margin-top: 200px;
        }
    </style>
</head>
<body>

<div id="header">
    <img class="logo_img"
         alt=""
         src="static/img/logo.gif">
    <span class="wel_word">我的订单</span>
    <%@include
            file="/pages/common/login_success_menu.jsp" %>
    >
</div>

<div id="main">

    <table>
        <tr>

            <td>日期</td>
            <td>时间</td>
            <td>金额</td>
            <td>状态</td>
            <td>详情</td>
        </tr>

        <c:if test="${empty requestScope.myOrders}">
            <tr>
                <td colspan="5"><a href="index.jsp">亲，当前没有订单！快跟小伙伴们去浏览商品吧！！！</a>
                </td>
            </tr>
        </c:if>

        <c:forEach items="${requestScope.myOrders}" var="order">
            <tr>

                <td>${order.createTime.toLocalDate()}</td>
                <td>${order.createTime.toLocalTime()}</td>
                <td>${order.price}</td>
                <td>
                    <c:choose>
                        <c:when test="${order.status == 0}">
                            未发货
                        </c:when>
                        <c:when test="${order.status == 1}">
                            <a href="clientOrderServlet?action=receiveOrder&orderId=${order.orderId}">确认收货</a>
                        </c:when>
                        <c:otherwise>
                            已签收
                        </c:otherwise>
                    </c:choose>
                </td>
                <td><a href="#">查看详情</a></td>
            </tr>
        </c:forEach>



    </table>


</div>

<%@include
        file="/pages/common/footer.jsp" %>
</body>
</html>