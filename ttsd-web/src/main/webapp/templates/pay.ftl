<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="正在载入">
    <#if pay??>
        <div class="callBack_container">
            <div class="loading-wrap">
                <div class="loading"></div>
                <#if pay.status>
                    <p>正在载入...</p>
                <#else>
                    <p>${pay.message}</p>
                </#if>
            </div>
        </div>

        <#if pay.status>
            <form id="payForm" action="${pay.url}" method="post">
                <input type="hidden" name="reqData" value='${pay.data}'/>
            </form>

            <script type="text/javascript">
                window.onload = function () {
                    document.getElementById('payForm').submit()
                }
            </script>
        </#if>

    </#if>
</@global.main>