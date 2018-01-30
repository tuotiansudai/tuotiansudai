<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.investor_invest_list}" pageJavascript="${m_js.investor_invest_list}" title="我的投资">

<div class="my-account-content" id="myInvest">
    <div class="m-header"><em id="iconMyInvest" class="icon-left"><i></i></em>我的投资 </div>
    <div class="menu-category">
        <span class="current" id="repayingBtn"><a href="javascript:;">回款中</a></span>
        <span id="raisingBtn"><a href="javascript:;">投标中</a></span>
    </div>
    <div id="wrapperOut" class="invest-list-wrap">
        <div class="invest-list-box">

            <div class="main">
                <script type="text/html" id="investListTpl">
                    {{if records.length}}
                    {{each records as value index}}
                    <div class="invest-item" data-loan-id="{{value.loanId}}" data-invest-id="{{value.investId}}">
                        <div class="top clearfix">
                            <dl class="dl-l clearfix">
                                <dt><em class="money">{{value.expectedInterest}}</em>元</dt>
                                <dd>预期收益</dd>
                            </dl>
                            <dl class="dl-r clearfix">
                                <dt><em></em> <i>{{value.lastRepayDate}}到期</i></dt>
                                <dd>投资金额 <em>{{value.investAmount}}</em>元
                                </dd>
                            </dl>
                        </div>
                        <dl class="bottom clearfix">
                            <dt>{{value.loanName}}</dt>
                            <dd>

                                {{if value.isTransferInvest}} <i class="icon-sign">可转让</i>{{/if}} {{if value.usedRedEnvelope}}<i class="icon-sign">红包</i> {{/if}}{{if value.usedCoupon}}<i class="icon-sign">券</i>{{/if}}
                                {{if value.achievements.length}}
                                {{each value.achievements as achiveItem achiveIndex}}
                                {{if achiveItem == 'FIRST_INVEST'}}
                                <i class="icon-sign">拓荒先锋</i>
                                {{/if}}
                                {{if achiveItem == 'MAX_AMOUNT'}}
                                <i class="icon-sign">拓天标王</i>
                                {{/if}}
                                {{if achiveItem == 'LAST_INVEST'}}
                                <i class="icon-sign">一锤定音</i>
                                {{/if}}
                                {{/each}}
                                {{/if}}
                            </dd>
                        </dl>

                    </div>
                    {{/each}}
                    {{/if}}
                </script>
            </div>
            <div id="noData" style="display: none">没有更多数据了</div>

        </div>

    </div>


</div>
</@global.main>
