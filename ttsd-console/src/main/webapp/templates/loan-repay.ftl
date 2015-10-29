<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<#assign loanRepays = baseDto.data>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>所有借款</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <!-- link bootstrap css and js -->
    <link href="../../style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="../../style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <!-- jquery -->
    <!-- link bootstrap css and js -->
    <!--下拉框-->
    <link rel="stylesheet" href="../../style/libs/bootstrap-select.css"/>
    <!--下拉框-->
    <!-- 日历插件 -->
    <link href="../../style/libs/bootstrap/bootstrap-datetimepicker/bootstrap-datetimepicker.css" rel="stylesheet">
    <!--自动补全-->

    <link rel="stylesheet" href="../../style/libs/jquery-ui/jquery-ui-1.10.3.custom.css"/>

    <link rel="stylesheet" href="../../style/index.css">
<@global.javascript pageJavascript="loan-repay.js"></@global.javascript>

</head>
<body>

<@menu.header label="proMan"></@menu.header>

<!-- main begin -->
<div class="main">
    <div class="container-fluid">
        <div class="row">

        <@menu.sidebar headLab="projectMain" sideLab="repaymentInfoList"></@menu.sidebar>

            <!-- content area begin -->
            <div class="col-md-10">
                <form action="${requestContext.getContextPath()}/loan-repay" method="post" class="form-inline query-build">
                    <div class="form-group">
                        <label for="number">项目编号:</label>
                        <input type="text" class="form-control" id="loanId" placeholder="" value="${(loanId?string("0"))!}">
                    </div>
                    <div class="form-group">
                        <label for="number">用户名:</label>
                        <input type="text" id="loginName" name="loginName" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${loginName!}"/>
                    </div>
                    <div class="form-group">
                        <label for="number">开始时间:</label>

                        <div class='input-group date' id='datetimepicker1'>
                            <input type='text' class="form-control" id="startTime" value="${(startTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                        </div>

                        <label for="number">结束时间:</label>
                        <div class='input-group date' id='datetimepicker2'>
                            <input type='text' class="form-control" id="endTime" value="${(endTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                        </div>

                    </div>
                    <div class="form-group">
                        <label for="" >标的类型: </label>

                            <select class="selectpicker" name="repayStatus" id="repayStatus">
                                    <option value="">全部</option>
                                <#list repayStatusList as status>
                                    <option value="${status}"
                                            <#if repayStatus?has_content && status == repayStatus>selected</#if>
                                            >${status.description}</option>
                                </#list>
                            </select>
                    </div>
                    <button type="button" class="btn btn-sm btn-primary btnSearch" id="btnRepayQuery" pageIndex="1">查询</button>
                    <button type="reset" class="btn btn-sm btn-default btnSearch" id="btnRepayReset">重置</button>

                </form>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>项目编号</th>
                            <th>项目名称</th>
                            <th>还款人</th>
                            <th>还款日期</th>
                            <th>当前期数</th>
                            <th>应还本金</th>
                            <th>应还利息</th>
                            <th>应还总数</th>
                            <th>还款状态</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list loanRepays.records as loanRepay>
                            <tr>
                                <td>${loanRepay.loanId?string('0')}</td>
                                <td>${loanRepay.loanName}</td>
                                <td>${loanRepay.agentLoginName!}</td>
                                <td>${loanRepay.repayDate?string("yyyy-MM-dd HH:mm:ss")}</td>
                                <td>第${loanRepay.period}期</td>
                                <td>${loanRepay.corpus}</td>
                                <td>${loanRepay.expectedInterest}</td>
                                <td>${loanRepay.totalAmount}</td>
                                <td>${loanRepay.loanRepayStatus.getDescription()}</td>
                            </tr>

                        </#list>

                        </tbody>

                    </table>
                </div>

                <!-- pagination  -->
                <nav>

                    <div>
                        <span class="bordern">总共${loanRepays.count}条,每页显示${loanRepays.pageSize}条</span>
                    </div>
                    <ul class="pagination">

                         <#if loanRepays.hasPreviousPage>
                            <li>
                                <a href="#" aria-label="Previous">
                                    <span class="Previous" aria-hidden="true" pageIndex="${loanRepays.index - 1}">&laquo; Prev</span>
                                </a>
                            </li>
                         <#else >
                             <li>
                                 <a href="#" aria-label="Previous">
                                     <span class="Previous" aria-hidden="true" pageIndex="${loanRepays.index}">&laquo; Prev</span>
                                 </a>
                             </li>
                        </#if>
                             <li><a>${loanRepays.index}</a></li>

                        <#if loanRepays.hasNextPage>
                            <li>
                                <a href="#" aria-label="Next">
                                    <span class="Next" aria-hidden="true" pageIndex="${loanRepays.index + 1}">Next &raquo;</span>
                                </a>
                            </li>
                        <#else >
                            <li>
                                <a href="#" aria-label="Next">
                                    <span class="Next" aria-hidden="true" pageIndex="${loanRepays.index}">Next &raquo;</span>
                                </a>
                            </li>
                        </#if>

                    </ul>
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