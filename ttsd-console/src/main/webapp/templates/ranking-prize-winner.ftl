<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="tiandouPrize" title="中奖用户列表">

<!-- content area begin -->
<div style="margin: 20px 30px 20px 253px">
    中奖人数：${drawCount!0}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</div>

    <#if prizeWinnerDtoList ?? && (prizeWinnerDtoList?size>0)>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>中奖时间</th>
                <th>用户名</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>身份证号</th>
            </tr>
            </thead>
            <tbody>
                <#list prizeWinnerDtoList as winner>
                <tr>
                    <td>${winner.time!}</td>
                    <td>${winner.loginName!}</td>
                    <td>${winner.realName!}</td>
                    <td>${winner.mobile!}</td>
                    <td>${winner.identityNumber!}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    <#else>
    <div class="col-md-10">
        没有中奖记录
    </div>
    </#if>
</@global.main>