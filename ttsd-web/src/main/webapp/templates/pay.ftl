<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="成功">
<#if (pay.data)??>
    <#assign payData = pay.data>

<div class="callBack_container">
    <div class="loading-wrap">
        <div class="loading"></div>
        <p>正在载入...</p>
    </div>
</div>
    <#if payData.status>
    <form id="payForm" action="${payData.url}" method="post">
        <#list payData.fields?keys as key>
            <input type="hidden" name="${key}" value='${payData.fields[key]}'/>
        </#list>
    </form>
    <#else>
    <p>${payData.message!}</p>
    </#if>

</#if>

    <#if payData.status>
    <script type="text/javascript">
    window.onload=function()  {
        document.getElementById('payForm').submit()
    }
    </script>
    </#if>

</@global.main>