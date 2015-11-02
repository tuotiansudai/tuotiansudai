<!DOCTYPE html>
<html>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>公告管理</title>
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

</head>
<body>
<@menu.header label="announceMan"></@menu.header>
<div class="main">
    <div class="container-fluid">
        <div class="row">
        <@menu.sidebar headLab="announceMan" sideLab="announceMan"></@menu.sidebar>
            <!-- content area begin -->

            <div class="col-md-10">
                <form action="" class="form-inline query-build">
                    <div class="row">
                        <div class="form-group">
                            <label for="control-label">编号</label>
                            <input type="text" class="form-control" >
                        </div>
                        <div class="form-group">
                            <label for="control-label">标题</label>
                            <input type="text" class="form-control" >
                        </div>
                        <button class="btn btn-primary" type="submit">查询</button>
                    </div>

                </form>

                <div class="row">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th>标题</th>
                            <th>更新时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>20150706</td>
                            <td>好消息！7月注册活动红包发放添加新渠道啦！再也不用为领不到红包而担忧了！</td>
                            <td>2015-07-07</td>
                            <td><a href="#" class="btn btn-link">编辑</a> | <a href="#" class="btn btn-link">删除</a></td>
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