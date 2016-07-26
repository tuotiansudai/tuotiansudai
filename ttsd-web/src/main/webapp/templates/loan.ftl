<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.loan_detail}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="loan-detail-content" data-loan-status="${loan.loanStatus}" data-loan-progress="${loan.progress?string.computer}" data-loan-countdown="${loan.preheatSeconds?string.computer}"
     data-user-role="<@global.role hasRole="'INVESTOR'">INVESTOR</@global.role>">
    <div class="borderBox clearfix no-border">
        <div class="loan-model borderBox no-border">
            <div class="news-share bg-w fl">
                <h2 class="hd clearfix title-block <#if loan.activityType == 'NEWBIE'>new</#if>">
                    <div class="fl title">${loan.name}</div>
                    <div class="fl orange extra-rate" id="extra-rate">投资加息+0.4%~0.5% <i class="fa fa-question-circle-o" aria-hidden="true"></i></div>
                    <script>
                        var __extraRate = [
                               {
                                   minInvestAmount: 100,
                                   maxInvestAmount: 999,
                                   rate: 0.3
                               }, {
                                   minInvestAmount: 1000,
                                   maxInvestAmount: 9999,
                                   rate: 0.4
                               }, {
                                   minInvestAmount: 10000,
                                   maxInvestAmount: 0,
                                   rate: 0.5
                               }
                           ];
                    </script>
                    <script type="text/template" id="extra-rate-popup-tpl">
                       <div class="extra-rate-popup" id="extra-rate-popup">
                           <div class="header clearfix">
                               <div class="td fl">投资金额</div>
                               <div class="td fl">加息</div>
                           </div>
                           <% _.each(__extraRate, function(value){ %>
                               <div class="clearfix">
                                   <div class="td fl"><%= value.minInvestAmount %>元 ≤ 投资额</div>
                                   <div class="td fl"><%= value.rate %>%</div>
                               </div>
                           <% }) %>
                       </div>
                    </script>
                    <span class="fr boilerplate">借款协议样本</span>
                </h2>
               <div class="container-block loan-info">
                   <div class="content">
                       <div class="row loan-number-detail">
                           <div class="col-md-4">
                               <div class="title">预期年化收益</div>
                               <div class="number red">11<i class="data-extra-rate" data-extra-rate></i><span>%</span></div>
                           </div>
                           <div class="col-md-4">
                               <div class="title">项目期限</div>
                               <div class="number">90<span>天</span></div>
                           </div>
                           <div class="col-md-4">
                               <div class="title">项目金额</div>
                               <div class="number">100<span>万元</span></div>
                           </div>
                       </div>
                       <div class="row loan-active-detail">
                           <div class="col-md-6">
                               <span class="title">投资进度：</span>
                               <div class="progress-bar">
                                   <div class="progress-inner" style="width: 44.55%">
                                   </div>
                               </div>
                               <#-- 这里的百分比要和上面 .progress-inner的style里的百分比一样 -->
                               <span class="orange2">44.55%</span>
                           </div>
                           <div class="col-md-6">
                               <span class="title">可投金额：</span>
                               33,333,333元
                           </div>
                           <div class="col-md-6">
                               <span class="title">募集截止时间：</span>
                               6天12小时58分(标满即放款)
                           </div>
                           <div class="col-md-6">
                               <span class="title">还款方式：</span>
                               按月付息，到期还本
                           </div>
                       </div>
                   </div> <#-- .content end tag -->
               </div> <#-- .container-block end tag -->
            </div>
            <div class="account-info fl bg-w">
                <h5 class="l-title">拓天速贷提醒您：投资非存款，投资需谨慎！</h5>
                <#if ["PREHEAT", "RAISING"]?seq_contains(loan.loanStatus)>
                    <form action="/invest" method="post" id="investForm">
                        <dl class="account-list">
                            <!-- <dd>
                                <span class="fl">可投金额：</span>
                                <em class="fr">
                                    <i class="amountNeedRaised-i" data-amount-need-raised="${loan.amountNeedRaised?string.computer}">${(loan.amountNeedRaised / 100)?string("0.00")}</i> 元
                                </em>
                            </dd> -->
                            <dd class="clearfix"><span class="fl">账户余额：</span><a class="fr" href="#">去充值>></a><em class="fr account-amount" data-user-balance="${loan.userBalance?string.computer}">${(loan.userBalance / 100)?string("0.00")} 元</em></dd>
                            <!-- <dd><span class="fl">每人限投：</span><em class="fr">${loan.maxInvestAmount} 元</em></dd> -->
                            <dd class="invest-amount tl" <#if loan.loanStatus == "PREHEAT">style="display: none"</#if>>
                                <span class="fl">投资金额：</span>
                                <input type="text" name="amount" data-l-zero="deny" data-v-min="0.00" data-min-invest-amount="${loan.minInvestAmount}" data-max-invest-amount="${loan.maxInvestAmount}" placeholder="0.00" value="${investAmount!loan.maxAvailableInvestAmount}"
                                       data-no-password-remind="${loan.hasRemindInvestNoPassword?c}"
                                       data-no-password-invest="${loan.investNoPassword?c}"
                                       data-auto-invest-on="${loan.autoInvest?c}"
                                       class="text-input-amount fr position-width"/>
                                <#if errorMessage?has_content>
                                    <span class="errorTip hide"><i class="fa fa-times-circle"></i>${errorMessage!}</span>
                                </#if>
                                <#if errorType?has_content>
                                    <input type="hidden" class="errorType hide" value="${errorType!}"/>
                                </#if>
                            </dd>

                            <dd class="experience-ticket clearfix" <#if loan.loanStatus == "PREHEAT">style="display: none"</#if>>
                                <span class="fl">优惠券：</span>
                                <div class="fr experience-ticket-box">
                                    <em class="experience-ticket-input <#if !coupons?has_content>disabled</#if>" id="use-experience-ticket">
                                        <span>
                                            <#if coupons?has_content>
                                                <#if maxBenefitUserCoupon??>
                                                    <#switch maxBenefitUserCoupon.couponType>
                                                        <#case "INTEREST_COUPON">
                                                            +${maxBenefitUserCoupon.rate * 100}%${maxBenefitUserCoupon.name}
                                                            <#break>
                                                        <#case "BIRTHDAY_COUPON">
                                                        ${maxBenefitUserCoupon.name}
                                                            <#break>
                                                        <#default>
                                                        ${maxBenefitUserCoupon.name}${(maxBenefitUserCoupon.amount / 100)?string("0.00")}元
                                                    </#switch>
                                                <#else>
                                                    请选择优惠券
                                                </#if>
                                            <#else>
                                                当前无可用优惠券
                                            </#if>
                                    </span>
                                    <i class="fa fa-sort-down fr"></i>
                                    <i class="fa fa-sort-up hide fr"></i>
                                </em>
                                <#if coupons?has_content>
                                    <ul class="ticket-list hide">
                                        <#list coupons as coupon>
                                            <#if !coupon.shared>
                                                <li data-coupon-id="${coupon.couponId?string.computer}"
                                                    data-user-coupon-id="${coupon.id?string.computer}"
                                                    data-coupon-type="${coupon.couponType}"
                                                    data-product-type-usable="${coupon.productTypeList?seq_contains(loan.productType)?string('true', 'false')}"
                                                    data-coupon-end-time="${coupon.endTime?string("yyyy-MM-dd")}T${coupon.endTime?string("HH:mm:ss")}"
                                                    <#if coupon.investLowerLimit!=0>class="lower-upper-limit"</#if>>
                                                    <input type="radio"
                                                           id="${coupon.id?string.computer}"
                                                           name="userCouponIds"
                                                           value="${coupon.id?string.computer}"
                                                           class="input-use-ticket"
                                                           <#if maxBenefitUserCoupon?? && maxBenefitUserCoupon.id == coupon.id>
                                                           checked
                                                           </#if>
                                                    />
                                                    <label>
                                                        <span class="sign">${coupon.couponType.getAbbr()}</span>
                                                        <span class="ticket-info">
                                                            <i class="ticket-title">
                                                                <#switch coupon.couponType>
                                                                    <#case "INTEREST_COUPON">
                                                                        +${coupon.rate * 100}%${coupon.name}
                                                                        <#break>
                                                                    <#case "BIRTHDAY_COUPON">
                                                                    ${coupon.name}
                                                                        <#break>
                                                                    <#default>
                                                                    ${coupon.name}${(coupon.amount / 100)?string("0.00")}元
                                                                </#switch>
                                                            </i>
                                                            <#if !(coupon.productTypeList?seq_contains(loan.productType))>
                                                                <br/>
                                                                <i class="ticket-term" title="[适用于<#list coupon.productTypeList as productType>${productType.getName()}<#if productType_has_next> 、</#if></#list>可用]">[适用于<#list coupon.productTypeList as productType>${productType.getName()}<#if productType_has_next> 、</#if></#list>可用]</i>
                                                            <#else>
                                                                <br/>
                                                                <#if coupon.investLowerLimit!=0>
                                                                    <i class="ticket-term lower-limit" data-invest-lower-limit="${coupon.investLowerLimit?string.computer}">[投资满${(coupon.investLowerLimit / 100)?string("0.00")}元即可使用]</i>
                                                                </#if>
                                                                <#if coupon.investLowerLimit==0>
                                                                    <i class="ticket-term"><#if coupon.couponType=='BIRTHDAY_COUPON'>[首月享${1 + coupon.birthdayBenefit}倍收益]<#else>[投资即可使用]</#if>
                                                                    </i>
                                                                </#if>
                                                            </#if>
                                                            </span>
                                                        </label>
                                                    </li>
                                                </#if>
                                            </#list>
                                        </ul>
                                        <#list coupons as coupon>
                                            <#if (coupon.shared && coupon.investLowerLimit==0 && coupon.productTypeList?seq_contains(loan.productType))>
                                                <input type="hidden" id="${coupon.id?string.computer}" name="userCouponIds" value="${coupon.id?string.computer}" data-coupon-id="${coupon.couponId?string.computer}" />
                                                <p class="red-tiptext clearfix">
                                                    <i class="icon-redbag"></i>
                                                    <span>${coupon.couponType.getName()}${(coupon.amount / 100)?string("0.00")}元（投资即可返现）</span>
                                                </p>
                                            </#if>
                                        </#list>
                                    </#if>
                                </div>
                            </dd>

                            <dd class="expected-interest-dd tc" <#if loan.loanStatus == "PREHEAT">style="display: none"</#if>>
                                <span>预计总收益：</span>
                                <span class="principal-income">0.00</span>
                                <span class="experience-income"></span>
                                元
                            </dd>

                            <dd class="time-item" <#if loan.loanStatus == "RAISING">style="display: none"</#if>>
                                <#if loan.preheatSeconds lte 1800>
                                    <i class="time-clock"></i><strong id="minute_show">00</strong><em>:</em><strong id="second_show">00</strong>以后可投资
                                <#else>
                                    ${(loan.fundraisingStartTime?string("yyyy-MM-dd HH时mm分"))!}放标
                                </#if>
                            </dd>

                            <dd>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input class="hid-loan" type="hidden" name="loanId" value="${loan.id?string.computer}"/>
                                <button id="investSubmit" class="btn-pay btn-normal" type="button" <#if loan.loanStatus == "PREHEAT">disabled="disabled"</#if>>
                                    <#if loan.loanStatus == "PREHEAT">预热中</#if>
                                    <#if loan.loanStatus == "RAISING">马上投资</#if>
                                </button>
                            </dd>
                            <@global.role hasRole="'INVESTOR'">
                                <#if !loan.investNoPassword>
                                    <dd>
                                        <a class="fl open-no-password-invest" id="noPasswordTips" data-open-agreement="${loan.autoInvest?c}" >
                                            推荐您开通免密投资
                                            <i class="fa fa-question-circle text-m" title="开通后您可以简化投资过程，理财快人一步"></i>
                                        </a>
                                    </dd>
                                </#if>
                            </@global.role>
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
        </div>
        <div class="chart-info-responsive bg-w">
            项目金额：<@amount>${loan.loanAmount?string.computer}</@amount> 元<br/>
            代理人：${loan.agentLoginName}<br/>
            借款人：${loan.loanerLoginName}<br/>
            项目期限：${loan.periods}<#if loan.type.getLoanPeriodUnit() == "MONTH"> 月<#else> 天</#if><br/>
            还款方式：${loan.type.getName()}<br/>
            投资要求：${loan.minInvestAmount} 元起投，投资金额为 ${loan.investIncreasingAmount} 元的整数倍<br/>
            <a href="${staticServer}/pdf/loanAgreementSample.pdf" target="_blank">借款协议样本</a>
        </div>

        <#if loan.achievement??>
            <div class="loan-designation bg-w clearfix borderBox">
                <h3>称号争夺大作战<a href="/activity/invest-achievement" target="_blank">查看活动详情>></a></h3>
                <table class="table design-table">
                    <thead>
                    <tr>
                        <th class="title">称号</th>
                        <th><i class="first-icon"></i>拓荒先锋</th>
                        <th><i class="max-icon"></i>拓天标王</th>
                        <th><i class="last-icon"></i>一锤定音</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="title">获得者</td>
                        <td>${loan.achievement.firstInvestAchievementMobile!('<span class="text-lighter">虚位以待</span>')}</td>
                        <td>
                            <#if loan.achievement.maxAmountAchievementMobile??>
                                ${loan.achievement.maxAmountAchievementMobile}
                                <#if loan.loanStatus == 'RAISING'>
                                    <span class="text-lighter">(待定)</span>
                                </#if>
                            <#else>
                                <span class="text-lighter">虚位以待</span>
                            </#if>
                        </td>
                        <td>${loan.achievement.lastInvestAchievementMobile!('<span class="text-lighter">虚位以待</span>')}</td>
                    </tr>
                    <tr>
                        <td class="title">战况</td>
                        <td>
                            <#if loan.achievement.firstInvestAchievementMobile??>
                                ${loan.achievement.firstInvestAchievementDate?string("yyyy-MM-dd HH:mm:dd")} 占领先锋
                            <#else>
                                --
                            </#if>
                        </td>
                        <td>
                            <#if loan.achievement.maxAmountAchievementMobile??>
                                以累积投资 ${loan.achievement.maxAmountAchievementAmount}元 夺得标王
                            <#else>
                                --
                            </#if>
                        </td>
                        <td>
                            <#if loan.achievement.lastInvestAchievementMobile??>
                                ${loan.achievement.lastInvestAchievementDate?string("yyyy-MM-dd HH:mm:dd")} 一锤定音
                            <#else>
                                目前项目剩余${loan.achievement.loanRemainingAmount}元<br/>快来一锤定音吧
                            </#if>
                        </td>
                    </tr>
                    <tr>
                        <td class="title">奖励</td>
                        <td>0.2％加息券＋50元红包</td>
                        <td><span class="text-reward">0.5％加息券＋100元红包</span></td>
                        <td>0.2％加息券＋50元红包</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </#if>

        <div class="bg-w clear-blank borderBox loan-detail">
            <div class="loan-nav">
                <ul class="clearfix">
                    <li class="active">借款详情</li>
                    <li>出借记录</li>
                </ul>
            </div>
            <div class="loan-list pad-s invest-list-content">
                <div class="loan-list-con">
                    <div class="content detail">
                        <div class="subtitle">
                            <h3>借款基本信息</h3>
                        </div>
                        <div class="container-fluid list-block">
                            <div class="row">
                                <div class="col-md-4">借款人：刘某某</div>
                                <div class="col-md-4">平台ID：1212</div>
                                <div class="col-md-4">性别：男</div>
                                <div class="col-md-4">年龄：22</div>
                                <div class="col-md-4">婚姻状况：已婚</div>
                                <div class="col-md-4">身份证号：212121212121212</div>
                                <div class="col-md-4">申请地区：北京</div>
                                <div class="col-md-4">收入水平：5万元</div>
                            </div>
                        </div>
                        <div class="subtitle">
                            <h3>抵押档案</h3>
                        </div>
                        <div class="container-fluid list-block">
                            <div class="row">
                                <div class="col-md-4">借款人：刘某某</div>
                                <div class="col-md-4">平台ID：1212</div>
                                <div class="col-md-4">性别：男</div>
                                <div class="col-md-4">年龄：22</div>
                                <div class="col-md-4">婚姻状况：已婚</div>
                                <div class="col-md-4">身份证号：212121212121212</div>
                                <div class="col-md-4">申请地区：北京</div>
                                <div class="col-md-4">收入水平：5万元</div>
                            </div>
                        </div>
                        <div class="subtitle">
                            <h3>风控审核</h3>
                        </div>
                        <div class="container-fluid danger-control">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="container-fluid table">
                                        <div class="row">
                                            <div class="col-xs-6 bg">1212</div>
                                            <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                            <div class="col-xs-6 bg">1212</div>
                                            <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                            <div class="col-xs-6 br-b bg">1212</div>
                                            <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="container-fluid table">
                                        <div class="row">
                                            <div class="col-xs-6 bg">1212</div>
                                            <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                            <div class="col-xs-6 bg">1212</div>
                                            <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                            <div class="col-xs-6 br-b bg">1212</div>
                                            <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>1212</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div> <#-- .danger-control end tag -->
                        <div class="subtitle">
                            <h3>申请资料</h3>
                        </div>
                        <div class="apply-data">
                            <h5>1、身份证</h5>
                            <div class="scroll-wrap" scroll-carousel>
                                <div class="scroll-content">
                                    <div class="row">
                                        <a class="col" href="http://placekitten.com/200/125" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/125" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                    </div>
                                </div>
                                <div class="left-button disabled">
                                </div>
                                <div class="right-button">
                                </div>
                            </div>
                            <h5>2、房本证件</h5>
                            <div class="scroll-wrap" scroll-carousel>
                                <div class="scroll-content">
                                    <div class="row">
                                        <a class="col" href="http://placekitten.com/200/125" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/125" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                    </div>
                                </div>
                                <div class="left-button disabled">
                                </div>
                                <div class="right-button">
                                </div>
                            </div>
                            <h5>3、借款合同</h5>
                            <div class="scroll-wrap" scroll-carousel>
                                <div class="scroll-content">
                                    <div class="row">
                                        <a class="col" href="http://placekitten.com/200/125" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/125" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                    </div>
                                </div>
                                <div class="left-button disabled">
                                </div>
                                <div class="right-button">
                                </div>
                            </div>
                            <h5>4、房屋强制执行公证书</h5>
                            <div class="scroll-wrap" scroll-carousel>
                                <div class="scroll-content">
                                    <div class="row">
                                        <a class="col" href="http://placekitten.com/200/125" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/125" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                    </div>
                                </div>
                                <div class="left-button disabled">
                                </div>
                                <div class="right-button">
                                </div>
                            </div>
                            <h5>5、打款凭条</h5>
                            <div class="scroll-wrap" scroll-carousel>
                                <div class="scroll-content">
                                    <div class="row">
                                        <a class="col" href="http://placekitten.com/200/125" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/125" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                    </div>
                                </div>
                                <div class="left-button disabled">
                                </div>
                                <div class="right-button">
                                </div>
                            </div>
                            <h5>6、不动产登记证明</h5>
                            <div class="scroll-wrap" scroll-carousel>
                                <div class="scroll-content">
                                    <div class="row">
                                        <a class="col" href="http://placekitten.com/200/125" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/125" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                        <a class="col" href="http://placekitten.com/200/120" rel="example_group">
                                            <img class="img" src="http://placekitten.com/200/120" layer-src="http://placekitten.com/200/120" />
                                        </a>
                                    </div>
                                </div>
                                <div class="left-button disabled">
                                </div>
                                <div class="right-button">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="loan-list-con">
                    <div class="content record">
                        <div class="row title-list">
                            <div class="col-md-4">
                                <div class="br">
                                    <div class="item">
                                        <h4 class="first"><span><a href="#">拓荒先锋 >></a></span></h4>
                                        <p>恭喜yyh***2016-05-21 10:49:21 拔得头筹奖励0.2％加息券＋50元红包</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="br">
                                    <div class="item">
                                        <h4 class="king"><span><a href="#">拓天标王 >></a></span></h4>
                                        <p>恭喜yyh****以累计投资 60000.00元 夺得标王奖励0.2％加息券＋50元红包</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="br">
                                    <div class="item">
                                        <h4 class="hammer"><span><a href="#">一锤定音 >></a></span></h4>
                                        <p>恭喜yyh***2016-05-21 10:49:21 终结此标奖励0.2％加息券＋50元红包</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <table class="table-striped invest-list">
                        </table>
                        <div class="pagination" data-url="/loan/${loan.id?string.computer}/invests" data-page-size="10">
                        </div>
                    </div>
            </div>
        </div>
    </div>
</div>
<div id="authorizeAgreementOptions" class="pad-s-tb tl fl hide">
    <p class="mb-0 text-m color-title">请在新打开的联动优势完成操作后选择：</p>
    <p class="text-m"><span class="title-text">授权成功：</span><span class="go-on-btn success_go_on_invest">继续投资</span><span class="color-tip">（授权后可能会有几秒的延迟）</span></p>
    <p class="mb-0"><span class="title-text">授权失败： </span><span class="again-btn">重新授权</span><span class="btn-lr">或</span><span class="go-on-btn fail_go_on_invest">继续投资</span></p>
    <p class="text-s color-title">遇到问题请拨打我们的客服热线：400-169-1188（工作日9:00-20:00）</p>
</div>
<form action="/agreement" id="goAuthorize" method="post" target="_blank">
    <input type="hidden" name = "noPasswordInvest" value="true" />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
    <#include "coupon-alert.ftl" />
</div>
    <#include "red-envelope-float.ftl" />
</@global.main>