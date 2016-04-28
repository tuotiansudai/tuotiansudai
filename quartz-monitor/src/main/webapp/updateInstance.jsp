<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<div class="pageContent">
	<form method="post" action="<%=request.getContextPath()%>/quartz/update.action" class="pageForm required-validate" onsubmit="return validateCallback(this,dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
			<div class="unit">
				<label>主机ip：</label>
				<input name="uuid" class="required" type="hidden" size="30"  value="${uuid }"/>
				<input name="host" class="required" type="text" size="30"  value="${host }"/>
			</div>
			<div class="unit">
				<label>主机端口：</label>
				<input type="text" class="required" name="port"  value="${port }"/>
			</div>
			<div class="unit">
				<label>用户名：</label>
				<input type="text" name="username"  class="textInput" alt="请输入用户名"  value="${username }">
			</div>
			<div class="unit">
				<label>密码：</label>
				<input name="password" class="digits" type="text" size="30" alt="请输入数字"  value="${password }"/>
			</div>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
