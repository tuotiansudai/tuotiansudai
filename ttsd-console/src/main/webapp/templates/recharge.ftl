<!DOCTYPE html>
<html>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>充值记录</title>
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
            $('#recordDate .date').datepicker({
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
        <@menu.sidebar headLab="finaMan" sideLab="recharge"></@menu.sidebar>
            <!-- content area begin -->

            <div class="col-md-10">
                <form action="" class="form-inline query-build">
                    <div class="row">
                        <div class="form-group">
                            <label for="control-label">编号</label>
                            <input type="text" class="form-control" >
                        </div>
                        <div class="form-group">
                            <label for="control-label">用户名</label>
                            <input type="text" class="form-control" >
                        </div>
                        <div class="form-group" id="recordDate">
                            <label for="control-label">时间</label>
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
                            <label for="control-label">充值来源</label>
                            <select class="selectpicker"  data-style="btn-default" >
                                <option>WEB</option>
                                <option>ANDROID </option>
                                <option>IOS </option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="control-label">状态</label>
                            <select class="selectpicker"  data-style="btn-default" >
                                <option>请选择提现状态</option>
                                <option>等待审核 </option>
                                <option>提现成功</option>
                                <option>提现失败</option>

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
                            <th>编号</th>
                            <th>时间</th>
                            <th>用户名</th>
                            <th>充值金额</th>
                            <th>手续费</th>
                            <th>充值渠道</th>
                            <th>管理员充值</th>
                            <th>充值状态</th>
                            <th>充值来源</th>

                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>2015080400000004</td>
                            <td>2015-08-04</td>
                            <td>sidneygao</td>
                            <td>1000.00</td>
                            <td>0.00</td>
                            <td>ICBI</td>
                            <td>否</td>
                            <td>充值成功</td>
                            <td>ANDROID</td>
                        </tr>
                        <tr>
                            <td>2015080400000004</td>
                            <td>2015-08-04</td>
                            <td>sidneygao</td>
                            <td>1000.00</td>
                            <td>0.00</td>
                            <td>ICBI</td>
                            <td>否</td>
                            <td>充值成功</td>
                            <td>ANDROID</td>
                        </tr>
                        <tr>
                            <td>2015080400000004</td>
                            <td>2015-08-04</td>
                            <td>sidneygao</td>
                            <td>1000.00</td>
                            <td>0.00</td>
                            <td>ICBI</td>
                            <td>否</td>
                            <td>充值成功</td>
                            <td>ANDROID</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="row">
                    <!-- pagination  -->
                    <nav class="pagination-control">
                        <div><span class="bordern">总共5条,每页显示15条</span></div>
                        <ul class="pagination pull-left">

                            <li>
                                <a href="#">
                                    <span>« Prev</span>
                                </a>
                            </li>
                            <li><a>1</a></li>
                            <li>
                                <a href="#">
                                    <span>Next »</span>
                                </a>

                            </li>
                        </ul>
                        <button class="btn btn-default pull-left" type="button">导出Excel</button>
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