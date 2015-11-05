<!DOCTYPE html>
<html>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>投资记录</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <!-- link bootstrap css and js -->
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <script src="${requestContext.getContextPath()}/js/libs/jquery-1.11.3.min.js"></script>
    <!-- jquery -->
    <script src="${requestContext.getContextPath()}/js/libs/bootstrap.min.js"></script>
    <!-- link bootstrap css and js -->

    <!-- 日历插件 -->
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/bootstrap-datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap-select.css" rel="stylesheet"/>
    <script src="${requestContext.getContextPath()}/js/libs/moment-with-locales.min.js"></script>
    <script src="${requestContext.getContextPath()}/js/libs/bootstrap-datetimepicker.js"></script>
    <script src="${requestContext.getContextPath()}/js/libs/bootstrap-select.js"></script>
    <!--自动补全-->
    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/libs/jquery-ui/jquery-ui-1.11.4.min.css"/>
    <script src="${requestContext.getContextPath()}/js/libs/jquery-ui-1.11.4.min.js"></script>
    <!--自动补全-->
    <script type="text/javascript">
        $(function () {
            $('#datetimepicker1').datetimepicker({format: 'YYYY-MM-DD', maxDate: 'now'});
            $('#datetimepicker2').datetimepicker({format: 'YYYY-MM-DD', maxDate: 'now'});
            var dpicker2 = $('#datetimepicker2').data("DateTimePicker");
            $('#datetimepicker1').on('dp.change', function (e) {
                dpicker2.minDate(e.date);
            });
            $('form button[type="reset"]').click(function () {
                location.href = "invests";
            });
            $('form button[type="submit"]').click(function (event) {
                var queryParams = '';
                if ($('form input[name="loanId"]').val()) {
                    queryParams += "loanId=" + $('form input[name="loanId"]').val() + "&";
                }
                if ($('form input[name="loginName"]').val().length > 0) {
                    queryParams += "loginName=" + $('form input[name="loginName"]').val() + "&";
                }
                if ($('form input[name="startTime"]').val()) {
                    queryParams += "startTime=" + $('form input[name="startTime"]').val() + "&";
                }
                if ($('form input[name="endTime"]').val()) {
                    queryParams += "endTime=" + $('form input[name="endTime"]').val() + "&";
                }
                if ($('form select[name="investStatus"]').val()) {
                    queryParams += "investStatus=" + $('form select[name="investStatus"]').val() + "&";
                }
                location.href = "${requestContext.getContextPath()}/invests?" + queryParams;
                return false;
            });

            //自动完成提示
            var autoValue = '';
            var api_url = '${requestContext.getContextPath()}/loan/loaner';
            $("#tags").autocomplete({
                source: function (query, process) {
                    //var matchCount = this.options.items;//返回结果集最大数量
                    $.get(api_url + '/' + query.term, function (respData) {
                        autoValue = respData;
                        return process(respData);
                    });
                }
            });
            $("#tags").blur(function () {
                for (var i = 0; i < autoValue.length; i++) {
                    if ($(this).val() == autoValue[i]) {
                        $(this).removeClass('Validform_error');
                        return false;
                    } else {
                        $(this).addClass('Validform_error');
                    }

                }

            });
        });
    </script>
    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/index.css">
</head>
<body>

<@menu.header label="finaMan"></@menu.header>
<!-- main begin -->
<div class="main">
    <div class="container-fluid">
        <div class="row">

        <@menu.sidebar headLab="finaMan" sideLab="userInvest"></@menu.sidebar>

            <!-- content area begin -->
            <div class="col-md-10">
                <form action="" method="get" class="form-inline query-build">
                    <div class="form-group">
                        <label for="number">项目编号</label>
                        <input type="text" class="form-control" name="loanId" placeholder=""
                               value="${(loanId?string.computer)!}">
                    </div>
                    <div class="form-group">
                        <label for="number">投资人</label>
                        <input type="text" id="tags" name="loginName" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${loginName!}"/>
                    </div>
                    <div class="form-group">
                        <label for="number">日期</label>

                        <div class='input-group date' id='datetimepicker1'>
                            <input type='text' class="form-control" name="startTime"
                                   value="${(startTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                        </div>
                        -
                        <div class='input-group date' id='datetimepicker2'>
                            <input type='text' class="form-control" name="endTime"
                                   value="${(endTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="project">投资状态</label>
                        <select class="selectpicker" name="investStatus">
                            <option value="" <#if !(investStatus??)>selected</#if>>全部</option>
                        <#list investStatusList as status>
                            <option value="${status}" <#if investStatus?? && status==investStatus>selected</#if>>
                            ${status.description}
                            </option>
                        </#list>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
                    <button type="reset" class="btn btn-sm btn-default btnSearch">重置</button>
                </form>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>项目编号</th>
                            <th>项目名称</th>
                            <th>项目类型</th>
                            <th>投资人</th>
                            <th>推荐人</th>
                            <th>投资类型</th>
                            <th>投资时间</th>
                            <th>自动投标</th>
                            <th>投资金额</th>
                            <th>投资状态</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list data.records as invest>
                        <tr>
                            <td>${invest.loanId?string.computer}</td>
                            <td>${invest.loanName}</td>
                            <td>${invest.loanType}</td>
                            <td>${invest.investorLoginName!}</td>
                            <td>${invest.referrerLoginName!}</td>
                            <td>${invest.source}</td>
                            <td>${invest.createdTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td>${invest.autoInvest?then('是','否')}</td>
                            <td>${invest.amount}</td>
                            <td>${invest.status}</td>
                        </tr>
                        <#else>
                        <tr>
                            <td colspan="10">Empty</td>
                        </tr>
                        </#list>
                        </tbody>

                    </table>
                </div>

                <!-- pagination  -->
                <nav>
                    <div>
                        <span class="bordern">总共${data.count}条, 每页显示${data.pageSize}条</span>
                    </div>
                <#if data.records?has_content>
                    <ul class="pagination">
                        <li>
                            <#if data.hasPreviousPage >
                            <a href="?index=${data.index - 1}
                            <#if loanId??>loanId=${loanId}&</#if>
                            <#if loginName??>loginName=${loginName}&</#if>
                            <#if startTime??>startTime=${startTime}&</#if>
                            <#if endTime??>endTime=${endTime!}&</#if>
                            <#if investStatus??>investStatus=${investStatus}&</#if>" aria-label="Previous">
                            <#else>
                            <a href="#" aria-label="Previous">
                            </#if>
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                        </li>
                        <li><a>${data.index}</a></li>
                        <li>
                            <#if data.hasNextPage>
                            <a href="?index=${data.index + 1}
                            <#if loanId??>loanId=${loanId}&</#if>
                            <#if loginName??>loginName=${loginName}&</#if>
                            <#if startTime??>startTime=${startTime}&</#if>
                            <#if endTime??>endTime=${endTime!}&</#if>
                            <#if investStatus??>investStatus=${investStatus}&</#if>" aria-label="Next">
                            <#else>
                            <a href="#" aria-label="Next">
                            </#if>
                            <span aria-hidden="true">Next &raquo;</span>
                        </a>
                        </li>
                    </ul>
                </#if>
                </nav>
                <!-- pagination -->
            </div>
            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->

</body>
</html>