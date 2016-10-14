<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.loan_detail}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="loan-detail-content" data-loan-status="${loan.loanStatus}" data-loan-progress="${loan.progress?string.computer}" data-loan-countdown="${loan.countdown?string.computer}"
     data-authentication="<@global.role hasRole="'USER'">USER</@global.role>" data-user-role="<@global.role hasRole="'INVESTOR'">INVESTOR</@global.role>" >
    <div class="borderBox clearfix no-border">
        <div class="loan-model bg-w">
            <div class="news-share bg-w">
                <h2 class="hd clearfix title-block <#if loan.activityType == 'NEWBIE'>new</#if>">
                    <div class="fl title">${loan.name}</div>
                    <#if loan.activity?string("true","false") == "true">
                        <span class="arrow-tag-normal">
                            <i class="ic-left"></i>
                            <em>${loan.activityDesc!}</em>
                            <i class="ic-right"></i>
                        </span>
                    </#if>
                    <#if loan.extraSource?? && loan.extraSource == "MOBILE">
                        <div class="fl orange extra-rate">
                            <i class="fa fa-mobile"></i>
                            APP专享
                        </div>
                    <#else>
                        <#if extraLoanRates??>
                            <div class="fl orange extra-rate" id="extra-rate">投资奖励+${extraLoanRates.minExtraRate}%~${extraLoanRates.maxExtraRate}%<i class="fa fa-question-circle" aria-hidden="true"></i>
                            </div>
                            <script>
                                var __extraRate = [
                                    <#list extraLoanRates.items as extraLoanRate>
                                        {
                                            minInvestAmount: ${extraLoanRate.amountLower},
                                            maxInvestAmount: ${extraLoanRate.amountUpper},
                                            rate: ${extraLoanRate.rate}
                                        }<#if extraLoanRate_has_next>,</#if>
                                    </#list>];
                            </script>
                        </#if>
                        <script type="text/template" id="extra-rate-popup-tpl">
                            <div class="extra-rate-popup" id="extra-rate-popup">
                                <div class="header clearfix">
                                    <div class="td fl">投资金额</div>
                                    <div class="td fl">投资奖励</div>
                                </div>
                                <% _.each(__extraRate, function(value){
                                var text;
                                if(value.maxInvestAmount!=0) {
                                text =' < '+ value.maxInvestAmount;
                                }
                                else {
                                text='';
                                }
                                %>
                                <div class="clearfix">
                                    <div class="td fl"><%= value.minInvestAmount %>元 ≤ 投资额 <%=text %></div>
                                    <div class="td fl"><%= value.rate %>%</div>
                                </div>
                                <% }) %>
                            </div>
                        </script>
                    </#if>
                    <span class="fr boilerplate"><a href="${staticServer}/pdf/loanAgreementSample.pdf" target="_blank">借款协议样本</a></span>
                </h2>
                <div class="container-block loan-info">
                    <div class="content">
                        <div class="row loan-number-detail">
                            <div class="col-md-4">
                                <div class="title">预期年化收益</div>
                                <div class="number red"><@percentInteger>${loan.baseRate}</@percentInteger><@percentFraction>${loan.baseRate}</@percentFraction>
                                    <#if loan.activityRate != 0>
                                        <i class="data-extra-rate">
                                            +<@percentInteger>${loan.activityRate}</@percentInteger><@percentFraction>${loan.activityRate}</@percentFraction>
                                        </i>
                                    </#if>
                                    <i class="data-extra-rate" data-extra-rate></i>
                                    <span>%</span>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="title">项目期限</div>
                                <div class="number">${loan.duration}<span>天</span></div>
                            </div>
                            <div class="col-md-4">
                                <div class="title">项目金额</div>
                                <div class="number"><@amount>${loan.loanAmount?string.computer}</@amount><span>元</span></div>
                            </div>
                        </div>
                        <div class="row loan-active-detail">
                            <div class="col-md-6">
                                <span class="title">投资进度：</span>
                                <div class="progress-bar">
                                    <div class="progress-inner" style="width: ${loan.progress?string("0.00")}%"></div>
                                </div>
                            <#-- 这里的百分比要和上面 .progress-inner的style里的百分比一样 -->
                                <span class="orange2">${loan.progress?string("0.00")}%</span>
                            </div>
                            <div class="col-md-6">
                                <span class="title">可投金额：</span>
                            ${(loan.amountNeedRaised / 100)?string("0.00")}元
                            </div>
                            <#if loan.loanStatus='RAISING'>
                                <div class="col-md-6">
                                    <span class="title">募集截止时间：</span>
                                ${loan.raisingPeriod.getDays()}天${loan.raisingPeriod.getHours()}小时${loan.raisingPeriod.getMinutes()}分(标满即放款)
                                </div>
                            </#if>
                            <div class="col-md-6">
                                <span class="title">还款方式：</span>
                            ${loan.type.getRepayType()},${loan.type.getInterestType()}
                            </div>
                        </div>
                    </div> <#-- .content end tag -->
                </div> <#-- .container-block end tag -->
            </div>
            <div class="blank-middle"></div>
            <div class="account-info bg-w">
                <h5 class="l-title">拓天速贷提醒您：投资非存款，投资需谨慎！</h5>
                <#if ["PREHEAT", "RAISING"]?seq_contains(loan.loanStatus)>
                    <form action="/invest" method="post" id="investForm">
                        <dl class="account-list">
                            <dd class="clearfix">
                                <span class="fl">账户余额：</span>
                                <a class="fr" href="/recharge">去充值>></a>
                                <em class="fr account-amount" data-user-balance="${loan.investor.balance?string.computer}">${(loan.investor.balance / 100)?string("0.00")} 元</em>
                            </dd>
                            <dd class="invest-amount tl" <#if loan.loanStatus == "PREHEAT">style="display: none"</#if>>
                                <span class="fl">投资金额：</span>
                                <input type="text" name="amount" data-l-zero="deny" data-v-min="0.00" data-min-invest-amount="${loan.minInvestAmount}" data-max-invest-amount="${loan.maxInvestAmount}"
                                       placeholder="0.00" value="${loan.investor.maxAvailableInvestAmount}"
                                       data-no-password-remind="${loan.investor.remindNoPassword?c}"
                                       data-no-password-invest="${loan.investor.noPasswordInvest?c}"
                                       data-auto-invest-on="${loan.investor.autoInvest?c}"
                                       data-amount-need-raised="${loan.amountNeedRaised?string.computer}"
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
                                                                <i class="ticket-term"
                                                                   title="[适用于<#list coupon.productTypeList as productType>${productType.getName()}<#if productType_has_next> 、</#if></#list>可用]">[适用于<#list coupon.productTypeList as productType>${productType.getName()}<#if productType_has_next>
                                                                    、</#if></#list>可用]</i>
                                                            <#else>
                                                                <br/>
                                                                <#if coupon.investLowerLimit!=0>
                                                                    <i class="ticket-term lower-limit"
                                                                       data-invest-lower-limit="${coupon.investLowerLimit?string.computer}">[投资满${(coupon.investLowerLimit / 100)?string("0.00")}元即可使用]</i>
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
                                                <input type="hidden" id="${coupon.id?string.computer}" name="userCouponIds" value="${coupon.id?string.computer}"
                                                       data-coupon-id="${coupon.couponId?string.computer}"/>
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
                                <#if membershipPreferenceValid>
                                    <#if membershipLevel==2>
                                        <i class="icon-graded level2"></i>
                                    </#if>
                                    <#if membershipLevel==3>
                                        <i class="icon-graded level3"></i>
                                    </#if>
                                    <#if membershipLevel==4>
                                        <i class="icon-graded level4"></i>
                                    </#if>
                                    <#if membershipLevel==5>
                                        <i class="icon-graded level5"></i>
                                    </#if>
                                </#if>

                            </dd>

                            <dd class="time-item" <#if loan.loanStatus == "RAISING">style="display: none"</#if>>
                                <#if loan.countdown lte 1800>
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
                                <#if !loan.investor.noPasswordInvest>
                                    <dd>
                                        <a class="fl open-no-password-invest" id="noPasswordTips" data-open-agreement="${loan.investor.autoInvest?c}">
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
                        <#if loan.loanerDetail??>
                            <div class="subtitle">
                                <h3>借款人基本信息</h3>
                            </div>
                            <div class="container-fluid list-block">
                                <div class="row">
                                    <#list ['借款人', '性别', '年龄', '婚姻状况', '身份证号', '申请地区', '收入水平', '就业情况'] as key>
                                        <#if loan.loanerDetail[key]?? && loan.loanerDetail[key] != '' && loan.loanerDetail[key] != '不明' >
                                            <div class="col-md-4">${key}：${loan.loanerDetail[key]}</div>
                                        </#if>
                                    </#list>
                                </div>
                            </div>
                        </#if>

                        <#if loan.basicInfo??>
                            <div class="subtitle">
                                <h3>借款用途描述</h3>
                            </div>
                            <div class="container-fluid list-block">
                                <p>${loan.basicInfo}</p>
                            </div>
                        </#if>

                        <div class="subtitle">
                            <h3>抵押档案</h3>
                        </div>
                        <div class="container-fluid list-block">
                            <div class="row">
                                <#if loan.pledgeHouseDetail??>
                                    <#list ['抵押物所在地', '抵押物估值', '房屋面积', '房产证编号', '不动产登记证明', '公证书编号', '抵押物借款金额'] as key>
                                        <#if loan.pledgeHouseDetail[key]?? && loan.pledgeHouseDetail[key] != ''>
                                            <div class="col-md-4">${key}：${loan.pledgeHouseDetail[key]}</div>
                                        </#if>
                                    </#list>
                                </#if>

                                <#if loan.pledgeVehicleDetail??>
                                    <#list ['抵押物所在地', '车辆品牌', '车辆型号', '抵押物估值', '抵押物借款金额'] as key>
                                        <#if loan.pledgeVehicleDetail[key]?? && loan.pledgeVehicleDetail[key] != ''>
                                            <div class="col-md-4">${key}：${loan.pledgeVehicleDetail[key]}</div>
                                        </#if>
                                    </#list>
                                </#if>

                                <#if loan.pledgeEnterpriseDetail??>
                                    <#list ['公司法人', '公司最高持股人', '公司所在地', '担保方式', '抵押物估值', '抵押物所在地'] as key>
                                        <#if loan.pledgeEnterpriseDetail[key]?? && loan.pledgeEnterpriseDetail[key] != ''>
                                            <div class="col-md-4">${key}：${loan.pledgeEnterpriseDetail[key]}</div>
                                        </#if>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                        <div class="subtitle">
                            <h3>风控审核</h3>
                        </div>
                        <div class="container-fluid danger-control">
                            <div class="row">
                                <#if loan.pledgeType == 'ENTERPRISE'>
                                    <div class="col-md-6">
                                        <div class="container-fluid table">
                                            <div class="row">
                                                <div class="col-xs-6 bg">法人核证</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 bg">法人征信</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">股东持股</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">验资报告</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="container-fluid table">
                                            <div class="row">
                                                <div class="col-xs-6 bg">公司对公账单</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 bg">银行流水查证</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">账务报表审计</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">税务缴纳</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                            </div>
                                        </div>
                                    </div>
                                <#else>
                                    <div class="col-md-6">
                                        <div class="container-fluid table">
                                            <div class="row">
                                                <div class="col-xs-6 bg">身份认证</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 bg">手机认证</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">婚姻状况认证</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="container-fluid table">
                                            <div class="row">
                                                <div class="col-xs-6 bg">房产认证</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 bg">住址信息认证</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">收入证明</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                            </div>
                                        </div>
                                    </div>
                                </#if>
                            </div>
                        </div> <#-- .danger-control end tag -->
                        <div class="subtitle">
                            <h3>申请资料</h3>
                        </div>
                        <div class="apply-data">
                            <#list loan.loanTitleDto as loanTitle>
                                <#list loan.loanTitles as loanTitleRelation >
                                    <#if loanTitle.id == loanTitleRelation.titleId>
                                        <h5>${loanTitle.title}</h5>
                                        <div class="scroll-wrap" scroll-carousel>
                                            <div class="scroll-content">
                                                <div class="row">
                                                    <#list loanTitleRelation.applicationMaterialUrls?split(",") as title>
                                                        <a class="col" href="${title}" rel="example_group">
                                                            <img class="img" layer-src="${staticServer}${title}" src="${staticServer}${title}" alt="${loanTitle.title}"/>
                                                        </a>
                                                    </#list>
                                                </div>
                                            </div>
                                            <div class="left-button disabled"></div>
                                            <div class="right-button"></div>
                                        </div>
                                    </#if>
                                </#list>
                            </#list>
                            <h5>声明：${loan.declaration!}</h5>
                        </div>
                    </div>
                </div>
                <div class="loan-list-con">
                    <div class="content record">
                        <#if loan.achievement??>
                        <div class="row title-list">
                            <div class="col-md-4">
                                <div class="br">
                                    <div class="item">
                                        <h4 class="first"><span><a href="/activity/invest-achievement" target="_blank">拓荒先锋 >></a></span></h4>
                                        <#if (loan.achievement.firstInvestAchievementMobile)??>
                                            <p>恭喜${loan.achievement.firstInvestAchievementMobile} ${loan.achievement.firstInvestAchievementDate?string("yyyy-MM-dd HH:mm:dd")} 拔得头筹 奖励0.2％加息券＋50元红包</p>
                                        <#else>
                                            <p>虚位以待</p>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="br">
                                    <div class="item">
                                        <h4 class="king"><span><a href="/activity/invest-achievement" target="_blank">拓天标王 >></a></span></h4>
                                        <#if (loan.achievement.maxAmountAchievementMobile)??>
                                            <p>恭喜${loan.achievement.maxAmountAchievementMobile} 以累计投资${loan.achievement.maxAmountAchievementAmount}元 <#if loan.loanStatus == 'RAISING'><span
                                                    class="text-lighter">(待定)</span></#if>夺得标王 奖励0.5％加息券＋100元红包</p>
                                        <#else>
                                            <p>虚位以待</p>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="br">
                                    <div class="item">
                                        <h4 class="hammer"><span><a href="/activity/invest-achievement" target="_blank">一锤定音 >></a></span></h4>
                                        <#if (loan.achievement.lastInvestAchievementMobile)??>
                                            <p>恭喜${loan.achievement.lastInvestAchievementMobile} ${loan.achievement.lastInvestAchievementDate?string("yyyy-MM-dd HH:mm:dd")} 终结此标 奖励0.2％加息券＋50元红包</p>
                                        <#else>
                                            <p>目前项目剩余${(loan.amountNeedRaised / 100)?string("0.00")}元快来一锤定音吧</p>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </div>
                        </#if>
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
        <input type="hidden" name="noPasswordInvest" value="true"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <#include "coupon-alert.ftl" />
</div>
    <#include "red-envelope-float.ftl" />
    <#include "login-tip.ftl" />
</@global.main>