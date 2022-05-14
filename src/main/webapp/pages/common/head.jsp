<%--
  Created by IntelliJ IDEA.
  User: emg
  Date: 2022/5/14
  Time: 15:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page
        contentType="text/html;charset=UTF-8"
        language="java" %>

<%--需要动态的写--%>
<%--<base href="http://localhost:8080/bookStore_war_exploded/">--%>

<% String basepath = request.getScheme()
        +"://"
        + request.getServerName()
        + ":"
        + request.getServerPort()
        + request.getContextPath()
        + "/";
%>

<base href="<%=basepath%>">
<link type="text/css" rel="stylesheet" href="static/css/style.css" >
<script type="text/javascript" src="static/script/jquery-1.7.2.js"></script>