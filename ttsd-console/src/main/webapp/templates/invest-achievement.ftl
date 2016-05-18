<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="invest-achievement.js" headLab="activity-manage" sideLab="investAchievement" title="投资称号管理">

<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="loginName">用户名</label>
            <input type="text" id="loginName" name="loginName" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${loginName!}" />
        </div>
        <button type="submit" class="btn btn-sm btn-primary">查询</button>
    </form>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
                <tr>
                    <th>项目名称</th>
                    <th>项目状态</th>
                    <th>借款期限</th>
                    <th>项目金额</th>
                    <th>拓天标王</th>
                    <th>拓荒先锋</th>
                    <th>一锤定音</th>
                    <th>满标日期</th>
                    <th>首投用时</th>
                    <th>满标用时</th>
                </tr>
            </thead>
            <tbody>
                <#list investAchievementDtos as investAchievementDto>
                    <tr>
                        <td>${investAchievementDto.name!}</td>
                        <td>${investAchievementDto.loanStatus.description!}</td>
                        <td>${investAchievementDto.periods}</td>
                        <td>${(investAchievementDto.loanAmount/100)?string('0.00')!}</td>
                        <#if investAchievementDto.maxAmountLoginName??>
                            <td>${investAchievementDto.maxAmountLoginName!}/${(investAchievementDto.maxAmount/100)?string('0.00')!}</td>
                        <#else>
                            <td>/</td>
                        </#if>
                        <#if investAchievementDto.firstInvestLoginName??>
                            <td>${investAchievementDto.firstInvestLoginName!}/${(investAchievementDto.firstInvestAmount/100)?string('0.00')!}</td>
                        <#else>
                            <td>/</td>
                        </#if>
                        <#if investAchievementDto.lastInvestLoginName??>
                            <td>${investAchievementDto.lastInvestLoginName!}/${(investAchievementDto.lastInvestAmount/100)?string('0.00')!}</td>
                        <#else>
                            <td>/</td>
                        </#if>
                        <#if investAchievementDto.raisingCompleteTime??>
                            <td>${investAchievementDto.raisingCompleteTime?string('yyyy-MM-dd HH:mm:ss')!}</td>
                        <#else>
                            <td>/</td>
                        </#if>
                        <td>${investAchievementDto.whenFirstInvest!}</td>
                        <td>${investAchievementDto.whenCompleteInvest!}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <nav class="pagination-control">
        <div>
            <span class="bordern">总共${investAchievementCount?string('0')}条,每页显示${pageSize}条</span>
        </div>
        <ul class="pagination pull-left">
            <li>
                <#if hasPreviousPage >
                <a href="?loginName=${loginName!}&pageSize=${pageSize}&index=${index-1}"
                   aria-label="Previous">
                <#else>
                <a href="#" aria-label="Previous">
                </#if>
                <span aria-hidden="true">&laquo; Prev</span>
                </a>
            </li>
            <li><a>${index}</a></li>
            <li>
                <#if hasNextPage >
                <a href="?loginName=${loginName!}&pageSize=${pageSize}&index=${index+1}"
                   aria-label="Next">
                <#else>
                <a href="#" aria-label="Next">
                </#if>
                <span aria-hidden="true">Next &raquo;</span>
                </a>
            </li>
        </ul>
        <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
    </nav>
</div>

</@global.main>