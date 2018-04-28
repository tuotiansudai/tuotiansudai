<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'invest_detail' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.invest_detail}" pageJavascript="${js.invest_detail}" title="投资详情">

<div class="my-account-content amount-detail" id="wrapperOut" >

    <div class="amount-detail-inner" >
        <ul class="input-list">
            <li>
                <label>投资金额</label>
                <em>51.82元</em>
            </li>

            <li>
                <label>预期总收益</label>
                <em>1.38元</em>
            </li>
            <li>
                <label>已收回款</label>
                <em>53.20元</em>
            </li>
            <li>
                <label>待收回款</label>
                <em>0.00元</em>
            </li>
            <li>
                <label>起息日</label>
                <em>2016/09/19</em>
            </li>
            <li>
                <label>到期日</label>
                <em>2016/12/30</em>
            </li>
        </ul>

        <dl class="payment-plan">
            <dt>回款计划</dt>
            <dd>
                <span>2016/10/18</span>
                <span>0.46元</span>
                <em class="status">完成</em>
            </dd>
            <dd>
                <span>2016/10/18</span>
                <span>0.46元</span>
                <em class="status">完成</em>
            </dd>
            <dd>
                <span>2016/10/18</span>
                <span>0.46元</span>
                <em class="status">完成</em>
            </dd>
        </dl>

        <ul class="input-list">
            <li>
                <label>约定年化利率</label>
                <em>11.0%</em>
            </li>

            <li>
                <label>项目期限</label>
                <em>90天</em>
            </li>
            <li>
                <label>所用优惠券</label>
                <em> 0.5%加息券</em>
            </li>
            <li>
                <label>会员优惠</label>
                <em><i class="membership-level v0"></i>服务费九折</em>
            </li>
            <li>
                <label>购买日期</label>
                <em>2016/12/20</em>
            </li>

            <li>
                <label>产品详情</label>
                <a>
                    <i class="fa fa-angle-right"></i>
                </a>
            </li>

            <li>
                <label>转让须知</label>
                <a href="notice-transfer.ftl">
                    <i class="fa fa-angle-right"></i>
                </a>
            </li>
        </ul>
    </div>
</div>


<div class="apply-transfer" id="applyTransfer">
    <#--<a href="#" data-status="apply">申请转让</a>-->
    <a href="#" data-status="cancel" data-transfer-application-id="<%=item.transferApplicationId%>">取消转让</a>

</div>

<div  id="closeTransfer" class="close-transfer" style="display: none">
    <b class="pop-title">温馨提示</b>
    <p>您确认取消该笔债权的转让？</p>
</div>
</@global.main>
