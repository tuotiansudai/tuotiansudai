<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="risk-estimate.js" headLab="user-manage" sideLab="riskEstimate" title="用户投资偏好">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="project">风险偏好类型</label>
            <select class="selectpicker" name="selectedEstimate">
                <option value="">全部</option>
                <#list estimateOptions as estimateOption>
                    <option value="${estimateOption.name()}" <#if (selectedEstimate?? && estimateOption == selectedEstimate)>selected</#if>>${estimateOption.getType()}</option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="project">家庭年收入</label>
            <select class="selectpicker" name="selectedIncome">
                <option value="">全部</option>
                <#list incomeOptions as incomeOption>
                    <option value="${incomeOption.name()}" <#if (selectedIncome?? && incomeOption == selectedIncome)>selected</#if>>${incomeOption.getDesc()}</option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="project">投资期望利率</label>
            <select class="selectpicker" name="selectedRate">
                <option value="">全部</option>
                <#list rateOptions as rateOption>
                    <option value="${rateOption.name()}" <#if (selectedRate?? && rateOption == selectedRate)>selected</#if>>${rateOption.getDesc()}</option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="project">计划投资期限</label>
            <select class="selectpicker" name="selectedDuration">
                <option value="">全部</option>
                <#list durationOptions as durationOption>
                    <option value="${durationOption.name()}" <#if (selectedDuration?? && durationOption == selectedDuration)>selected</#if>>${durationOption.getDesc()}</option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="project">年龄区间</label>
            <select class="selectpicker" name="selectedAge">
                <option value="">全部</option>
                <#list ageOptions as ageOption>
                    <option value="${ageOption.name()}" <#if (selectedAge?? && ageOption == selectedAge)>selected</#if>>${ageOption.getDesc()}</option>
                </#list>
            </select>
        </div>


        <button type="submit" class="btn btn-sm btn-primary">查询</button>
        <button type="reset" class="btn btn-sm btn-default">重置</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户名</th>
                <th>手机号</th>
                <th>姓名</th>
                <th>注册时间</th>
                <th>用户在投金额</th>
                <th>风险偏好类型</th>
                <th>家庭年收入</th>
                <th>投资期望利率</th>
                <th>计划投资金额</th>
                <th>计划投资期限</th>
                <th>年龄区间</th>
            </tr>
            </thead>
            <tbody>
                <#list pagination.records as item>
                <tr>
                    <td>${(item.loginName)!}</td>
                    <td>${(item.mobile)!}</td>
                    <td>${(item.userName)!}</td>
                    <td>${item.registerTime?string('yyyy-MM-dd HH:mm')}</td>
                    <td>${(item.investingAmount/100)?string["0.##"]} 元</td>
                    <td>${item.estimate.getType()}</td>
                    <td><#if item.income??>${item.income.getDesc()}</#if></td>
                    <td><#if item.rate??>${item.rate.getDesc()}</#if></td>
                    <td><#if item.income??>${(item.investment.value * item.income.value)} 万元</#if></td>
                    <td><#if item.duration??>${item.duration.getDesc()}</#if></td>
                    <td><#if item.age??>${item.age.getDesc()}</#if></td>
                </tr>
                <#else>
                <tr>
                    <td colspan="11">Empty</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if pagination.records?has_content>
            <div>
                <span class="bordern">总共${pagination.count}条,每页显示${pagination.pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if pagination.hasPreviousPage >
                    <a href="?selectedEstimate=${selectedEstimate!}&selectedIncome=${selectedIncome!}&selectedRate=${selectedRate!}&selectedDuration=${selectedDuration!}&selectedAge=${selectedAge!}&index=${pagination.index-1}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${pagination.index}</a></li>
                <li>
                    <#if pagination.hasNextPage >
                    <a href="?selectedEstimate=${selectedEstimate!}&selectedIncome=${selectedIncome!}&selectedRate=${selectedRate!}&selectedDuration=${selectedDuration!}&selectedAge=${selectedAge!}&index=${pagination.index+1}"
                       aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>
                </li>
            </ul>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>