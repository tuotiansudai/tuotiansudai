<!DOCTYPE html>
<html>
<body>
<div class="main">
    <div class="hd">
        修改密码 ${loginName!}
    </div>
    <form method="post">
        <div class="item-block">
            <label for="">请输入原密码：</label>
            <input class="phone-txt jq-ps-2" name="oldPassword" type="password" placeholder="请输入原密码"/>
        </div>
        <div class="item-block">
            <label for="">请输入新密码：</label>
            <input class="phone-txt jq-ps-1" name="newPassword" type="password" placeholder="请输入新密码"/>
        </div>
        <div class="item-block">
            <label for="">请确认新密码：</label>
            <input class="phone-txt jq-ps-2" name="newPasswordCheck" type="password" placeholder="请确认新密码"/>
        </div>
        <div class="item-block">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn-send-form grey">修改密码</button>
        </div>
    </form>
</div>
</body>
</html>