<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="coupon-detail.js" headLab="activity-manage" sideLab="${sideLabType!}" title="体验券数据统计">

<div class="col-md-10">
    <div class="form-group">
        <select class="selectpicker" data-link = '/activity-manage/coupon/${couponId?string('0')}/detail'>
            <option value="" <#if !(isUsed??)>selected</#if>>全部</option>
            <option value="true" <#if isUsed?? && isUsed>selected</#if>>已使用</option>
            <option value="false" <#if isUsed?? && !isUsed>selected</#if>>未使用</option>
        </select>
    </div>

    <div class="table-responsive">
            <table class="table table-bordered table-hover " style="width:80%;" >
                <thead>
                    <tr>
                        <th>用户名</th>
                        <th>使用时间</th>
                        <th>投资金额</th>
                        <th>项目编号</th>
                        <th>项目名称</th>
                    </tr>
                </thead>
                <tbody>
                    <#list userCoupons as userCoupon>
                    <tr>
                        <td>${userCoupon.loginName}</td>
                        <td>
                            <#if userCoupon.usedTime??>
                                ${userCoupon.usedTime?string('yyyy-MM-dd HH:mm')}
                            <#else>
                                未使用
                            </#if>
                        </td>
                        <td>
                            <#if userCoupon.investAmount??>
                                ${userCoupon.investAmount/100}
                            <#else>
                                未使用
                            </#if>
                        </td>
                        <td>
                            <#if userCoupon.loanId??>
                                ${userCoupon.loanId?string('0')}
                            <#else>
                                未使用
                            </#if>
                        </td>
                        <td>
                            <#if userCoupon.loanName??>
                                ${userCoupon.loanName!}
                            <#else>
                                未使用
                            </#if>
                        </td>
                    </tr>
                    </#list>
                </tbody>
            </table>
        </div>

</div>

</@global.main>