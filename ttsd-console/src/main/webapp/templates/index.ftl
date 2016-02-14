<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="sys-manage" sideLab="myTasks" title="系统首页">
<div class="col-md-10 home-report">

    我的任务： </br>
    <#list taskList as task>
        <#if task.taskType == 'TASK'>
            <a href="${task.operateURL}">${task.description!}</a> 同意｜拒绝
        </#if>
        <#if task.taskType == 'NOTIFY'>
            <a href="${task.operateURL}">${task.description!}</a> 知道了
        </#if>
    </#list>

    平台数据： </br>
    今日新增用户数${userToday!} 本周新增用户数${user7Days!}  本月新增用户数${user30Days!} </br>

    今日充值金额${rechargeToday!} 本周充值金额${recharge7Days!}  本月充值金额${recharge30Days!} </br>

    今日提现金额${withdrawToday!} 本周提现金额${withdraw7Days!}  本月提现金额${withdraw30Days!} </br>

    今日投资金额${investToday!} 本周投资金额${invest7Days!}  本月投资金额${invest30Days!} </br>

    平台累计交易额:${totalInvest!} </br>

</div>

</@global.main>