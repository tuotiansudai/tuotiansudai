<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.loan_list}" pageJavascript="${m_js.loan_list}" title="出借列表_出借产品_拓天速贷">

<div class="my-loan-content" id="loanList">
    <div class="menu-category">
        <span><a href="/m/loan-list">直投项目</a></span>
        <span class="current"><a>转让项目</a></span>
        <em class="notice-tip-btn"></em>
    </div>

    <div id="wrapperOut" class="loan-list-frame">
        <div class="loan-list-content">
            <div class="category-box-main">
                <#list transferApplicationItemList as transferApplicationItem>
                <div class="target-category-box transferLi" data-url="/m/transfer/${(transferApplicationItem.transferApplicationId)!}">
                    <b class="newer-title">${transferApplicationItem.name!}</b>
                    <ul class="loan-info clearfix">
                        <li><span class="percent-number <#if (transferApplicationItem.transferStatus == "SUCCESS")>colorChange</#if>"> <i>
                    <@percentInteger>${transferApplicationItem.baseRate+transferApplicationItem.activityRate}</@percentInteger><@percentFraction>${transferApplicationItem.baseRate+transferApplicationItem.activityRate}</@percentFraction>
                        </i><b class="percentSmall">%</b></span><em class="note">约定年化利率</em></li>
                        <li><em class="duration-day">${transferApplicationItem.leftDays!}</em> 天 <em class="note">剩余天数</em></li>
                        <#if (transferApplicationItem.transferStatus == "SUCCESS")>
                            <li><a href="/m/transfer/${(transferApplicationItem.transferApplicationId)!}" class="tranfered"></a></li>
                        <#else>
                            <li><a data-url="/m/transfer/${(transferApplicationItem.transferApplicationId)!}" class="btn-invest btn-normal goToTranDetail">立即出借</a></li>
                        </#if>

                    </ul>
                    <div class="table-row progress-column">
                        <span class="p-title">
                            <i class="price <#if (transferApplicationItem.transferStatus == "SUCCESS")>colorChange</#if>">转让价格：<em class="money"><@percentInteger>${transferApplicationItem.transferAmount!}</@percentInteger><@percentFraction>${transferApplicationItem.transferAmount!}</@percentFraction></em>元</i>/<em class="money"><@percentInteger>${transferApplicationItem.investAmount!}</@percentInteger><@percentFraction>${transferApplicationItem.investAmount!}</@percentFraction></em>元(原)
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
                    <span>出借</span>
                </a>
                <a class="menu-knowledge" href="/m/about/knowledge">
                    <i></i>
                    <span>发现</span>
                </a>
                <a class="menu-my" href="/m/account">
                    <i></i>
                    <span>我的</span>
                </a>
            </div>
        </div>
</div>
</@global.main>
