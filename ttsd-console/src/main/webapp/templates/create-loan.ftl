<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>筹款编辑</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- link bootstrap css and js -->
    <link href="../../style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="../../style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <script src="../../js/libs/jquery-1.10.1.min.js"></script>
    <!-- jquery -->
    <script src="../../js/libs/bootstrap.min.js"></script>
    <!-- link bootstrap css and js -->

    <link rel="stylesheet" href="../../style/index.css">
    <!-- 日历插件 -->
    <link href="../../style/libs/bootstrap/bootstrap-datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
    <script type="text/javascript" charset="utf-8" src="../../js/libs/moment-with-locales.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../js/libs/bootstrap-datetimepicker.js"></script>
    <!-- 富文本编辑器 -->
    <script type="text/javascript" charset="utf-8" src="../../js/libs/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="../../js/libs/ueditor/ueditor.all.js"></script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="../../js/libs/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" src="../../js/libs/ueditor/ueditor.js"></script>
    <!-- 富文本编辑器  end-->

    <!--上传图片插件-->
    <link rel="stylesheet" href="../../style/libs/fileinput.css"/>
    <script src="../../js/libs/fileinput.js"></script>
    <script src="../../js/libs/fileinput_locale_zh.js"></script>
    <!--上传图片插件 end-->

    <#--表单校验-->
    <script src="../../js/libs/Validform_v5.3.2.js"></script>
    <script src="../../js/libs/Validform_Datatype.js"></script>

    <#--表单校验-->

    <!--Arttemplate js-->
    <script src="../../js/libs/template.js"></script>
    <!--Arttemplate js end-->
    <!--下拉框-->
    <link rel="stylesheet" href="../../style/libs/bootstrap-select.css"/>
    <script src="../../js/libs/bootstrap-select.js"></script>
    <!--下拉框-->

    <!--自动补全-->
    <link rel="stylesheet" href="../../style/libs/jquery-ui-1.9.2.custom.css"/>
    <script src="../../js/libs/jquery-ui-1.9.2.custom.min.js"></script>
    <!--自动补全-->
<#--当前页面js-->
    <script src="../../js/createLoan.js"></script>
<#--当前页面js end-->
    <script id="upload" type="text/html">
        <div class="form-group">
            <label class="col-sm-2 control-label"></label>

            <div class="col-sm-10">
                <div class="row col-file-box">
                    <div class="select-box">
                        <select class="selectpicker col-sm-5">
                            {{each _data}}
                            <option value="{{$value.id}}">{{$value.title}}</option>
                            {{/each}}
                        </select>

                        <input type="hidden" class="jq-txt" value="{{_data[0]['id']}}" />
                    </div>
                    <input type="text" name="file-name[]" class="files-input form-control" placeholder="请输入资料名称"/>
                    <button type="button" class="btn btn-default jq-add">添加</button>
                    <button type="button" class="btn btn-danger jq-delete">删除</button>
                </div>
                <input type="file" multiple=true class="file-loading">
            </div>
        </div>
    </script>
    <script id="select" type="text/html">
        <select class="selectpicker col-sm-5">
            {{each _data}}
            <option value="{{$value.id}}">{{$value.title}}</option>
            {{/each}}
        </select>
    </script>
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
                <form class="form-horizontal jq-form">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款项目名称: </label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-user" placeholder="请输入内容" datatype="*" errormsg="请输入名称">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">代理用户: </label>

                        <div class="col-sm-4">
                            <input type="text" id="tags_1" class="form-control ui-autocomplete-input" autocomplete="off"
                                   placeholder="请输入内容">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款用户: </label>

                        <div class="col-sm-4">
                            <input type="text" id="tags" class="form-control ui-autocomplete-input" autocomplete="off"
                                   placeholder="请输入内容">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">标的类型: </label>

                        <div class="col-sm-4">
                            <select class="selectpicker jq-b-type">
                            <#list loanTypes as loanType>
                                <option value="${loanType.loanTypeName}" data-repayTimeUnit="${loanType.repayTimeUnit}"
                                        data-repayTimePeriod="${loanType.repayTimePeriod}">
                                ${loanType.name}
                                </option>
                            </#list>
                            </select>
                            <input type="hidden" class="jq-mark-type" value="" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">借款期限: </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-timer" placeholder="" datatype="num">

                        </div>
                        <div class="col-sm-3">
                            <div class="form-control-static">(单位：
                                <label class="jq-day">1</label>
                                <label class="jq-piex">天</label>
                                )
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">项目描述: </label>

                        <div class="col-sm-10">
                            <script id="editor" type="text/plain"></script>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">申请材料: </label>

                        <div class="col-sm-10">
                            <button type="button" class="btn-upload btn btn-info">＋</button>
                        </div>
                    </div>
                    <div class="upload-box"></div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">预计出借金额（元）: </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-pay jq-money" placeholder="请输入内容" datatype="money_fl">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">投资手续费比例(%): </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-fee jq-money" placeholder="请输入内容" datatype="money_fl">
                        </div>
                        <div class="col-sm-6">
                            <div class="form-control-static"> 还款时收取所得利息的百分比。</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">最小投资金额（元）: </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-min-pay jq-money" placeholder="50.00" datatype="money_fl">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">投资递增金额（元）: </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-add-pay jq-money" placeholder="50.00" datatype="money_fl">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">单笔最大投资金额（元）: </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-max-pay jq-money" placeholder="999999.00" datatype="money_fl">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">活动类型: </label>

                        <div class="col-sm-4">
                            <select class="selectpicker ">
                            <#list activityTypes as activityType>
                                <option value="${activityType.activityTypeCode}">
                                ${activityType.activityTypeName}
                                </option>
                            </#list>
                            </select>
                            <input type="hidden" class="jq-impact-type"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">活动利率(%): </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-percent jq-money" placeholder="请输入内容" datatype="money_fl">
                        </div>
                        <div class="col-sm-6">
                            <div class="form-control-static">适用于所有标(0 表示无),站点前端以(基本利率%+加息利率%)方式展现,如(10%+2%)。</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">基本利率(%): </label>

                        <div class="col-sm-4">
                            <input type="text" class="form-control jq-base-percent jq-money" placeholder="请输入内容" datatype="money_fl">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">合同: </label>

                        <div class="col-sm-4">
                            <select class="selectpicker ">
                            <#list contracts as contract>
                                <option value="${contract.id}">
                                ${contract.contractName}
                                </option>
                            </#list>
                            </select>
                            <input type="hidden" class="jq-pact"/>
                        </div>
                    </div>
                    <div class="form-group input-append">
                        <label class="col-sm-2 control-label">筹款启动时间: </label>

                        <div class="col-sm-4">
                            <div class='input-group date' id='datetimepicker6'>
                                <input type='text' class="form-control jq-star-date"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group input-append">
                        <label class="col-sm-2 control-label">筹款截止时间: </label>

                        <div class="col-sm-4">
                            <div class='input-group date' id='datetimepicker7'>
                                <input type='text' class="form-control jq-end-date"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                            </div>
                        </div>
                    </div>

                    <!--<div class="form-group">-->
                    <!--<label  class="col-sm-2 control-label">初审是否通过: </label>-->

                    <!--<div class="col-sm-4">-->
                    <!--<span class="label label-success "> 是</span>-->
                    <!--</div>-->
                    <!--</div>-->
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
                    <div class="form-group">
                        <label class="col-sm-2 control-label">操作: </label>

                        <div class="col-sm-4">

                            <button type="button" class="btn jq-btn-form btn-primary">保存</button>
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