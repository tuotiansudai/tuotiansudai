<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="userTiandou" title="用户天豆查询">

<!-- content area begin -->
<div class="col-md-10">

    <form action="/activity-manage/user-tiandou" class="form-inline query-build" method="post">
        <div class="form-group">
            <label for="loginName">用户名</label>
            <input type="text" id="login-name" name="loginName" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${loginName!}"/>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input class="btn btn-default btn-submit" type="submit" value="查询">
    </form>

    <if loginName ?? && loginName!="">
        用户名：${loginName!}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

        可用天豆：${usableTianDou!0}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

        排名：${rank!"无"}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

        累计天豆：${totalScore!}
    </if>
    </br>
    </br>
    </br>
</div>
    <#if useRecordList ?? && (useRecordList?size>0)>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>时间</th>
                <th>行为</th>
                <th>投资额（元）</th>
                <th>天豆数</th>
                <th>备注</th>
            </tr>
            </thead>
            <tbody>
                <#list useRecordList as record>
                <tr>
                    <td>${record.time!}</td>
                    <td>${record.type!}</td>
                    <td>${record.amount/100?float}</td>
                    <td>${record.score!}</td>
                    <td>
                        <#if record.type=="DRAW">${record.prize.name!}</#if>
                        <#if record.type=="INVEST">${record.desc!}</#if>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    <#else>
    <div class="col-md-10">
        没有天豆记录
    </div>
    </#if>

</@global.main>