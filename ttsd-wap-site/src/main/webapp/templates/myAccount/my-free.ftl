<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'my_free' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.my_free}" pageJavascript="${js.my_free}" title="我的体验金">


<div class="my-account-content amount-overview" id="couponList">
    <dl class="free-container">
        <dt>什么是体验金？</dt>
        <dd>1、体验金是由拓天速贷提供给平台客户用来投资拓天体验金项目的本金，有效期为3天，50元起投。</dd>
        <dd>2、拓天体验金项目仅限用户使用体验金投资，项目到期后，平台回收体验金，收益归用户所有。</dd>
        <dd>3、体验金不能转出，但体验金投资产生的收益可以提现。</dd>
        <dd>4、用户首次提现体验金投资所产生的收益时，需要投资其他定期项目（债权转让项目除外）累计满1000元才可以提现。</dd>
        <dd>5、新注册用户可以获得6888元体验金。</dd>
        <dd>6、关注并参与平台活动可获取更多体验金。</dd>
        <dd>本活动规则解释权归拓天速贷所有，如有疑问请联系在线客服或拨打400-169-1188</dd>
    </dl>
</div>
</@global.main>
