<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'buy_free' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.buy_free}" pageJavascript="${js.buy_free}" title="体验金购买详情">

<div class="my-account-content apply-transfer"  id="applyTransfer">

    <div class="benefit-box">
        <div class="target-category-box" data-url="loan-transfer-detail.ftl">
            <b class="newer-title">房产抵押借款17070</b>
            <ul class="loan-info clearfix">
                <li>
                    <span>
                        <i>3天</i>
                    </span>
                    <em>项目期限</em>
                </li>
                <li>
                    <span>
                        <i>15%</i>
                    </span>
                    <em>预期年化收益</em>
                </li>
                <li>
                    <span>
                        <i>12.30元</i>
                    </span>
                    <em>万元收益</em>
                </li>
            </ul>
        </div>
        <div class="bg-square-box"></div>
    </div>
    <form id="investForm">
    <div class="input-amount-box">
        <ul class="input-list">
            <li>
                <label>投资金额</label>
                <input type="text" value="" name="price" class="input-amount" placeholder="请输入金额">
                <em>元</em>
            </li>
            <li class="mt-10">
                <label>预期收益</label>
                <span class="number-text"><strong>0</strong>元</span>
            </li>
        </ul>
    </div>

    <button type="submit" class="btn-wap-normal" disabled>确定购买</button>

    </form>


    <div class="transfer-notice">

        <b>温馨提示:</b>
        用户首次提现体验金投资所产生的收益时，需要投资其他定期项目（债权转让项目除外）累计满1000元才可以提现。
    </div>


</div>

</@global.main>
