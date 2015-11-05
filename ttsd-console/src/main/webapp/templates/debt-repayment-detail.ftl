<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>债权还款计划详情</title>
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
                        <div class="pull-left currentTab"><span> 债权还款计划 > 全部</span></div>
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
                            <th>应还日期</th>
                            <th>还款金额</th>
                            <th>标的名称</th>
                            <th>代理人</th>
                            <th>还款状态</th>
                            <th>还款时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>2015-10-03</td>
                            <td> 104.50</td>
                            <td>新手的标</td>
                            <td>tuotiantian</td>
                            <td class="badgeCol">尚未还款</td>
                            <td>---</td>

                        </tr>
                        <tr>
                            <td>2015-10-03</td>
                            <td> 104.50</td>
                            <td>新手的标</td>
                            <td>tuotiantian</td>
                            <td class="badgeCol">尚未还款</td>
                            <td>---</td>

                        </tr>
                        <tr>
                            <td>2015-10-03</td>
                            <td> 104.50</td>
                            <td>新手的标</td>
                            <td>tuotiantian</td>
                            <td>还款完成</td>
                            <td>2015-10-01</td>

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