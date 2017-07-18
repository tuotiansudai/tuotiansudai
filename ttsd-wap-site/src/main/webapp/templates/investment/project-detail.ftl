<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'project_detail' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.project_detail}" pageJavascript="${js.project_detail}" title="项目详情">

<div class="project-detail" id="projectDetail">
    <div class="menu-category">
        <span class="current"><a href="#">项目材料</a></span>
        <span><a href="#">交易记录</a></span>
    </div>

    <div id="wrapperOut" class="loan-list-frame">
        <div class="loan-list-content" >
            <#include 'mortgage-kind.ftl'>
        </div>

    </div>

</div>


</@global.main>
