<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>复审</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- link bootstrap css and js -->
    <link href="../../style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="../../style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <!-- jquery -->
    <!-- link bootstrap css and js -->

    <link rel="stylesheet" href="../../style/index.css">
    <!-- 日历插件 -->
    <link rel="stylesheet" href="../../style/libs/bootstrap-select.css"/>
    <link href="../../style/libs/bootstrap/bootstrap-datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
    <script src="../../js/libs/config.js"></script>
    <script src="../../js/libs/require-2.1.20.min.js"
            defer
            async="true"
            data-main="../../js/recheck.js"></script>
</head>
<body>

<@menu.header label="proMan"></@menu.header>

<!-- main begin -->
<div class="main">
    <div class="container-fluid">
        <div class="row">

            <@menu.sidebar headLab="proMan" sideLab="RECHECK"></@menu.sidebar>

            <!-- content area begin -->
            <div class="col-md-10">
                <form class="form-horizontal" method="post">
                    <div class="form-group">
                        <label for="project" class="col-sm-2 control-label">借款项目名称: </label>
                        <div class="col-sm-4">
                            <span class="form-control">${loan.projectName}</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">代理用户: </label>

                        <div class="col-sm-4">
                            <span class="form-control">${loan.agentLoginName}</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款用户: </label>

                        <div class="col-sm-4">
                            <span class="form-control">${loan.loanerLoginName}</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">标的类型: </label>

                        <div class="col-sm-4">
                            <span class="form-control">${loan.type.getName()}</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款总金额: </label>

                        <div class="col-sm-4">
                            <span class="form-control">${loan.loanAmount}</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">尚未募集的金额: </label>

                        <div class="col-sm-4">
                            <span class="form-control">${loan.amountNeedRaised}</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款利率: </label>

                        <div class="col-sm-4">
                            <span class="form-control">${loan.basicRate}% + ${loan.activityRate}%</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款期限: </label>

                        <div class="col-sm-4">
                            <span class="form-control">${loan.periods}</span>
                        </div>
                    </div>
                    <div class="form-group input-append">
                        <label class="col-sm-2 control-label">筹款截止时间: </label>

                        <div class="col-sm-4">
                            <div class='input-group date' id='datetimepicker'>
                                <input name="fundraisingEndTime" type='text' class="form-control" value="${(loan.fundraisingEndTime?string("yyyy-MM-dd HH:mm:ss"))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">最小投资金额: </label>

                        <div class="col-sm-4">
                            <input name="minInvestAmount" class="form-control" value="${loan.minInvestAmount!}" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">操作: </label>

                        <div class="col-sm-4">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-primary">放款</button>
                            <button type="button" class="btn btn-primary">延期</button>
                            <button type="button" class="btn btn-primary">流标</button>
                        </div>
                    </div>
                </form>
            </div>
            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->

</body>
</html>
