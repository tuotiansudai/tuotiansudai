<#assign menus=
        [
            {
                "name":"sysMain",
                "header":{"text":"系统主页","link":"/"},
                "sidebar":[]
            },
            {
                "name":"proMan",
                "header":{"text":"项目管理","link":"/loan"},
                "sidebar":
                [
                    {"name":"all","text":"所有的借款","link":""},
                    {"name":"start","text":"发起借款","link":"/loan"},
                    {"name":"firstTrial","text":"初审的借款","link":""},
                    {"name":"moneyCollect","text":"筹款中的借款","link":""},
                    {"name":"recheck","text":"复审的借款","link":""},
                    {"name":"##############","text":"还款中的借款","link":""},
                    {"name":"finishRefund","text":"完成还款的借款","link":""},
                    {"name":"drain","text":"已经流标的借款","link":""},
                    {"name":"overdue","text":"逾期的借款","link":""},
                    {"name":"recheck","text":"复审核借款","link":""},
                    {"name":"investmentInfoList","text":"项目投资明细表","link":""},
                    {"name":"repaymentInfoList","text":"项目还款明细表","link":""}
                ]
            },
            {
                "name":"userMan",
                "header":{"text":"用户管理","link":"/"},
                "sidebar":[
                    {"name":"addUser","text":"添加用户","link":""},
                    {"name":"userMan","text":"用户管理","link":""},
                    {"name":"referMan","text":"推荐人管理","link":""},
                    {"name":"##############","text":"推荐层级-收益比例管理(用户)","link":""},
                    {"name":"##############","text":"用户推荐层级-收益比例管理(系统)","link":""},
                    {"name":"##############","text":"业务员推荐层级-收益比例管理(系统)","link":""}
                ]
            },
            {
                "name":"finaMan",
                "header":{"text":"财务管理","link":"/invests"},
                "sidebar":[
                    {"name":"userInvest","text":"用户投资管理","link":""},
                    {"name":"##############","text":"债权还款计划","link":""},
                    {"name":"recharge","text":"充值记录","link":""},
                    {"name":"##############","text":"提现记录","link":""},
                    {"name":"##############","text":"用户资金查询","link":""},
                    {"name":"##############","text":"联动优势资金查询","link":""},
                    {"name":"##############","text":"系统账户查询","link":""},
                    {"name":"##############","text":"修改账户余额","link":""}
                ]
            },
            {
                "name":"notifyMan",
                "header":{"text":"公告管理","link":"/"},
                "sidebar":[
                    {"name":"##############","text":"公告管理","link":""}
                ]
            },
            {
                "name":"secMan",
                "header":{"text":"安全管理","link":"/"},
                "sidebar":[
                    {"name":"##############","text":"用户登录日志","link":""},
                    {"name":"##############","text":"用户管理日志","link":""}
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
            <a href="../" class="navbar-brand"><img src="images/logo.jpg" alt=""></a>
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