<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="index.js" headLab="sys-manage" sideLab="myTasks" title="系统首页">
<div class="col-md-10 home-report">

    <div class="title-type">
        <h4 class="title-task">我的任务</h4>
    </div>
    <div class="table-data">
        <#if taskList ?? && (taskList?size>0)>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>发起者</th>
                    <th>内容</th>
                    <th>发起时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                    <#list taskList as task>
                    <tr>
                        <td>${task.sender!}</td>
                        <td><a href="${task.operateURL!}">${task.description}</a></td>
                        <td>${task.createdTime?string("yyyy-MM-dd HH:mm:ss")!}</td>
                        <td>
                            <#if task.taskType == "TASK" >
                                <a class="btn btn-primary btn-xs" href="${task.operateURL!}">去审核</a>
                                <#if task.operationType == 'PAYROLL'>
                                    <a class="btn btn-xs btn-danger" href="${task.operateURL!}">驳回</a>
                                <#elseif task.operationType != 'PUSH'>
                                    <a class="btn btn-danger btn-xs refuse" href="javascript:void(0);"
                                       data-url="" data-taskId="${task.id}">拒绝</a>
                                </#if>
                            </#if>
                            <#if task.taskType == "NOTIFY" >
                                <a class="btn btn-info btn-xs" href="javascript:void(0);"
                                   data-taskId="${task.id}">知道了</a>
                            </#if>
                        </td>
                    </tr>
                    </#list>
                </tbody>
            </table>
        <#else>
            没有待处理的任务。</br></br>
        </#if>

    </div>
    <div class="table-data">
        <div class="two-flex">
            <div class="title-type">
                <h4 class="person-num">新增用户 (人)</h4>
            </div>
            <div class="table-data">
                <ul class="data-list yellow-bg">
                    <li>
                        <p class="num-text">${userToday!}</p>
                        <p class="name-text">今日新增</p>
                    </li>
                    <li>
                        <p class="num-text">${user7Days!}</p>
                        <p class="name-text">近7天新增</p>
                    </li>
                    <li>
                        <p class="num-text">${user30Days!}</p>
                        <p class="name-text">近30天新增</p>
                    </li>
                </ul>
            </div>
        </div>
        <div class="two-flex">
            <div class="title-type">
                <h4 class="send-money">平台累计交易金额 (元)</h4>
            </div>

            <div class="table-data">
                <ul class="data-list red-bg">
                    <li>
                        <p class="num-text">${totalInvest/100?float}</p>
                        <p class="name-text">累计交易总额</p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="title-type">
        <h4 class="give-charge">充值金额 (元) (出借人)</h4>
    </div>
    <div class="table-data">
        <ul class="data-list list-width blue-bg">
            <li>
                <p class="num-text">${rechargeTodayNotLoaner/100?float}</p>
                <p class="name-text">今日充值</p>
            </li>
            <li>
                <p class="num-text">${recharge7DaysNotLoaner/100?float}</p>
                <p class="name-text">近7天充值</p>
            </li>
            <li>
                <p class="num-text">${recharge30DaysNotLoaner/100?float}</p>
                <p class="name-text">近30天充值</p>
            </li>

        </ul>
    </div>
    <div class="title-type">
        <h4 class="give-charge">充值金额 (元) (借款人)</h4>
    </div>
    <div class="table-data">
        <ul class="data-list list-width blue-bg">
            <li>
                <p class="num-text">${rechargeTodayLoaner/100?float}</p>
                <p class="name-text">今日充值</p>
            </li>
            <li>
                <p class="num-text">${recharge7DaysLoaner/100?float}</p>
                <p class="name-text">近7天充值</p>
            </li>
            <li>
                <p class="num-text">${recharge30DaysLoaner/100?float}</p>
                <p class="name-text">近30天充值</p>
            </li>
        </ul>
    </div>
    <div class="title-type">
        <h4 class="get-money">提现金额 (元) (出借人)</h4>
    </div>
    <div class="table-data">
        <ul class="data-list list-width green-bg">
            <li>
                <p class="num-text">${withdrawTodayNotLoaner/100?float}</p>
                <p class="name-text">今日提现</p>
            </li>
            <li>
                <p class="num-text">${withdraw7DaysNotLoaner/100?float}</p>
                <p class="name-text">近7天提现</p>
            </li>
            <li>
                <p class="num-text">${withdraw30DaysNotLoaner/100?float}</p>
                <p class="name-text">近30天提现</p>
            </li>
        </ul>
    </div>
    <div class="title-type">
        <h4 class="get-money">提现金额 (元) (借款人)</h4>
    </div>
    <div class="table-data">
        <ul class="data-list list-width green-bg">
            <li>
                <p class="num-text">${withdrawTodayLoaner/100?float}</p>
                <p class="name-text">今日提现</p>
            </li>
            <li>
                <p class="num-text">${withdraw7DaysLoaner/100?float}</p>
                <p class="name-text">近7天提现</p>
            </li>
            <li>
                <p class="num-text">${withdraw30DaysLoaner/100?float}</p>
                <p class="name-text">近30天提现</p>
            </li>
        </ul>
    </div>
    <div class="title-type">
        <h4 class="send-money">投资金额 (元)</h4>
    </div>
    <div class="table-data">
        <ul class="data-list list-width loan-bg">
            <li>
                <p class="num-text">${investToday/100?float}</p>
                <p class="name-text">今日投资</p>
            </li>
            <li>
                <p class="num-text">${invest7Days/100?float}</p>
                <p class="name-text">近7天投资</p>
            </li>
            <li>
                <p class="num-text">${invest30Days/100?float}</p>
                <p class="name-text">近30天投资</p>
            </li>
        </ul>
    </div>

</div>

</@global.main>