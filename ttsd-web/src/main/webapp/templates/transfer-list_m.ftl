<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.loan_list}" pageJavascript="${m_js.loan_list}" title="投资列表_投资产品_拓天速贷">

<div class="my-loan-content" id="loanList">
    <div class="menu-category">
        <span><a href="/m/loan-list">直投项目</a></span>
        <span class="current"><a>转让项目</a></span>
    </div>

    <div id="wrapperOut" class="loan-list-frame">
        <div class="loan-list-content">
            <div class="category-box-main">
                <#list transferApplicationItemList as transferApplicationItem>
                <div class="target-category-box transferLi" data-url="/m/transfer/${(transferApplicationItem.transferApplicationId)!}">
                    <b class="newer-title">${transferApplicationItem.name!}</b>
                    <ul class="loan-info clearfix">
                        <li><span class="percent-number <#if (transferApplicationItem.transferStatus == "SUCCESS")>colorChange</#if>"> <i>${transferApplicationItem.baseRate!}</i><b class="percentSmall">%</b></span><em class="note">预期年化收益</em></li>
                        <li><em class="duration-day">${transferApplicationItem.leftDays!}</em> 天 <em class="note">剩余天数</em></li>
                        <#if (transferApplicationItem.transferStatus == "SUCCESS")>
                            <li><a href="/m/transfer/${(transferApplicationItem.transferApplicationId)!}" class="tranfered"></a></li>
                        <#else>
                            <li><a href="/m/transfer/${(transferApplicationItem.transferApplicationId)!}" class="btn-invest btn-normal">立即投资</a></li>
                        </#if>

                    </ul>
                    <div class="table-row progress-column">
                        <span class="p-title">
                            <i class="price <#if (transferApplicationItem.transferStatus == "SUCCESS")>colorChange</#if>">转让价格：<@percentInteger>${transferApplicationItem.transferAmount!}</@percentInteger><@percentFraction>${transferApplicationItem.transferAmount!}</@percentFraction>元</i>/<@percentInteger>${transferApplicationItem.investAmount!}</@percentInteger><@percentFraction>${transferApplicationItem.investAmount!}</@percentFraction>元(原)
                        </span>
                    </div>
                </div>
                </#list>
            </div>
            <div id="pullUp">
                <span class="pullUpLabel">上拉加载更多</span>
            </div>
    </div>
        <div class="loan-list">
            <div class="footer-wap-container">
                <a class="menu-home" href="/m">
                    <i></i>
                    <span>首页</span>
                </a>
                <a class="menu-invest current" href="/m/loan-list">
                    <i></i>
                    <span>投资</span>
                </a>
                <a class="menu-my">
                    <i></i>
                    <span>我的</span>
                </a>
            </div>
        </div>
</div>
</@global.main>
