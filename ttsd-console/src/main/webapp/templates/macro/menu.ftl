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
                <li id="sysMain">
                    <a href="">系统主页</a>
                </li>
                <li id="proMan">
                    <a href="">项目管理</a>
                </li>
                <li id="userMan">
                    <a href="">用户管理</a>
                </li>
                <li id="finaMan">
                    <a href="">财务管理</a>
                </li>
                <li id="artMan">
                    <a href="">文章管理</a>
                </li>
                <li id="secMan">
                    <a href="">安全管理</a>
                </li>
            </ul>
        </div>
    </nav>
</header>
<script>
    document.getElementById("${label}").className="active";
</script>
<!-- header end -->
</#macro>

<#macro sidebar label>
<!-- menu sidebar -->
<div class="col-md-2">
    <ul class="nav bs-docs-sidenav">
        <li id="index"><a href="index.html">所有借款</a></li>
        <li id="firstTrial"><a href="firstTrial.html">初审的借款</a></li>
        <li id="moneyCollect"><a href="moneyCollect.html">筹款中借款</a></li>
        <li id="finishRefund"><a href="finishRefund.html">完成还款的借款</a></li>
        <li id="Drain"><a href="Drain.html">已经流标的借款</a></li>
        <li id="overdue"><a href="overdue.html">逾期借款</a></li>
        <li id="star"><a href="star.html">发起借款</a></li>
        <li id="twoTrial"><a href="twoTrial.html">复审借款</a></li>
        <li id="recheck"><a href="recheck.html">复审核借款</a></li>
        <li id="check"><a href="check.html">审核借款</a></li>
        <li id="fundsEdit"><a href="fundsEdit.html">复审借款</a></li>
    </ul>
</div>
<script>
    document.getElementById("${label}").className="active";
</script>
<!-- menu sidebar end -->
</#macro>