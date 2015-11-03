<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>发布公告</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="style/index.css">
    <@global.javascript pageJavascript="announce-release.js"></@global.javascript>
</head>
<body>
<@menu.header label="announceMan"></@menu.header>
<div class="main">
    <div class="container-fluid">
        <div class="row">
        <@menu.sidebar headLab="announceMan" sideLab="announceMan"></@menu.sidebar>
            <!-- content area begin -->

            <div class="col-md-10">
                <form class="form-horizontal jq-form">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">标题: </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">项目描述: </label>

                        <div class="col-sm-10">
                            <script id="editor" type="text/plain"></script>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">属性: </label>

                        <div class="col-sm-4">
                            <div class="checkbox jq-checkbox">
                                <label>
                                    <input type="checkbox" class="jq-index" value="1" checked>
                                    首页
                                </label>
                            </div>
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