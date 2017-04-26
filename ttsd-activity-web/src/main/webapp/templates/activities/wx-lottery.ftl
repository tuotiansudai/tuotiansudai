<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'wx_lottery' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.css"}>




<@global.main pageCss="${css.wx_lottery}" pageJavascript="${js.wx_lottery}" activeNav="" activeLeftNav="" title="拓天速贷注册_用户注册_拓天速贷" keywords="拓天速贷,拓天速贷会员,拓天速贷注册，用户注册" description="拓天速贷会员注册为您提供规范、专业、安全有保障的互联网金融信息服务.">
<div class="wx-lottery-container">
	
</div>
</@global.main>