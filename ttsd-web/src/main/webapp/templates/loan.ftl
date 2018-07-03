<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.loan_detail}" pageJavascript="${js.loan_detail}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="loan-detail-content" id="loanDetailContent" data-loan-status="${loan.loanStatus}" data-loan-progress="${loan.progress?string.computer}" data-loan-countdown="${loan.countdown?string.computer}" data-estimate="${estimate?string('true', 'false')}"
     data-authentication="<@global.role hasRole="'USER'">USER</@global.role>" data-user-role="<@global.role hasRole="'INVESTOR'">INVESTOR</@global.role>" data-bankcard="${hasBankCard?c}">
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
                    <span class="fr boilerplate"><a href="${commonStaticServer}/images/pdf/loanAgreement-sample.pdf" target="_blank">债权转让协议样本</a></span>
                </h2>
                <div class="container-block loan-info">
                    <div class="content">
                        <div class="row loan-number-detail clearfix">
                            <div class="col-md-4">
                                <div class="title">约定年化利率</div>
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
                                <div class="number"><span>最长</span>${loan.duration}<span>天</span></div>
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
                                    <span class="title">募集期限：</span>
                                ${loan.raisingDays}天
                                </div>

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
                <div class="row related-expenses clearfix">
                    <span class="title">相关费用：</span>
                    <span class="related-desc">根据会员等级的不同，收取投资应收收益7%-10%的费用。您当前投资可能会收取${investFeeRate*100}%技术服务费。</span>
                </div>
            </div>
            <div class="blank-middle"></div>
            <div class="account-info bg-w">

                    <h5 class="l-title"> <#if loan.estimate??><span id="riskTips" class="risk-tips">${loan.estimate}<em></em><i class="risk-tip-content extra-rate-popup">该项目适合投资偏好类型为${loan.estimate}的用户</i></span>  </#if>拓天速贷提醒您：市场有风险，投资需谨慎！</h5>

                <#if ["PREHEAT", "RAISING"]?seq_contains(loan.loanStatus)>
                    <form action="/invest" method="post" id="investForm">
                        <input type="hidden" name="zeroShoppingPrize" value="${zeroShoppingPrize!}">
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

                            <#--<dd class="experience-ticket clearfix" <#if loan.loanStatus == "PREHEAT">style="display: none"</#if>>-->
                                <#--<span class="fl">优惠券：</span>-->
                                <#--<div class="fr experience-ticket-box">-->
                                    <#--<em class="experience-ticket-input <#if !coupons?has_content>disabled</#if>" id="use-experience-ticket">-->
                                        <#--<span>-->
                                            <#--<#if coupons?has_content>-->
                                                <#--<#if maxBenefitUserCoupon??>-->
                                                    <#--<#switch maxBenefitUserCoupon.couponType>-->
                                                        <#--<#case "INTEREST_COUPON">-->
                                                            <#--+${maxBenefitUserCoupon.rate * 100}%${maxBenefitUserCoupon.name}-->
                                                            <#--<#break>-->
                                                        <#--<#case "BIRTHDAY_COUPON">-->
                                                        <#--${maxBenefitUserCoupon.name}-->
                                                            <#--<#break>-->
                                                        <#--<#default>-->
                                                        <#--${maxBenefitUserCoupon.name}${(maxBenefitUserCoupon.amount / 100)?string("0.00")}元-->
                                                    <#--</#switch>-->
                                                <#--<#else>-->
                                                    <#--请选择优惠券-->
                                                <#--</#if>-->
                                            <#--<#else>-->
                                                <#--当前无可用优惠券-->
                                            <#--</#if>-->
                                    <#--</span>-->
                                        <#--<i class="fa fa-sort-down fr"></i>-->
                                        <#--<i class="fa fa-sort-up hide fr"></i>-->
                                    <#--</em>-->
                                    <#--<#if coupons?has_content>-->
                                        <#--<ul class="ticket-list hide">-->
                                            <#--<#list coupons as coupon>-->
                                                <#--<#if !coupon.shared>-->
                                                    <#--<li data-coupon-id="${coupon.couponId?string.computer}"-->
                                                        <#--data-user-coupon-id="${coupon.id?string.computer}"-->
                                                        <#--data-coupon-type="${coupon.couponType}"-->
                                                        <#--data-product-type-usable="${coupon.productTypeList?seq_contains(loan.productType)?string('true', 'false')}"-->
                                                        <#--data-coupon-end-time="${coupon.endTime?string("yyyy-MM-dd")}T${coupon.endTime?string("HH:mm:ss")}"-->
                                                        <#--<#if coupon.investLowerLimit!=0>class="lower-upper-limit"</#if>>-->
                                                        <#--<input type="radio"-->
                                                               <#--id="${coupon.id?string.computer}"-->
                                                               <#--name="userCouponIds"-->
                                                               <#--value="${coupon.id?string.computer}"-->
                                                               <#--class="input-use-ticket"-->
                                                            <#--<#if maxBenefitUserCoupon?? && maxBenefitUserCoupon.id == coupon.id>-->
                                                               <#--checked-->
                                                            <#--</#if>-->
                                                        <#--/>-->
                                                        <#--<label>-->
                                                            <#--<span class="sign">${coupon.couponType.getAbbr()}</span>-->
                                                        <#--<span class="ticket-info">-->
                                                            <#--<i class="ticket-title">-->
                                                                <#--<#switch coupon.couponType>-->
                                                                    <#--<#case "INTEREST_COUPON">-->
                                                                        <#--+${coupon.rate * 100}%${coupon.name}-->
                                                                        <#--<#break>-->
                                                                    <#--<#case "BIRTHDAY_COUPON">-->
                                                                    <#--${coupon.name}-->
                                                                        <#--<#break>-->
                                                                    <#--<#default>-->
                                                                    <#--${coupon.name}${(coupon.amount / 100)?string("0.00")}元-->
                                                                <#--</#switch>-->
                                                            <#--</i>-->
                                                            <#--<#if !(coupon.productTypeList?seq_contains(loan.productType))>-->
                                                                <#--<br/>-->
                                                                <#--<#assign minProductType=361>-->
                                                                <#--<#list coupon.productTypeList as productType>-->
                                                                    <#--<#if productType.getDuration() < minProductType>-->
                                                                        <#--<#assign minProductType=productType.getDuration()>-->
                                                                    <#--</#if>-->
                                                                <#--</#list>-->
                                                                <#--<#if minProductType=90><#assign couponTips='[适用于60天以上项目可用]'></#if>-->
                                                                <#--<#if minProductType=180><#assign couponTips='[适用于120天以上项目可用]'></#if>-->
                                                                <#--<#if minProductType=360><#assign couponTips='[适用于200天以上项目可用]'></#if>-->
                                                                <#--<i class="ticket-term" title="${couponTips!}">${couponTips!}</i>-->
                                                            <#--<#else>-->
                                                                <#--<br/>-->
                                                                <#--<#if coupon.investLowerLimit!=0>-->
                                                                    <#--<i class="ticket-term lower-limit"-->
                                                                       <#--data-invest-lower-limit="${coupon.investLowerLimit?string.computer}">[投资满${(coupon.investLowerLimit / 100)?string("0.00")}元即可使用]</i>-->
                                                                <#--</#if>-->
                                                                <#--<#if coupon.investLowerLimit==0>-->
                                                                    <#--<i class="ticket-term"><#if coupon.couponType=='BIRTHDAY_COUPON'>[首月享${1 + coupon.birthdayBenefit}倍收益]<#else>[投资即可使用]</#if>-->
                                                                    <#--</i>-->
                                                                <#--</#if>-->
                                                            <#--</#if>-->
                                                            <#--</span>-->
                                                        <#--</label>-->
                                                    <#--</li>-->
                                                <#--</#if>-->
                                            <#--</#list>-->
                                        <#--</ul>-->
                                        <#--<#list coupons as coupon>-->
                                            <#--<#if (coupon.shared && coupon.investLowerLimit==0 && coupon.productTypeList?seq_contains(loan.productType))>-->
                                                <#--<input type="hidden" id="${coupon.id?string.computer}" name="userCouponIds" value="${coupon.id?string.computer}"-->
                                                       <#--data-coupon-id="${coupon.couponId?string.computer}"/>-->
                                                <#--<p class="red-tiptext clearfix">-->
                                                    <#--<i class="icon-redbag"></i>-->
                                                    <#--<span>${coupon.couponType.getName()}${(coupon.amount / 100)?string("0.00")}元（投资即可返现）</span>-->
                                                <#--</p>-->
                                            <#--</#if>-->
                                        <#--</#list>-->
                                    <#--</#if>-->
                                <#--</div>-->
                            <#--</dd>-->

                            <dd class="expected-interest-dd tc" <#if loan.loanStatus == "PREHEAT">style="display: none"</#if>>
                                <span>预计总收益：</span>
                                <span class="principal-income">0.00</span>
                                <#--<span class="experience-income"></span>-->
                                元
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
                                            <i class="fa fa-question-circle text-m" title="开通后您可以简化投资过程，投资快人一步"></i>
                                        </a>
                                    </dd>
                                </#if>
                            </@global.role>

                            <input type="hidden" value="${loan.investor.authenticationRequired?c}" id="isAuthenticationRequired">
                            <input type="hidden" value="${loan.investor.anxinUser?c}" id="isAnxinUser">
                            <@global.role hasRole="'INVESTOR'">
                            <#if !loan.investor.anxinUser>
                            <dd class="skip-group">

                                <span class="init-checkbox-style on">
                                     <input type="checkbox" id="skipCheck" class="default-checkbox" checked>
                                 </span>
                                <div class="skip-text">
                                   我已阅读并同意<a href="javascript:void(0)"><span class="anxin_layer link-agree-service">《安心签平台服务协议》</span>、<span class="anxin_layer link-agree-privacy">《隐私条款》</span>、<span class="anxin_layer link-agree-number">《CFCA数字证书服务协议》</span>和<span class="anxin_layer link-agree-number-authorize">《CFCA数字证书授权协议》</span>
                                    <span class="check-tip" id="checkTip">请勾选</span></a>
                                </div>
                            </dd>
                            </#if>
                            </@global.role>
                        </dl>
                    </form>
                    <form name="isPaySuccess" id="isPaySuccess" method="post" style="display: none">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>
                </#if>
                <#if ["REPAYING", "RECHECK", "CANCEL", "OVERDUE", "COMPLETE"]?seq_contains(loan.loanStatus)>
                    <form action="/loan-list" method="get">
                        <dl class="account-list">
                            <dd>
                                <span class="img-status ${loan.loanStatus?lower_case}"></span>
                            </dd>
                            <dd>
                                <button class="btn-pay btn-normal" type="submit">查看其他项目</button>
                            </dd>
                        </dl>
                    </form>
                </#if>
            </div>
        </div>
        <div class="bg-w clear-blank borderBox loan-detail" id="loanDetailSwitch">
            <div class="loan-nav">
                <ul class="clearfix">
                    <li class="active">借款详情</li>
                    <li>出借记录</li>
                </ul>
            </div>
            <div class="loan-list pad-s invest-list-content">
                <div class="loan-list-con">
                    <div class="content detail">
                        <#if loan.introduce??>
                            <div class="subtitle">
                                <h3>项目介绍</h3>
                            </div>
                            <div class="container-fluid list-block clearfix">
                                ${loan.introduce}
                            </div>
                        </#if>
                        <#if loan.loanerDetail??>
                            <div class="subtitle">
                                <h3>借款人基本信息</h3>
                            </div>
                            <div class="container-fluid list-block clearfix">
                                <div class="row">
                                    <#list ['借款人', '性别', '年龄', '婚姻状况', '身份证号', '申请地区', '收入水平', '就业情况', '借款用途', '逾期率', '还款来源'] as key>
                                        <#if (loan.loanerDetail[key])?? && loan.loanerDetail[key] != '' && loan.loanerDetail[key] != '不明' >
                                            <div class="col-md-4"><div class="col-md-3">${key}：</div><div class="col-md-9">${loan.loanerDetail[key]}</div></div>
                                        </#if>
                                    </#list>
                                </div>
                            </div>
                        </#if>

                        <#if loan.basicInfo??>
                            <div class="subtitle">
                                <h3>借款用途描述</h3>
                            </div>
                            <div class="container-fluid list-block clearfix">
                                <p>${loan.basicInfo}</p>
                            </div>
                        </#if>

                        <#if "HOUSE" == loan.pledgeType>
                            <#list loan.pledgeHouseDetailList as pledgeHouseDetail>
                                <div class="subtitle">
                                    <h3>抵押档案<#if (loan.pledgeHouseDetailList?size > 1)>${pledgeHouseDetail_index+1}</#if></h3>
                                </div>
                                <div class="container-fluid list-block clearfix">
                                    <div class="row">
                                        <#if pledgeHouseDetail??>
                                            <#list ['抵押物所在地', '房屋面积', '房产证编号', '房权证编号', '不动产登记证明', '公证书编号'] as key>
                                                <#if pledgeHouseDetail[key]?? && pledgeHouseDetail[key] != ''>
                                                    <div class="col-md-4">${key}：${pledgeHouseDetail[key]}</div>
                                                </#if>
                                            </#list>
                                        </#if>
                                    </div>
                                </div>

                            </#list>
                        </#if>

                        <#if "VEHICLE" == loan.pledgeType>
                            <#list loan.pledgeVehicleDetailList as pledgeVehicleDetail>
                                <div class="subtitle">
                                    <h3>抵押档案<#if (loan.pledgeVehicleDetailList?size > 1)>${pledgeVehicleDetail_index+1}</#if></h3>
                                </div>
                                <div class="container-fluid list-block clearfix">
                                    <div class="row">
                                        <#if pledgeVehicleDetail??>
                                            <#list ['抵押物所在地', '车辆品牌', '车辆型号'] as key>
                                                <#if pledgeVehicleDetail[key]?? && pledgeVehicleDetail[key] != ''>
                                                    <div class="col-md-4">${key}：${pledgeVehicleDetail[key]}</div>
                                                </#if>
                                            </#list>
                                        </#if>
                                    </div>
                                </div>

                            </#list>
                        </#if>

                        <#if ["ENTERPRISE_CREDIT","ENTERPRISE_PLEDGE"]?seq_contains(loan.pledgeType)>
                            <div class="subtitle">
                                <h3>借款人基本信息</h3>
                            </div>
                            <div class="container-fluid list-block clearfix">
                                <div class="row">
                                    <#if loan.loanerEnterpriseDetailsInfo??>
                                        <#list ['借款人', '公司所在地','企业借款用途描述', '还款来源'] as key>
                                            <#if loan.loanerEnterpriseDetailsInfo[key]?? && loan.loanerEnterpriseDetailsInfo[key] != ''>
                                                <div class="col-md-6">${key}：${loan.loanerEnterpriseDetailsInfo[key]}</div>
                                            </#if>
                                        </#list>

                                    </#if>
                                </div>
                            </div>
                        </#if>

                        <#if "ENTERPRISE_PLEDGE" == loan.pledgeType>
                            <#list loan.pledgeEnterpriseDetailList as pledgeEnterpriseDetail>
                                <div class="subtitle">
                                    <h3>抵押物信息<#if (loan.pledgeEnterpriseDetailList?size > 1)>${pledgeEnterpriseDetail_index+1}</#if></h3>
                                </div>
                                <div class="container-fluid list-block clearfix">
                                    <div class="row">
                                        <#if pledgeEnterpriseDetail??>
                                            <#list ['担保方式', '抵押物所在地'] as key>
                                                <#if pledgeEnterpriseDetail[key]?? && pledgeEnterpriseDetail[key] != ''>
                                                    <div class="col-md-12">${key}：${pledgeEnterpriseDetail[key]}</div>
                                                </#if>
                                            </#list>
                                        </#if>
                                    </div>
                                </div>

                            </#list>

                        </#if>

                        <#if loan.pledgeType == "ENTERPRISE_FACTORING">
                            <div class="subtitle">
                                <h3>借款企业信息</h3>
                            </div>
                            <div class="container-fluid list-block clearfix">
                                <div class="row">
                                    <#if loan.enterpriseInfo??>
                                        <#list ['企业名称', '经营地址'] as key>
                                            <#if loan.enterpriseInfo[key]?? && loan.enterpriseInfo[key] != ''>
                                                <div class="col-md-6">${key}：${loan.enterpriseInfo[key]}</div>
                                            </#if>
                                        </#list>
                                        <#list ['借款用途', '还款来源'] as key>
                                            <#if loan.enterpriseInfo[key]?? && loan.enterpriseInfo[key] != ''>
                                                <div class="col-md-6">${key}：${loan.enterpriseInfo[key]}</div>
                                            </#if>
                                        </#list>
                                    </#if>
                                </div>
                            </div>

                            <div class="subtitle">
                                <h3>保理公司基本信息</h3>
                            </div>
                            <div class="container-fluid list-block clearfix">
                                <div class="row">
                                    <#if loan.enterpriseInfo??>
                                        <#list ['公司名称','公司简介'] as key>
                                            <#if loan.enterpriseInfo[key]?? && loan.enterpriseInfo[key] != ''>
                                                <div class="col-md-12">${key}：${loan.enterpriseInfo[key]}</div>
                                            </#if>
                                        </#list>
                                    </#if>
                                </div>
                            </div>
                        </#if>

                        <#if loan.pledgeType == "ENTERPRISE_BILL">
                            <div class="subtitle">
                                <h3>借款企业信息</h3>
                            </div>
                            <div class="container-fluid list-block clearfix">
                                <div class="row">
                                    <#if loan.enterpriseInfo??>
                                        <#list ['企业名称', '经营地址'] as key>
                                            <#if loan.enterpriseInfo[key]?? && loan.enterpriseInfo[key] != ''>
                                                <div class="col-md-6">${key}：${loan.enterpriseInfo[key]}</div>
                                            </#if>
                                        </#list>
                                        <#list ['借款用途', '还款来源'] as key>
                                            <#if loan.enterpriseInfo[key]?? && loan.enterpriseInfo[key] != ''>
                                                <div class="col-md-6">${key}：${loan.enterpriseInfo[key]}</div>
                                            </#if>
                                        </#list>
                                    </#if>
                                </div>
                            </div>
                        </#if>

                        <div class="subtitle">
                            <h3>风控审核</h3>
                        </div>
                        <div class="container-fluid danger-control clearfix">
                            <div class="row">
                                <#if loan.pledgeType == 'ENTERPRISE_CREDIT' || loan.pledgeType == 'ENTERPRISE_PLEDGE' >
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
                                <#elseif loan.pledgeType == 'ENTERPRISE_FACTORING'>
                                    <div class="col-md-6">
                                        <div class="container-fluid table">
                                            <div class="row">
                                                <div class="col-xs-6 bg">实地认证</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 bg">应收账质押合同</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">企业征信报告</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">验资报告</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="container-fluid table">
                                            <div class="row">
                                                <div class="col-xs-6 bg">不动产明细</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 bg">企业授信余额</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">银行流水查证</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">税务缴纳</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                            </div>
                                        </div>
                                    </div>

                                <#elseif loan.pledgeType == 'ENTERPRISE_BILL'>
                                    <div class="col-md-6">
                                        <div class="container-fluid table">
                                            <div class="row">
                                                <div class="col-xs-6 bg">实地认证</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 bg">商业汇票</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">企业征信报告</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">验资报告</div>
                                                <div class="col-xs-6 br-r br-b"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="container-fluid table">
                                            <div class="row">
                                                <div class="col-xs-6 bg">不动产明细</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 bg">企业授信余额</div>
                                                <div class="col-xs-6 br-r"><i class="fa fa-check-circle-o" aria-hidden="true"></i>已认证</div>
                                                <div class="col-xs-6 br-b bg">银行流水查证</div>
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
                        <div class="apply-data clearfix">
                            <#list loan.loanTitles as loanTitleRelation >
                                <#list loan.loanTitleDto as loanTitle>
                                    <#if loanTitle.id == loanTitleRelation.titleId>
                                        <h5>${loanTitle.title}</h5>
                                        <div class="scroll-wrap" scroll-carousel>
                                            <div class="scroll-content">
                                                <div class="row">
                                                    <#list loanTitleRelation.applicationMaterialUrls?split(",") as title>
                                                        <a class="col" href="${commonStaticServer}${title}" rel="example_group">
                                                            <img class="img" layer-src="${commonStaticServer}${title}" src="${commonStaticServer}${title}" alt="${loanTitle.title}"/>
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
                        <#--<#if loan.achievement??>-->
                        <#--<div class="row title-list">-->
                            <#--<div class="col-md-4">-->
                                <#--<div class="br">-->
                                    <#--<div class="item">-->
                                        <#--<h4 class="first"><span><a href="/activity/invest-achievement" target="_blank">拓荒先锋 >></a></span></h4>-->
                                        <#--<#if (loan.achievement.firstInvestAchievementMobile)??>-->
                                            <#--<p>-->
                                                <#--恭喜${loan.achievement.firstInvestAchievementMobile} <#if (loan.achievement.firstInvestAchievementDate)??>${loan.achievement.firstInvestAchievementDate?string("yyyy-MM-dd HH:mm:dd")}</#if>-->
                                                <#--拔得头筹 奖励0.2％加息券＋50元投资红包</p>-->
                                        <#--<#else>-->
                                            <#--<p>虚位以待</p>-->
                                        <#--</#if>-->
                                    <#--</div>-->
                                <#--</div>-->
                            <#--</div>-->
                            <#--<div class="col-md-4">-->
                                <#--<div class="br">-->
                                    <#--<div class="item">-->
                                        <#--<h4 class="king"><span><a href="/activity/invest-achievement" target="_blank">拓天标王 >></a></span></h4>-->
                                        <#--<#if (loan.achievement.maxAmountAchievementMobile)??>-->
                                            <#--<p>恭喜${loan.achievement.maxAmountAchievementMobile} 以累计投资${loan.achievement.maxAmountAchievementAmount}元 <#if loan.loanStatus == 'RAISING'><span-->
                                                    <#--class="text-lighter">(待定)</span></#if>夺得标王 奖励0.5％加息券＋100元投资红包</p>-->
                                        <#--<#else>-->
                                            <#--<p>虚位以待</p>-->
                                        <#--</#if>-->
                                    <#--</div>-->
                                <#--</div>-->
                            <#--</div>-->
                            <#--<div class="col-md-4">-->
                                <#--<div class="br">-->
                                    <#--<div class="item">-->
                                        <#--<h4 class="hammer"><span><a href="/activity/invest-achievement" target="_blank">一锤定音 >></a></span></h4>-->
                                        <#--<#if (loan.achievement.lastInvestAchievementMobile)??>-->
                                            <#--<p>-->
                                                <#--恭喜${loan.achievement.lastInvestAchievementMobile} <#if (loan.achievement.lastInvestAchievementDate)??>${loan.achievement.lastInvestAchievementDate?string("yyyy-MM-dd HH:mm:dd")}</#if>-->
                                                <#--终结此标 奖励0.2％加息券＋50元投资红包</p>-->
                                        <#--<#else>-->
                                            <#--<p>目前项目剩余${(loan.amountNeedRaised / 100)?string("0.00")}元快来一锤定音吧</p>-->
                                        <#--</#if>-->
                                    <#--</div>-->
                                <#--</div>-->
                            <#--</div>-->
                        <#--</div>-->
                        <#--</#if>-->
                        <div class="table-box">

                        </div>
                        <div class="pagination" data-url="/loan/${loan.id?string.computer}/invests" data-page-size="10">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="authorizeAgreementOptions" class="pad-s-tb tl fl hide">
        <p class="mb-0 text-m color-title">请在新打开的富滇银行完成操作后选择：</p>
        <p class="text-m"><span class="title-text">授权成功：</span><span class="go-on-btn success_go_on_invest">继续投资</span><span class="color-tip">（授权后可能会有几秒的延迟）</span></p>
        <p class="mb-0"><span class="title-text">授权失败： </span><span class="again-btn">重新授权</span><span class="btn-lr">或</span><span class="go-on-btn fail_go_on_invest">继续投资</span></p>
        <p class="text-s color-title">遇到问题请拨打我们的客服热线：400-169-1188（工作日9:00-20:00）</p>
    </div>
    <form action="/agreement" id="goAuthorize" method="post" target="_blank">
        <input type="hidden" name="noPasswordInvest" value="true"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <#include "component/anxin-qian.ftl" />
    <#include "component/anxin-agreement.ftl" />
    <#include "component/coupon-alert.ftl" />
</div>
    <#include "component/red-envelope-float.ftl" />
    <#include "component/login-tip.ftl" />
<#--风险测评-->
<div id="riskAssessmentFormSubmit" class="pad-m popLayer" style="display: none; padding-top:50px;padding-bottom: 0">
    <div class="tc text-m">根据监管要求，出借人在出借前需进行投资偏好评估，取消则默认为保守型（可承受风险能力为最低）。是否进行评估？</div>
    <#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
    <div class="tc person-info-btn" style="margin-top:40px;">
        <button id="cancelAssessmentFormSubmit" class="btn  btn-cancel btn-close btn-close-turn-on" type="button">取消</button>&nbsp;&nbsp;&nbsp;
        <button id="confirmAssessment" class="btn btn-success btn-turn-off" type="button">确认</button>
    </div>
</div>
<div id="bankCardDOM" class="pad-m popLayer" style="display: none; padding-top:20px;padding-bottom: 0">

    <div class="tc text-m">您还没绑卡，请先进行绑卡</div>
<#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
    <form action="${requestContext.getContextPath()}/bank-card/bind/source/WEB" method="post" style="display: block">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <div class="tc person-info-btn" style="margin-top:40px;">
            <button class="btn  btn-cancel btn-close" type="button">取消</button>&nbsp;&nbsp;&nbsp;
            <input id="accountBtn" class="btn btn-success" value="确定" type="submit"/>
        </div>
    </form>

</div>


<script type="text/template" id="LendTemplate">
    <table class="invest-list table-striped">
            <thead>
            <tr>
            <th>出借人</th>
            <th class="tr">出借金额（元）</th>
    <th class="responsive-hide">出借方式</th>
            <th class="responsive-hide tr">预期收益（元）</th>
    <th>出借时间</th>
    </tr>
    </thead>
    <tbody>
    <% for(var i = 0; i < records.length; i++) {
    var item = records[i];
    %>
    <tr>
    <td class="loan-td">
        <%=item.mobile%>
    </td>
    <td class="tr">
        <%=item.amount%>
    </td>
    <td class="responsive-hide">
        <span class="invest-<%=item.source%>"></span>
    </td>
    <td class="responsive-hide tr">
        <%=item.expectedInterest%>
    </td>
    <td>
        <%=item.createdTime%>
    </td>
    </tr>
    <% } %>
    </tbody>
    </table>

</script>
</@global.main>