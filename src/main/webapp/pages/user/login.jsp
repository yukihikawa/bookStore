<%--
  Created by IntelliJ IDEA.
  User: emg
  Date: 2022/5/13
  Time: 19:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page
        contentType="text/html;charset=UTF-8"
        language="java" %>
<html>
<head>
    <%@include
            file="/pages/common/head.jsp" %>

    <meta charset="UTF-8">
    <title>
        尚硅谷会员登录页面</title>
</head>
<body>
<div id="login_header">
    <img class="logo_img"
         alt=""
         src="static/img/logo.gif">
</div>

<div class="login_banner">

    <div id="l_content">
        <span class="login_word">欢迎登录</span>
    </div>

    <div id="content">
        <div class="login_form">
            <div class="login_box">
                <div class="tit">
                    <h1>
                        尚硅谷会员</h1>
                    <a href="pages/user/regist.jsp">立即注册</a>
                </div>
                <div class="msg_cont">
                    <b></b>
                    <span class="errorMsg">
                        <%=request.getAttribute("msg") == null ? "请输入用户名和密码" : request.getAttribute("msg")%>
                    </span>
                </div>
                <div class="form">
                    <form action="loginServlet"
                          method="post">
                        <label>用户名称：</label>
                        <input class="itxt"
                               type="text"
                               placeholder="请输入用户名"
                               autocomplete="off"
                               tabindex="1"
                               name="username"
                               value="<%=request.getAttribute("username")==null?"":request.getAttribute("username")%>"
                               />
                        <br/>
                        <br/>
                        <label>用户密码：</label>
                        <input class="itxt"
                               type="password"
                               placeholder="请输入密码"
                               autocomplete="off"
                               tabindex="1"
                               name="password"/>
                        <br/>
                        <br/>
                        <input type="submit"
                               value="登录"
                               id="sub_btn"/>
                    </form>
                </div>

            </div>
        </div>
    </div>
</div>
<%@include
        file="/pages/common/footer.jsp" %>
</body>
</html>