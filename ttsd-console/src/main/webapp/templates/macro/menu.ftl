<#assign menus=
        [
            {
                "name":"sysMain",
                "header":{"text":"系统主页","link":"/"},
                "sidebar":[]
            },
            {
                "name":"projectMain",
                "header":{"text":"项目管理","link":"/loan"},
                "sidebar":
                [
                    {"name":"ALL","text":"所有的借款","link":"/loanList/console?status=&loanId=0&startTime=&endTime=&currentPageNo=1&loanName=&pageSize=10"},
                    {"name":"start","text":"发起借款","link":"/loan"},
                    {"name":"WAITING_VERIFY","text":"初审的借款","link":"/loanList/console?status=WAITING_VERIFY&loanId=0&startTime=&endTime=&currentPageNo=1&loanName=&pageSize=10"},
                    {"name":"RAISING","text":"筹款中的借款","link":"/loanList/console?status=RAISING&loanId=0&startTime=&endTime=&currentPageNo=1&loanName=&pageSize=10"},
                    {"name":"RECHECK","text":"复审的借款","link":"/loanList/console?status=RECHECK&loanId=0&startTime=&endTime=&currentPageNo=1&loanName=&pageSize=10"},
                    {"name":"REPAYING","text":"还款中的借款","link":"/loanList/console?status=REPAYING&loanId=0&startTime=&endTime=&currentPageNo=1&loanName=&pageSize=10"},
                    {"name":"COMPLETE","text":"完成还款的借款","link":"/loanList/console?status=COMPLETE&loanId=0&startTime=&endTime=&currentPageNo=1&loanName=&pageSize=10"},
                    {"name":"CANCEL","text":"已经流标的借款","link":"/loanList/console?status=CANCEL&loanId=0&startTime=&endTime=&currentPageNo=1&loanName=&pageSize=10"},
                    {"name":"OVERDUE","text":"逾期的借款","link":"/loanList/console?status=OVERDUE&loanId=0&startTime=&endTime=&currentPageNo=1&loanName=&pageSize=10"},
                    {"name":"investmentInfoList","text":"项目投资明细表","link":"/invests"},
                    {"name":"repaymentInfoList","text":"项目还款明细表","link":"/loan-repay"}
                ]
            },
            {
                "name":"userMain",
                "header":{"text":"用户管理","link":"/users"},
                "sidebar":[
                    {"name":"addUser","text":"添加用户","link":""},
                    {"name":"userMan","text":"用户管理","link":"/users"},
                    {"name":"referMan","text":"推荐人管理","link":"/referrerManage"},
                    {"name":"##############","text":"推荐层级-收益比例管理(用户)","link":""},
                    {"name":"##############","text":"用户推荐层级-收益比例管理(系统)","link":""},
                    {"name":"##############","text":"业务员推荐层级-收益比例管理(系统)","link":""}
                ]
            },
            {
                "name":"finaMan",
                "header":{"text":"财务管理","link":"/invests"},
                "sidebar":[
                    {"name":"userInvest","text":"用户投资管理","link":"/invests"},
                    {"name":"debtRepay","text":"债权还款计划","link":"/debtRepaymentPlan"},
                    {"name":"recharge","text":"充值记录","link":"/recharge"},
                    {"name":"withdraw","text":"提现记录","link":"/withdraw"},
                    {"name":"userFund","text":"用户资金查询","link":"/userFunds"},
                    {"name":"realTimeStatus","text":"联动优势资金查询","link":"/real-time-status"},
                    {"name":"systemBill","text":"系统账户查询","link":"/systemBill"},
                    {"name":"adminIntervention","text":"管理员修改账户余额","link":"/admin-intervention"},
                    {"name":"realTimeStatus","text":"联动优势余额查询","link":"/real-time-status"}
                ]
            },
            {
                "name":"announceMan",
                "header":{"text":"公告管理","link":"/announceManage"},
                "sidebar":[
                    {"name":"announceMan","text":"公告管理","link":"/announceManage"}
                ]
            },
            {
                "name":"security",
                "header":{"text":"安全管理","link":"/security-log/login-log"},
                "sidebar":[
                    {"name":"loginLog","text":"登录日志","link":"/security-log/login-log"},
                    {"name":"auditLog","text":"管理日志","link":"/security-log/audit-log"}
                ]
            }
        ]
>

<#macro header label>
<!-- header begin -->
<header class="navbar navbar-static-top bs-docs-nav" id="top" role="banner">
    <div class="container-fluid">
        <div class="navbar-header">
            <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target="#bs-navbar"
                    aria-controls="bs-navbar" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="${requestContext.getContextPath()}" class="navbar-brand"><img src="${requestContext.getContextPath()}/images/logo.jpg" alt=""></a>
        </div>
    </div>
    <nav id="bs-navbar" class="collapse navbar-collapse">
        <div class="container-fluid">
            <ul class="nav navbar-nav">
                <#list menus as menu>
                    <li <#if menu.name == label>class="active"</#if>>
                        <a href="${menu.header.link}">${menu.header.text}</a>
                    </li>
                </#list>
            </ul>
        </div>
    </nav>
</header>
<!-- header end -->
</#macro>

<#macro sidebar headLab sideLab>
<!-- menu sidebar -->
<div class="col-md-2">
    <ul class="nav bs-docs-sidenav">
        <#list menus as menu>
            <#if menu.name=headLab>
                <#list menu.sidebar as item>
                    <li <#if item.name == sideLab>class="active"</#if>><a href="${item.link}">${item.text}</a></li>
                </#list>
            </#if>
        </#list>
    </ul>
</div>
<!-- menu sidebar end -->
</#macro>