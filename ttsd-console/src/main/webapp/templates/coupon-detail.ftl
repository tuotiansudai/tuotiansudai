<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="coupon-detail.js" headLab="${headLab!}" sideLab="${sideLabType!}" title="体验券数据统计">

<div class="col-md-10">

    <form action="" class="form-inline query-build">
        <div class="row">

            <div class="form-group">
                <label for="control-label">用户名</label>
                <input type="text" class="form-control jq-loginName" value="${loginName!}" name="loginName">
            </div>

            <div class="form-group">
                <label for="control-label">投资时间</label>

                <div class='input-group date' id="registerDateBegin">
                    <input type='text' class="form-control jq-startTime" value="${(usedStartTime?string('yyyy-MM-dd HH:mm:ss'))!}" name="usedStartTime"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
                -
                <div class='input-group date' id="registerDateEnd">
                    <input type='text' class="form-control jq-endTime" value="${(usedEndTime?string('yyyy-MM-dd HH:mm:ss'))!}" name="usedEndTime"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
            </div>

            <div class="form-group">
                <label for="control-label">手机号</label>
                <input type="text" class="form-control jq-mobile" value="${mobile!}" name="mobile">
            </div>

            <div class="form-group">
                <label for="control-label">状态</label>
                <select class="selectpicker" name="isUsed">
                    <option value="" <#if !(isUsed??)>selected</#if>>全部</option>
                    <option value="true" <#if isUsed?? && isUsed>selected</#if>>已使用</option>
                    <option value="false" <#if isUsed?? && !isUsed>selected</#if>>未使用</option>
                </select>
            </div>

            <button class="btn btn-primary search" type="submit">查询</button>
            <button class="btn btn-default reset" type="reset">重置</button>

        </div>
        <input type="hidden" value="${couponId?string('0')}" name="couponId">
    </form>

    <div class="table-responsive">

            <input type="hidden" value="${couponId?string('0')}" class="coupon-id">
            <label for="control-label">投资金额汇总: ${investAmount /100}</label>
            <label for="control-label" style="margin-left: 20px;">年化投资收益汇总: ${interest /100}</label>
            <table class="table table-bordered table-hover " style="width:80%;" >
                <thead>
                    <tr>
                        <th>用户名</th>
                        <th>使用时间</th>
                        <th>投资金额</th>
                        <th>年化投资收益</th>
                        <th>项目期限</th>
                        <th>项目编号</th>
                        <th>项目名称</th>
                        <th>到期时间</th>
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
                            <#if userCoupon.investAmount?? && userCoupon.usedTime??>
                                ${userCoupon.investAmount/100}
                            <#else>
                                未使用
                            </#if>
                        </td>

                        <td>
                            <#if userCoupon.annualInterest?? && userCoupon.usedTime??>
                            ${userCoupon.annualInterest /100}
                            <#else>
                                未使用
                            </#if>
                        </td>

                        <td>
                            <#if userCoupon.productType?? && userCoupon.usedTime??>
                                ${userCoupon.productType.duration}天
                            <#else>
                                未使用
                            </#if>
                        </td>

                        <td>
                            <#if userCoupon.loanId?? && userCoupon.usedTime??>
                                ${userCoupon.loanId?string('0')}
                            <#else>
                                未使用
                            </#if>
                        </td>
                        <td>
                            <#if userCoupon.loanName?? && userCoupon.usedTime??>
                                ${userCoupon.loanName!}
                            <#else>
                                未使用
                            </#if>
                        </td>
                        <td>
                            <#if userCoupon.endTime?? && userCoupon.usedTime??>
                            ${userCoupon.endTime?string('yyyy-MM-dd HH:mm')}
                            <#else>
                                未使用
                            </#if>
                        </td>
                    </tr>
                    </#list>
                </tbody>
            </table>
        </div>


    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${userCouponsCount}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="/activity-manage/coupon/${couponId?string('0')}/detail?<#if isUsed??>isUsed=${isUsed?c}&</#if>loginName=${loginName!}&usedStartTime=${(usedStartTime?string('yyyy-MM-dd HH:mm:ss'))!}&usedEndTime=${(usedEndTime?string('yyyy-MM-dd HH:mm:ss'))!}&mobile=${mobile!}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="/activity-manage/coupon/${couponId?string('0')}/detail?<#if isUsed??>isUsed=${isUsed?c}&</#if>loginName=${loginName!}&usedStartTime=${(usedStartTime?string('yyyy-MM-dd HH:mm:ss'))!}&usedEndTime=${(usedEndTime?string('yyyy-MM-dd HH:mm:ss'))!}&mobile=${mobile!}&index=${index+1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span></a>
                </li>
            </ul>
    <@security.authorize access="hasAnyAuthority('DATA')">
            <button class="btn btn-default pull-left export-birthday-coupons" type="button">导出Excel</button>
    </@security.authorize>
        </nav>
    </div>

</div>

</@global.main>