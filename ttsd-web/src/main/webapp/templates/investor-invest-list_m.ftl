<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.investor_invest_list}" pageJavascript="${m_js.investor_invest_list}" title="我的投资">


<div class="my-account-content invest-detail" id="myInvest">
    <div class="m-header"><em id="iconMyInvest" class="icon-left"><i class="fa fa-angle-left"></i></em>我的投资 </div>
    <div class="menu-category">
        <span class="current"><a href="javascript:;">回款中</a></span>
        <span><a href="javascript:;">投标中</a></span>
    </div>
    <div id="wrapperOut" class="invest-list-wrap">
        <div class="invest-list-box">
            <div class="invest-item">
                <div class="top clearfix">
                    <dl class="dl-l clearfix">
                        <dt><em>128.05</em>元</dt>
                        <dd>预期收益</dd>
                    </dl>
                    <dl class="dl-r clearfix">
                        <dt><em></em> <i>2017-10-24到期</i></dt>
                        <dd>投资金额 <em>100,000.00</em>元
                        </dd>
                    </dl>
                </div>
                <dl class="bottom clearfix">
                    <dt>2房产抵押借款17118</dt>
                    <dd>
                        <i class="icon-sign">可转让</i> <i class="icon-sign">红包</i> <i class="icon-sign">拓天标王</i>
                    </dd>
                </dl>

            </div>


        </div>
    </div>


</div>
</@global.main>
