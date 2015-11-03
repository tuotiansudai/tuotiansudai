<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>债权还款计划</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="../../style/libs/bootstrap-datepicker.css" rel="stylesheet">
    <link rel="stylesheet" href="../../style/libs/bootstrap-select.css"/>
    <link href="../../style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">

    <link rel="stylesheet" href="style/index.css">
<@global.javascript pageJavascript="debt-repay-plan.js"></@global.javascript>

</head>
<body>
<@menu.header label="finaMan"></@menu.header>
<div class="main">
    <div class="container-fluid">
        <div class="row">
        <@menu.sidebar headLab="finaMan" sideLab="debtRepay"></@menu.sidebar>
            <!-- content area begin -->

            <div class="col-md-10">

                    <div class="row">
                        <div class="col-md-3">
                        <div class="pull-left currentTab"><span> 债权还款计划</span></div>
                        </div>
                        <div class="col-md-9 text-right">
                            <form action="" class="form-inline query-build">
                        <div class="form-group">

                            <select class="selectpicker"  data-style="btn-default" >
                                <option>全部</option>
                                <option>已还款</option>
                                <option>未还款</option>
                            </select>
                        </div>
                        <button class="btn btn-primary" type="submit">查询</button>
                            </form>
                            </div>
                    </div>

                <div class="row">
                    <table class="table table-bordered table-hover table-center">
                        <thead>
                        <tr>
                            <th>时间</th>
                            <th>还款金额</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td><a href="/debtRepaymentDetail" class="linked">2015-03</a></td>
                            <td> 104.5</td>

                        </tr>
                        <tr>
                            <td><a href="/debtRepaymentDetail" class="linked">2015-03</a></td>
                            <td>104.5</td>

                        </tr>
                        <tr>
                            <td><a href="/debtRepaymentDetail" class="linked">2015-03</a></td>
                            <td>104.5</td>

                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>

            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->
</body>
</html>