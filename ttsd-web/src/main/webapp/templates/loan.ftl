<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.loan_detail}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="loan-detail-content">
    <div class="borderBox bg-w clearfix">
        <div class="news-share fl">
            <h2 class="title hd" data-loan-status="${loan.loanStatus}">${loan.name}</h2>
            <div class="chart-box">
                <div class="box" title="已投${loan.progress?string("0.00")}%">
                    <div class="bg"></div>
                    <div class="rount"></div>
                    <div class="bg2"></div>
                    <div class="rount2" style="display: none;"></div>
                    <div class="pr-square-in">
                        <em>
                            <b><@percentInteger>${loan.basicRate}</@percentInteger><@percentFraction>${loan.basicRate}</@percentFraction></b>
                            <#if loan.activityRate!=0>+<@percentInteger>${loan.activityRate}</@percentInteger><@percentFraction>${loan.activityRate}</@percentFraction></#if>%
                        </em>
                        <i>年化收益</i>
                    </div>
                </div>
            </div>
            <div class="chart-info">
                项目金额：<@amount>${loan.loanAmount}</@amount>元<br/>
                代理人：${loan.agentLoginName}<br/>
                借款人：${loan.loanerLoginName} <br/>
                项目期限：${loan.periods}<#if loan.type.getLoanPeriodUnit() == "MONTH">月 <br/></#if>
                <#if loan.type.getLoanPeriodUnit() == "DAY">项目期限：${loan.periods}天 <br/></#if>
                还款方式：${loan.type.getName()}<br/>
                投资要求：<@amount>${loan.minInvestAmount}</@amount>元起投，投资金额为<@amount>${loan.investIncreasingAmount}</@amount>的整数倍<br/>
                <a href="${staticServer}/pdf/loanAgreementSample.pdf" target="_blank">借款协议样本</a>
            </div>
            <#if loan.productType??>
                <div class="product-type-text">${loan.productType.getName()}</div><!--还有稳盈绣/久盈富-->
            </#if>
        </div>
        <div class="account-info fl">
            <h5 class="l-title">拓天速贷提醒您：理财非存款，投资需谨慎！</h5>
            <#if loan.loanStatus == "RAISING">
                <form action="/invest" method="post">
                    <dl class="account-list">
                        <dd><span class="fl">可投金额：</span><em class="fr"><i class="amountNeedRaised-i">${loan.amountNeedRaised?string("0.00")}</i> 元</em></dd>
                        <dd><span class="fl">账户余额：</span><em class="fr account-amount">${loan.balance?string("0.00")} 元</em></dd>
                        <dd><span class="fl">每人限投：</span><em class="fr">${loan.maxInvestAmount} 元</em></dd>
                        <dd class="invest-amount tl"><#assign defaultInvestAmount = loan.maxAvailableInvestAmount!>
                            <#if investAmount?has_content>
                                <#assign defaultInvestAmount = investAmount>
                            </#if>
                            <span class="fl">投资金额：</span>
                            <input type="text" name="amount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00" value="${defaultInvestAmount}"
                                   class="text-input-amount fr position-width"/>
                            <#if errorMessage?has_content>
                                <span class="errorTip hide"><i class="fa fa-times-circle"></i>${errorMessage!}</span>
                            </#if>
                        </dd>
                        <dd class="experience-ticket">
                            <span class="fl">体验券</span>
                            <div class="fr experience-ticket-box">
                                <em class="experience-ticket-input disabled" id="use-experience-ticket">
                                    <span>当前我无可用体验券哦</span>
                                    <i class="fa fa-sort-down fr"></i>
                                    <i class="fa fa-sort-up hide fr"></i>
                                </em>
                                <ul class="ticket-list hide">
                                    <li><input type="radio" name="ticket" value="1" id="ticketList1"><label for="ticketList1"><i class="ticket-title"> 不使用体验券</i></label></li>
                                    <li><input type="radio" name="ticket" value="2" id="ticketList2" class="input-use-ticket">
                                        <label for="ticketList2">
                                            <span class="sign">新手</span>
                                            <span class="ticket-info">
                                                <i class="ticket-title" data-income="1000">新手体验券1000元</i><br/>
                                                <i class="ticket-term">[投资满1000元可用]</i>
                                            </span></label></li>
                                    <li class="disabled"><input type="radio" name="ticket" value="3" id="ticketList3" class="input-use-ticket" disabled>
                                        <label for="ticketList3">
                                            <span class="sign">满抵</span>
                                            <span class="ticket-info">
                                               <i class="ticket-title">体验券1000元</i><br/>
                                                <i class="ticket-term">[投资满1000元可用]</i>
                                            </span>
                                        </label></li>
                                </ul>
                            </div>
                        </dd>
                        <dd class="experience-revenue hide"><span class="fl">体验劵预期收益：</span><em class="fr"><i class="experience-interest"></i>元</em></dd>

                        <dd class="expected-interest-dd tc">
                            <span>预计总收益：</span>
                            <span class="principal-income">23.25</span>
                            <span class="experience-income">+12.38</span>元
                        </dd>
                        <dd>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input class="hid-loan" type="hidden" name="loanId" value="${loan.id?string("0")}"/>
                            <button class="btn-pay btn-normal" type="submit">马上投资</button>
                        </dd>

                    </dl>
                </form>
            </#if>
            <#if loan.loanStatus == "PREHEAT">
                <form action="/invest" method="post">
                    <dl class="account-list">
                        <dd><span class="fl">可投金额：</span><em class="fr"><i class="amountNeedRaised-i">${loan.amountNeedRaised?string("0.00")}</i>元</em>
                        <dd><span class="fl">账户余额：</span><em class="fr account-amount">${loan.balance?string("0.00")} 元</em></dd>
                        <dd><span class="fl">每人限投：</span><em class="fr">${loan.maxInvestAmount}元</em></dd>
                        <dd class="invest-amount tl">
                            <#assign defaultInvestAmount = loan.maxAvailableInvestAmount!>
                            <#if investAmount?has_content>
                                <#assign defaultInvestAmount = investAmount>
                            </#if>
                            <input type="text" name="amount" data-d-group="4" data-l-zero="deny" data-v-min="0.00" placeholder="0.00" value="${defaultInvestAmount}" class="text-input-amount"/>
                            <#if errorMessage?has_content>

                                <span class="error"><i class="fa fa-times-circle"></i>${errorMessage!}</span>
                            <#else >
                                <span class="error" style="display: none"><i class="fa fa-times-circle"></i></span>
                            </#if>
                        </dd>
                        <#list coupons as coupon>
                            <dd class="experience-ticket">
                                <span class="fl"><i class="fa fa-money"></i> ${coupon.name}<@amount>${coupon.amount?string(0)}</@amount>元</span>
                                <em class="fr"><label for="use-experience-ticket"> 使用体验劵</label> <input type="checkbox" id="use-experience-ticket" name="userCouponId" value="${coupon.id?string('0')}"
                                                                                                        data-amount="${(coupon.investQuota/100)?string('0.00')}"/>
                                </em>
                            </dd>
                            <dd class="experience-revenue hide"><span class="fl">体验劵预期收益：</span><em class="fr"><i class="experience-interest"><@amount>${coupon.interest?string(0)}</@amount></i>元</em>
                            </dd>
                        </#list>
                        <dd class="time-item">
                            <#if loan.preheatSeconds lte 1800>
                                <i class="time-clock"></i>
                                <strong id="minute_show">00</strong>
                                <em>:</em>
                                <strong id="second_show">00</strong>
                                以后可投资
                            <#else >
                            ${(loan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}放标
                            </#if>
                        </dd>
                        <dd class="hide">
                            <span class="fl">预计总收益：</span><em class="fr"><i class="expected-interest"></i>元</em>
                        </dd>
                        <dd><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input class="hid-loan" type="hidden" name="loanId" value="${loan.id?string("0")}"/>
                            <button class="btn-pay btn-normal" type="submit" disabled="disabled">预热中</button>
                        </dd>
                    </dl>
                </form>
            </#if>
            <#if ["REPAYING", "RECHECK", "CANCEL", "OVERDUE", "COMPLETE"]?seq_contains(loan.loanStatus)>
                <form action="/loan-list" method="get">
                    <dl class="account-list">
                        <dd class="img-status">
                            <img src="${staticServer}/images/sign/loan/${loan.loanStatus?lower_case}.png" alt=""/>
                        </dd>
                        <dd>
                            <button class="btn-pay btn-normal" type="submit">查看其他项目</button>
                        </dd>
                    </dl>
                </form>
            </#if>
        </div>
        <div class="bg-w clear-blank">
            <div class="loan-nav">
                <ul>
                    <li class="active">借款详情<i class="fa fa-caret-up"></i></li>
                    <li>出借记录<i class="fa fa-caret-up"></i></li>
                </ul>
            </div>
            <div class="loan-list pad-s">
                <div class="loan-list-con">
                    <div class="borderBox">
                        <h3 class="b-title">借款详情：</h3>
                    ${loan.descriptionHtml}
                    </div>

                    <div class="loan-material">
                        <h3>申请材料：</h3>
                        <div class="pic-list" id="picListBox">
                            <#list loan.loanTitleDto as loanTitle>
                                <#list loan.loanTitles as loanTitleRelation >
                                    <#if loanTitle.id == loanTitleRelation.titleId>
                                        <div class="title">${loanTitle.title}：</div>
                                        <#list loanTitleRelation.applicationMaterialUrls?split(",") as title>
                                            <img layer-src="${title}" src="${title}" alt="${loanTitle.title}"/>
                                        </#list>
                                    </#if>
                                </#list>
                            </#list>
                        </div>
                    </div>
                </div>
                <div class="loan-list-con">
                    <table class="table-striped">
                    </table>
                    <div class="pagination" data-url="/loan/${loan.id?string("0")}/invests" data-page-size="10">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var intDiff = parseInt(${loan.preheatSeconds?string('0')});//倒计时总秒数量
    var java_point = ${loan.progress?string('0')}; //后台传递数据
        <@global.role hasRole="'INVESTOR'">
        var user_can_invest = true;
        </@global.role>
</script>
</@global.main>