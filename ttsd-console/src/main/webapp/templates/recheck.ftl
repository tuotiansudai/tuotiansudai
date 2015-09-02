<!DOCTYPE html>
<html>
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
            <a href="../../../" class="navbar-brand"><img src="../../images/logo.jpg" alt=""></a>
        </div>
    </div>
    <nav id="bs-navbar" class="collapse navbar-collapse">
        <div class="container-fluid">
            <ul class="nav navbar-nav">
                <li class="active">
                    <a href="">系统主页</a>
                </li>
                <li>
                    <a href="">项目管理</a>
                </li>
                <li>
                    <a href="">用户管理</a>
                </li>
                <li>
                    <a href="">财务管理</a>
                </li>
                <li>
                    <a href="">文章管理</a>
                </li>
                <li>
                    <a href="">安全管理</a>
                </li>
            </ul>
        </div>
    </nav>
</header>
<!-- header end -->

<!-- main begin -->
<div class="main">
    <div class="container-fluid">
        <div class="row">
            <!-- menu sidebar -->
            <div class="col-md-2">
                <ul class="nav bs-docs-sidenav">
                    <li class="active"><a href="../index.html">所有借款</a></li>
                    <li><a href="firstTrial.html">初审的借款</a></li>
                    <li><a href="moneyCollect.html">筹款中借款</a></li>
                    <li><a href="finishRefund.html">完成还款的借款</a></li>
                    <li><a href="Drain.html">已经流标的借款</a></li>
                    <li><a href="overdue.html">逾期借款</a></li>
                    <li><a href="star.html">发起借款</a></li>
                    <li><a href="twoTrial.html">复审借款</a></li>
                    <li><a href="recheck.html">复审核借款</a></li>
                    <li><a href="check.html">审核借款</a></li>
                    <li><a href="fundsEdit.html">复审借款</a></li>
                </ul>
            </div>
            <!-- menu sidebar end -->

            <!-- content area begin -->
            <div class="col-md-10">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="project" class="col-sm-2 control-label">借款项目名称: </label>
                        <div class="col-sm-4">
                            <span class="form-control"> abc_sl_01</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">代理用户: </label>

                        <div class="col-sm-4">
                            <span class="form-control"> abc_sl_01</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款用户: </label>

                        <div class="col-sm-4">
                            <span class="form-control"> abc_sl_01</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">标的类型: </label>

                        <div class="col-sm-4">
                            <span class="form-control"> 先付利息后还本金，按天计息，即投即生息</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款用途: </label>

                        <div class="col-sm-4">
                            <span class="form-control"> abc_sl_01</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款总金额: </label>

                        <div class="col-sm-4">
                            <span class="form-control"> abc_sl_01</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">尚未募集的金额: </label>

                        <div class="col-sm-4">
                            <span class="form-control"> abc_sl_01</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款利率: </label>

                        <div class="col-sm-4">
                            <span class="form-control"> abc_sl_01</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">手续费（还款时收取）: </label>

                        <div class="col-sm-4">
                            <span class="form-control"> abc_sl_01</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款期限: </label>

                        <div class="col-sm-4">
                            <span class="form-control"> 1</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">审核信息: </label>

                        <div class="col-sm-4">
                            <textarea class="form-control" rows="3"></textarea>
                        </div>
                    </div>
                    <div class="form-group input-append">
                        <label class="col-sm-2 control-label">筹款截止时间: </label>

                        <div class="col-sm-4">
                            <div class='input-group date' id='datetimepicker'>
                                <input type='text' class="form-control"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">最小投资金额: </label>

                        <div class="col-sm-4">
                            <span class="form-control"> abc_sl_01</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">初审是否通过: </label>

                        <div class="col-sm-4">
                            <span class="label label-success "> 是</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">操作: </label>

                        <div class="col-sm-4">

                            <button type="submit" class="btn btn-primary">放款</button>
                            <button type="submit" class="btn btn-primary">延期</button>
                            <button type="submit" class="btn btn-primary">流标</button>
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
<script>
    var API_SELECT = '${requestContext.getContextPath()}/loan/titles';  // 申请资料标题url
    var API_POST_TITLE = '${requestContext.getContextPath()}/loan/title';  //
    var API_FORM = '${requestContext.getContextPath()}/loan/';
    var api_url = '${requestContext.getContextPath()}/loan/loaner';
</script>