<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="experience-balance" headLab="experience-manage" sideLab="experienceBalance" title="用户体验金余额">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="control-label">投资人手机号：</label>
            <input type="text" class="form-control jq-loginName" name="mobile" value="${mobile!}">
        </div>
        <div class="form-group">
            <label for="control-label">余额：</label>
            <input type="text" class="form-control jq-balance-min" name="balanceMin" value="${balanceMin!}"
                   onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"
                   onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control jq-balance-max" name="balanceMax" value="${balanceMax!}"
                   onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"
                   onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <button class="btn btn-primary" type="submit">查询</button>
        <button class="btn btn-default" type="reset">重置</button>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th colspan="6">账户体验金余额共计:&nbsp;${sumExperienceBalance/100} 元 &nbsp;&nbsp;&nbsp;折算成本:&nbsp;${sumExperienceBalance/100} 元 </th>
            </tr>
            <tr>
                <th>用户名</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>地区</th>
                <th>最后交易时间</th>
                <th>账户体验金余额</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.loginName!''}</td>
                    <td>${item.userName!''}</td>
                    <td>${item.mobile}</td>
                    <td>${item.province!''}</td>
                    <td>${(item.lastExchangeTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${item.lastExchangeTime/100}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                    <a href="/experience-manage/balance?mobile=${mobile!}&balanceMin=${balanceMin!}&balanceMax=${balanceMax!}&index=${data.index-1}&pageSize=${data.pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage >
                    <a href="/experience-manage/balance?mobile=${mobile!}&balanceMin=${balanceMin!}&balanceMax=${balanceMax!}&index=${data.index+1}&pageSize=${data.pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span></a>
                </li>
            </ul>
        </nav>
    </div>
</div>
<!-- content area end -->
</@global.main>

