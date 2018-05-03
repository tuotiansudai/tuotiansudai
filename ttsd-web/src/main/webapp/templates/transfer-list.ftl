<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.transfer_list}" pageJavascript="${js.transfer_list}" activeNav="我要投资" activeLeftNav="转让项目" title="债权转让_转让项目_拓天速贷" keywords="债权转让,债务转移,债权投资,金融债权,债权购买" description="拓天速贷债权转让项目,帮助用户提高投资的流动性,安全的债权转让产品提高用户的信任度,拓天速贷让用户资金灵活,收益最大化.">
<div class="transfer-list-content clearfix">
   <ul class="wrapper-list" id="wrapperList">
            <li class="project-kind">
                <span>项目状态: </span>
                <#assign statusUrl = "/transfer-list?transferStatus={transferStatus}&rateStart=${rateStart!}&rateEnd=${rateEnd!}">
                <#assign statusMap = {"":"全部","TRANSFERRING":"转让中","SUCCESS":"已完成"}>
                <#assign statusKeys = statusMap?keys>
                <#list statusKeys as key>
                    <a <#if transferStatus?? && transferStatus == key>class="active"
                       <#elseif !(transferStatus??) && key=="">class="active"</#if>
                       href=${statusUrl?replace("{transferStatus}",key)}>${statusMap[key]}</a>
                </#list>
                <div class="safety-notification">出借人适当性管理告知<i id="noticeBtn" class="fa fa-question-circle" aria-hidden="true"></i></div>
                <div class="notice-tips extra-rate-popup" style="display: none">
                    参与网络借贷的出借人，应当具备投资风险意识、风险识别能力，拥有非保本类金融产品投资的经历并熟悉互联网。请您在出借前，确保了解融资项目信贷风险，确认具有相应的风险认知和承受能力，并自行承担借贷产生的本息损失。
                </div>
                <em class="show-more">更多 <i class="fa fa-angle-down"></i> </em>
            </li>
            <li>
                <span>约定年化利率: </span>
                <#assign rateUrl = "/transfer-list?transferStatus=${transferStatus!}&{rateType}">
                <#assign rateMap = {"":"全部","rateStart=0&rateEnd=0.08":"8%以下","rateStart=0.08&rateEnd=0.1":"8-10%","rateStart=0.1&rateEnd=0":"10%以上"}>
                <#assign rateKeys = rateMap?keys>
                <#list rateKeys as key>
                    <a <#if rateStart == 0 && rateEnd == 0 && key=="">class="active"
                       <#elseif rateStart == 0 && rateEnd == 0.08 && rateMap[key]=="8%以下">class="active"
                       <#elseif rateStart == 0.08 && rateEnd == 0.1  && rateMap[key]=="8-10%">class="active"
                       <#elseif rateStart == 0.10 && rateEnd == 0 && rateMap[key]=="10%以上">class="active"
                    </#if>
                       href=${rateUrl?replace("{rateType}",key)}>${rateMap[key]}</a>
                </#list>
            </li>
        </ul>
    <div class="transfer-list-box" id="transferListBox">
        <ul>
            <#if transferApplicationItemList??>
                <#list transferApplicationItemList as transferApplicationItem>
                    <li data-url="/transfer/${(transferApplicationItem.transferApplicationId)!}" data-url-id="${(transferApplicationItem.transferApplicationId)!}" data-url-status="${transferApplicationItem.transferStatus}" class="clearfix">
                        <div class="transfer-info-frame fl">
                            <div class="transfer-top">
                                <span class="l-title fl">${transferApplicationItem.name!}</span>
                            </div>
                            <div class="transfer-info-dl clearfix">
                                <dl>
                                    <dt>转让价格</dt>
                                    <dd><em><@percentInteger>${transferApplicationItem.transferAmount!}</@percentInteger></em>
                                        <i><@percentFraction>${transferApplicationItem.transferAmount!}</@percentFraction>
                                        </i>元
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>项目本金</dt>
                                    <dd><em><@percentInteger>${transferApplicationItem.investAmount!}</@percentInteger></em>
                                        <i><@percentFraction>${transferApplicationItem.investAmount!}</@percentFraction>
                                        </i>元
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>约定年化利率</dt>
                                    <dd><em>${transferApplicationItem.baseRate!}%</em></dd>
                                </dl>
                                <dl>
                                    <dt>剩余天数</dt>
                                    <dd><em>${transferApplicationItem.leftDays!}</em></dd>
                                </dl>
                            </div>
                        </div>
                        <div class="transfer-right">
                            <#if (transferApplicationItem.transferStatus == "SUCCESS")>
                                <div class="transfer-time">
                                    <span>转让完成时间：${transferApplicationItem.transferTime?string("yyyy-MM-dd HH:mm:ss")}</span>
                                </div>
                                <div class="rest-amount">
                                    <button class="btn-normal" disabled="">已转让</button>
                                </div>
                            <#else>
                                <div class="transfer-time">
                                    <span>截止时间：${transferApplicationItem.deadLine?string("yyyy-MM-dd HH:mm:ss")}</span>
                                </div>
                                <div class="rest-amount">
                                    <i class="btn-invest btn-normal">马上投资</i>
                                </div>
                            </#if>

                        </div>
                    </li>
                </#list>
            </#if>
        </ul>
        <div class="pagination">
            <span class="total">共 <span class="subTotal">${count}</span> 条记录，当前第 <span class="index-page">${index}</span> 页</span>
            <span class="prev page-list <#if hasPreviousPage>active</#if>"
                  data-url="/transfer-list?transferStatus=${transferStatus!}&rateStart=${rateStart!}&rateEnd=${rateEnd!}&index=${index - 1}">上一页</span>
            <span class="next page-list <#if hasNextPage>active</#if>"
                  data-url="/transfer-list?transferStatus=${transferStatus!}&rateStart=${rateStart!}&rateEnd=${rateEnd!}&index=${index + 1}">下一页</span>
        </div>
    </div>
    <#include "component/coupon-alert.ftl" />
</div>
    <#include "component/red-envelope-float.ftl" />
</@global.main>