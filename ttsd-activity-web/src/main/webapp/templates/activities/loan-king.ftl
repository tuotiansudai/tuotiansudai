<#import "../macro/global.ftl" as global>

<@global.main pageCss="${css.loan_king}" pageJavascript="${js.loan_king}" activeNav="" activeLeftNav="" title="标王争霸场_拓天周年庆_活动中心_拓天速贷" keywords="拓天速贷,拓天周年庆,京东E卡,红包奖励" description="拓天周年庆-标王争霸场活动,活动期间,每个债权针对用户在的累计投资额进行排名,前三名相应可获得100元京东E卡和红包奖励.">
<div class="loan-king-container" id="loanKingContainer">
    <div class="top-item compliance-banner">
        <img src="" width="100%" class="media-pc">
        <img src="" width="100%" class="media-phone">
        <div class="invest-tip tip-width">市场有风险，投资需谨慎！</div>
    </div>
    <div class="wp clearfix">
        <div class="reg-tag-current" style="display: none">
            <#include '../module/fast-register.ftl' />
        </div>
        <div class="content-item">
            <h3>活动期间，每个债权根据用户在该债权的累计投资额进行排名，前三名可获丰厚奖励。</h3>
            <div class="loan-list">
                <img src="" width="80%" class="media-pc">
                <img src="" width="90%" class="media-phone">
            </div>
        </div>
        <div class="loan-title"></div>
        <#if (loans?size > 0)>
            <#list loans as loan>
                <div class="current-loan">
                    <h3>${loan.name}
                        <#if loan.activity?string("true","false") == "true">
                            <span class="arrow-tag-normal" style="margin-top:0;float:none;">
                                    <i class="ic-left"></i>
                                    <em>${loan.activityDesc!}</em>
                                    <i class="ic-right"></i>
                                </span>
                        </#if>
                    </h3>
                    <ul class="loan-info">
                        <li class="tl">
                            <span>预期年化收益</span>
                            <span class="info-text">
                                <em><strong><@percentInteger>${loan.baseRate}</@percentInteger></strong></em>
                                        <i><@percentFraction>${loan.baseRate}</@percentFraction>
                                            <#if (loan.extraRate > 0)>
                                                ~ <@percentInteger>${loan.baseRate + loan.extraRate}</@percentInteger><@percentFraction>${loan.extraRate}</@percentFraction>
                                            </#if>
                                            <#if (loan.activityRate > 0)>
                                                +<@percentInteger>${loan.activityRate}</@percentInteger><@percentFraction>${loan.activityRate}</@percentFraction>
                                            </#if>%
                                        </i>
                                <#if loan.extraSource?? && loan.extraSource == "MOBILE">
                                    <i class="fa fa-mobile"></i>
                                </#if>
                            </span>
                        </li>
                        <li class="tc">
                            <span>项目期限</span>
                            <span class="info-text">最长<strong>${loan.duration}</strong>天</span>
                        </li>
                        <li class="tr">
                            <span>招募金额</span>
                            <span class="info-text"><strong><@amount>${loan.loanAmount?string.computer}</@amount></strong>元</span>
                        </li>
                    </ul>
                    <div class="progress-line">
				<span class="line-model">
					<strong style="width:${loan.progress}%"></strong>
				</span>
                        <span class="line-number">${loan.progress?string("0.00")} %</span>
                    </div>
                    <table class="loan-table">
                        <tbody>
                            <#list loan.achievementViews as view>
                            <tr>

                                <#switch view_index>
                                    <#case 0>
                                        <td class="icon-king">暂居标王</td>
                                        <#break >
                                    <#case 1>
                                        <td>第二名</td>
                                        <#break >
                                    <#case 2>
                                        <td>第三名</td>
                                        <#break >
                                </#switch>
                                <td>${view.loginName}</td>
                                <td>${view.amount/100}元</td>
                            </tr>
                            </#list>

                        </tbody>
                    </table>

                    <div class="loan-btn">
                        <a href="<#if !isAppSource>/loan/${loan.id?c}<#else>app/tuotian/invest-list</#if>">速抢标王</a>
                    </div>
                </div>

            </#list>
        <#else >
            <div class="current-loan">
                <div class="empty-item">
                    目前暂无相关产品，等等再来呦～
                </div>
            </div>
        </#if>
        <dl class="rule-item">
            <dt>温馨提示：</dt>
            <dd>1.本活动仅限直投项目，债权转让及新手专享项目不参与累计；</dd>
            <dd>2.用户所获红包奖励将于债权放款后即时发放，用户可在PC端“我的账户”或App端“我的”中进行查看；</dd>
            <dd>3.京东E卡将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；</dd>
            <dd>4.为了保证获奖结果的公平性，实物大奖获奖用户在活动期间所进行的所有投标不允许进行债权转让，否则奖品将不予发放；</dd>
            <dd>5.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有；</dd>
            <dd>6.理财有风险，投资需谨慎。</dd>
        </dl>
    </div>
</div>
</@global.main>