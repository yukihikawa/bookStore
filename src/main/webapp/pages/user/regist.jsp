<%--
  Created by IntelliJ IDEA.
  User: emg
  Date: 2022/5/13
  Time: 19:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page
        contentType="text/html;charset=UTF-8"
        language="java" %>
<html>
<head>

    <meta charset="UTF-8">
    <title>
        尚硅谷会员注册页面</title>
    <%--静态包含base，css文件，jquery--%>
    <%@include
            file="/pages/common/head.jsp" %>
    <style type="text/css">
        .login_form {
            height: 420px;
            margin-top: 25px;
        }
    </style>


    <script type="text/javascript">
        //页面加载，绑定单击事件
        $(function () {
            $("#sub_btn").click(function () {
                //验证用户名：必须由字母，数字下划线组成，并且长度为 5 到 12 位
                //获取输入框
                var usernameText = $("#username").val();
                //正则对象
                var usernamePat = /^\w{5,12}$/;
                //test()方法
                if (!usernamePat.test(usernameText)) {
                    $("span.errorMsg").text("用户名不合法！");
                    return false;
                }
                //验证密码：必须由字母，数字下划线组成，并且长度为 5 到 12 位
                var passwordText = $("#password").val();
                //正则对象
                var passwordPat = /^\w{5,12}$/;
                //test()方法
                if (!passwordPat.test(passwordText)) {
                    $("span.errorMsg").text("密码不合法！");
                    return false;
                }
                //验证确认密码：和密码相同
                var repwd = $("#repwd").val();
                if (repwd != passwordText) {
                    $("span.errorMsg").text("与密码不相同！");
                    return false;
                }
                //邮箱验证：xxxxx@xxx.com
                var emailText = $("#email").val();
                var emailPat = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
                if (!emailPat.test(emailText)) {
                    $("span.errorMsg").text("邮箱不合法！");
                    return false;
                }
                //验证码：现在只需要验证用户已输入。因为还没讲到服务器。验证码生成。*/
                var codeText = $("#code").val();
                //去空格
                codeText = $.trim(codeText);
                if (codeText === null || codeText === "") {
                    $("span.errorMsg").text("验证码为空！");
                    return false;
                }

                $("span.errorMsg").text("");

            });
        });
    </script>
</head>
<body>
<div id="login_header">
    <img class="logo_img"
         alt=""
         src="static/img/logo.gif">
</div>

<div class="login_banner">

    <div id="l_content">
        <span class="login_word">欢迎注册</span>
    </div>

    <div id="content">
        <div class="login_form">
            <div class="login_box">
                <div class="tit">
                    <h1>
                        注册尚硅谷会员</h1>
                    <span class="errorMsg">
                        ${requestScope.msg}
                    </span>
                </div>
                <div class="form">
                    <form action="register"
                          method="post">
                        <input type="hidden" name="action" value="regist">

                        <label>用户名称：</label>
                        <input class="itxt"
                               type="text"
                               placeholder="请输入用户名"
                               value="${requestScope.username}"
                               autocomplete="off"
                               tabindex="1"
                               name="username"
                               id="username"
                        />
                        <br/>
                        <br/>
                        <label>用户密码：</label>
                        <input class="itxt"
                               type="password"
                               placeholder="请输入密码"
                               value="123456"
                               autocomplete="off"
                               tabindex="1"
                               name="password"
                               id="password"/>
                        <br/>
                        <br/>
                        <label>确认密码：</label>
                        <input class="itxt"
                               type="password"
                               placeholder="确认密码"
                               value="123456"
                               autocomplete="off"
                               tabindex="1"
                               name="repwd"
                               id="repwd"/>
                        <br/>
                        <br/>
                        <label>电子邮件：</label>
                        <input class="itxt"
                               type="text"
                               placeholder="请输入邮箱地址"
                               value="${requestScope.email}"
                               autocomplete="off"
                               tabindex="1"
                               name="email"
                               id="email"/>
                        <br/>
                        <br/>
                        <label>验证码：</label>
                        <input class="itxt"
                               type="text"
                               name="code"
                               style="width: 150px;"
                               id="code"
                               value=""/>
                        <img alt=""
                             id="kaptcha"
                             src="kaptcha.jpg"
                             style="float: right; margin-right: 40px; width: 100px; height: 28px">
                        <script type="text/javascript">
                            $("#kaptcha").click(function (){
                                this.src="kaptcha.jpg?d=" + new Date();
                            });
                        </script>
                        <br/>
                        <br/>
                        <input type="submit"
                               value="注册"
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
