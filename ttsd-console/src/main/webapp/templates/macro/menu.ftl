<#assign menus=
        [
            {
                "name":"sysMain",
                "header":{"text":"系统主页","link":""},
                "sidebar":
                    [
                        {"name":"index","text":"所有借款","link":"index.html"},
                        {"name":"firstTrial","text":"初审的借款","link":"firstTrial.html"},
                        {"name":"moneyCollect","text":"筹款中借款","link":"moneyCollect.html"},
                        {"name":"finishRefund","text":"完成还款的借款","link":"finishRefund.html"},
                        {"name":"Drain","text":"已经流标的借款","link":"Drain.html"},
                        {"name":"overdue","text":"逾期借款","link":"overdue.html"},
                        {"name":"start","text":"发起借款","link":"start.html"},
                        {"name":"twoTrial","text":"复审借款","link":"twoTrial.html"},
                        {"name":"recheck","text":"复审核借款","link":"recheck.html"},
                        {"name":"check","text":"审核借款","link":"check.html"},
                        {"name":"fundsEdit","text":"复审借款","link":"fundsEdit.html"}
                    ]
            },
            {
                "name":"proMan",
                "header":{"text":"项目管理","link":""},
                "sidebar":[]
            },
            {
                "name":"userMan",
                "header":{"text":"用户管理","link":""},
                "sidebar":[]
            },
            {
                "name":"finaMan",
                "header":{"text":"财务管理","link":""},
                "sidebar":[]
            },
            {
                "name":"artMan",
                "header":{"text":"文章管理","link":""},
                "sidebar":[]
            },
            {
                "name":"secMan",
                "header":{"text":"安全管理","link":""},
                "sidebar":[]
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