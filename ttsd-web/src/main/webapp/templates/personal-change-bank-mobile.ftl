<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.invest_success}" pageJavascript="${js.account_success}" activeLeftNav="" title="正在载入">
<form id="payForm" action="/personal-info/change-bank-mobile" method="post">
    原手机号: <input name="originMobile" vlaue="${originMobile}"/></br>
    新手机号: <input name="newMobile" vlaue="" /></br>
    是否可用: <input checked="checked" type="radio" name="type" value="1"/>
    <input checked="checked" type="radio" name="type" value="2"/>
</form>

<script type="text/javascript">
    window.onload = function () {
        document.getElementById('payForm').submit()
    }
</script>
</@global.main>