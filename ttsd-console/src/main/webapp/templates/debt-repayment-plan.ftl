<!DOCTYPE html>
<html>
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

    <script type="text/javascript" src="js/libs/jquery-1.10.1.min.js"></script>
    <script type="text/javascript" src="../js/libs/bootstrap.min.js"></script>
    <script type="text/javascript" src="../../js/libs/bootstrap-select.js"></script>
    <script type="text/javascript" src="../../js/libs/bootstrap-datepicker.js"></script>

    <script type="text/javascript">
        $(function() {
            $('#RewardDate .date,#investDate .date').datepicker({
                format:'yyyy-mm-dd',
                autoclose:true
            });
            $('.selectpicker').selectpicker();
        })
    </script>
</head>
<body>
<@menu.header label="finaMan"></@menu.header>
<div class="main">
    <div class="container-fluid">
        <div class="row">
        <@menu.sidebar headLab="finaMan" sideLab="userInvest"></@menu.sidebar>
            <!-- content area begin -->

            <div class="col-md-10">
                <form action="" class="form-inline query-build">
                    <div class="row">
                        <div class="form-group">
                            <label for="control-label">推荐人</label>
                            <input type="text" class="form-control" >
                        </div>
                        <div class="form-group">
                            <label for="control-label">投资人</label>
                            <input type="text" class="form-control" >
                        </div>
                        <div class="form-group" id="investDate">
                            <label for="control-label">投资时间</label>
                            <div class='input-group date'>
                                <input type='text' class="form-control" />
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                            </div>
                            -
                            <div class='input-group date'>
                                <input type='text' class="form-control"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="control-label">推荐层级</label>
                            <input type="text" class="form-control" >
                        </div>
                        <div class="form-group" id="RewardDate">
                            <label for="control-label">奖励时间</label>
                            <div class='input-group date'>
                                <input type='text' class="form-control" />
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                            </div>
                            -
                            <div class='input-group date'>
                                <input type='text' class="form-control"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="control-label">推荐人角色</label>
                            <select class="selectpicker"  data-style="btn-default" >
                                <option>全部</option>
                                <option>全部1</option>
                            </select>
                        </div>
                        <button class="btn btn-primary" type="submit">查询</button>
                        <button class="btn btn-default" type="submit">重置</button>
                    </div>

                </form>

                <div class="row">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>项目名称</th>
                            <th>期数</th>
                            <th>投资人</th>
                            <th>投资人姓名</th>
                            <th>投资金额</th>
                            <th>投资时间</th>
                            <th>推荐人</th>
                            <th>推荐人姓名</th>
                            <th>推荐人是否业务员</th>
                            <th>推荐层级</th>
                            <th>推荐奖励</th>
                            <th>奖励状态</th>
                            <th>奖励时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>terer</td>
                            <td>12期</td>
                            <td>jiaojiao</td>
                            <td>娇娇</td>
                            <td>50</td>
                            <td>2015-05-09</td>
                            <td>terer</td>
                            <td>张龙</td>
                            <td>否</td>
                            <td>1</td>
                            <td>0.1</td>
                            <td>已入账</td>
                            <td>2015-08-09</td>
                        </tr>
                        <tr>
                            <td>terer</td>
                            <td>12期</td>
                            <td>jiaojiao</td>
                            <td>娇娇</td>
                            <td>50</td>
                            <td>2015-05-09</td>
                            <td>terer</td>
                            <td>张龙</td>
                            <td>否</td>
                            <td>1</td>
                            <td>0.1</td>
                            <td>已入账</td>
                            <td>2015-08-09</td>
                        </tr>
                        <tr>
                            <td>terer</td>
                            <td>12期</td>
                            <td>jiaojiao</td>
                            <td>娇娇</td>
                            <td>50</td>
                            <td>2015-05-09</td>
                            <td>terer</td>
                            <td>张龙</td>
                            <td>否</td>
                            <td>1</td>
                            <td>0.1</td>
                            <td>已入账</td>
                            <td>2015-08-09</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="row">
                    <!-- pagination  -->
                    <nav>
                        <div><span class="bordern">总共5条,每页显示15条</span></div>
                        <ul class="pagination">

                            <li>
                                <a href="#">
                                    <span>? Prev</span>
                                </a>
                            </li>
                            <li><a>1</a></li>
                            <li>
                                <a href="#">
                                    <span>Next ?</span>
                                </a>

                            </li>
                        </ul>
                    </nav>
                </div>
            </div>

            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->
</body>
</html>