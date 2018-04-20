<#import "macro/global-dev.ftl" as global>

<#--定义jsName，这里test_demo为ftl 文件引用得 jsx文件名,css和js同名,如果需要模拟 只需要修改这个名字就行-->

<#assign jsName = 'test_demo' >

<#assign js = {"${jsName}":"http://localhost:3008/web/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/web/js/${jsName}.css"}>

<#--说明：在后端还没有做指向页面得时候，我们可以先根据这个模版访问页面，完成前端得工作，包括调用接口，
<#--只需要提前与后台沟通好数据结构跟关键字，前端页面做完后，如果后台工作人员给前端提供了ftl的访问链接，我们需要做的事情如下-->

<#--1. 我们停止我们的ftl-server服务，访问后端给的链接-->
<#--2. 并且删除上面的assign定义语句，如果在做页面的时候自己通过assign定义假数据了，一并注销或删除-->
<#--3. 把import后面的文件改成global.ftl,即  替换成 <#import "macro/global.ftl" as global>-->

<#--访问地址：http://localhost:3010/test-demo.ftl-->

<#--安装方法：-->
<#--npm install -g node-gyp-->
<#--node-gyp install ftl-server -g-->

<@global.main pageCss="${css.test_demo}" pageJavascript="${js.test_demo}" activeLeftNav="" title="登录拓天速贷_拓天速贷" keywords="拓天速贷,拓天会员,新手投资,拓天速贷用户" description="拓天速贷为投资投资人士提供规范、安全、专业的互联网金融信息服务,让您获得稳定收益和高收益的投资产品.">

<div class="login-container page-width" id="loginContainer">

    这里是内容

</div>
</@global.main>

