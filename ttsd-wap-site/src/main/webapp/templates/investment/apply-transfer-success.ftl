<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'apply_transfer' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.apply_transfer}" pageJavascript="" title="申请转让">

<div class="my-account-content apply-transfer-success" >

    <i class="icon-success"></i>
    <em>转让申请成功 <br/>债权申请日起，5日内无人接手则转让失败。</em>
    <a href="#" class="btn-wap-normal" >确定</a>


</div>
</@global.main>
