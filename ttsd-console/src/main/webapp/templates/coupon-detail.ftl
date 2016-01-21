<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="coupon-detail.js" headLab="activity-manage" sideLab="${sideLabType!}" title="体验券数据统计">

<div class="col-md-10">

    <form action="" class="form-inline query-build">
        <div class="row">

            <div class="form-group">
                <label for="control-label">用户名</label>
                <input type="text" class="form-control jq-loginName" value="${loginName!}" name="loginName">
            </div>

            <div class="form-group">
                <label for="control-label">注册时间</label>

                <div class='input-group date' id="registerDateBegin">
                    <input type='text' class="form-control jq-startTime" value="${(registerStartTime?string('yyyy-MM-dd HH:mm:ss'))!}" name="registerStartTime"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
                -
                <div class='input-group date' id="registerDateEnd">
                    <input type='text' class="form-control jq-endTime" value="${(registerEndTime?string('yyyy-MM-dd HH:mm:ss'))!}" name="registerEndTime"/>
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
                <select class="selectpicker" name="isUsed">
                    <option value="" <#if !(isUsed??)>selected</#if>>全部</option>
                    <option value="true" <#if isUsed?? && isUsed>selected</#if>>已使用</option>
                    <option value="false" <#if isUsed?? && !isUsed>selected</#if>>未使用</option>
                </select>
            </div>

            <button class="btn btn-primary search" type="submit">查询</button>
            <button class="btn btn-default reset" type="reset">重置</button>

        </div>
    </form>

    <div class="table-responsive">

            <input type="hidden" value="${couponId?string('0')}" class="coupon-id">

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


    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${userCouponsCount}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="/activity-manage/coupon/${couponId?string('0')}/detail?isUsed=${isUsed}&loginName=${loginName!}&registerStartTime=${(registerStartTime?string('yyyy-MM-dd HH:mm:ss'))!}&registerEndTime=${(registerEndTime?string('yyyy-MM-dd HH:mm:ss'))!}&mobile=${mobile?string('0')!}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="/activity-manage/coupon/${couponId?string('0')}/detail?isUsed=${isUsed}&loginName=${loginName!}&registerStartTime=${(registerStartTime?string('yyyy-MM-dd HH:mm:ss'))!}&registerEndTime=${(registerEndTime?string('yyyy-MM-dd HH:mm:ss'))!}&mobile=${mobile?string('0')!}&index=${index+1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span></a>
                </li>
            </ul>
        </nav>
    </div>

</div>

</@global.main>