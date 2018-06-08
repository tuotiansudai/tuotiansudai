<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.bank_callback}" pageJavascript="${m_js.bank_callback}" title="正在载入">
    <#if pay??>
    <div class="my-account-content personal-profile">
        <#if pay.status>
            <div class="m-header">正在载入...</div>
            <div class="gif-container">
            </div>
        <#else>
            <div class="m-header">交易失败</div>
            <div class="gif-container">
            </div>
            <p>${pay.message!('交易失败，请联系客服！')}</p>
        </#if>


    </div>
    </#if>

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

</@global.main>


