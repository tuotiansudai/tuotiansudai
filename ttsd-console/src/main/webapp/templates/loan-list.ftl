<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
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
    <link href="../style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="../style/libs/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">
    <!-- link bootstrap css and js -->
    <link href="../../style/libs/bootstrap-datepicker.css" rel="stylesheet">
    <@global.javascript pageJavascript="loanList.js"></@global.javascript>
    <link rel="stylesheet" href="../style/index.css">
</head>
<body>
<!-- header begin -->
<@menu.header label="projectMain"></@menu.header>
<!-- header end -->

<!-- main begin -->
<div class="main">
    <div class="container-fluid">
        <div class="row">
            <!-- menu sidebar -->
            <#if status??>
                <#assign loanStatus="${status}">
            <#else>
                <#assign loanStatus="ALL">
            </#if>
            <@menu.sidebar headLab="projectMain" sideLab="${loanStatus}"></@menu.sidebar>
            <!-- menu sidebar end -->

            <!-- content area begin -->
            <div class="col-md-10">
                <form action="" class="form-inline query-build" id="formLoanList">
                    <input type="hidden" class="status" name="status" value="<#if status??>${status}</#if>">
                    <div class="form-group">
                        <label for="number">编号</label>
                        <input type="text" class="form-control loanId" name="loanId"  placeholder="" value="${(loanId?string('0'))!}">
                    </div>
                    <div class="form-group">
                        <label for="number">项目名称</label>
                        <input type="text" class="form-control loanName" name="loanName"  placeholder="" value="${loanName!}">
                    </div>
                    <div class="form-group">
                        <label for="number">日期</label>

                        <div class='input-group date' id='datepickerBegin'>
                            <input type='text' class="form-control" name="startTime" value="${(startTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                        </div>
                        -
                        <div class='input-group date' id='datepickerEnd'>
                            <input type='text' class="form-control" name="endTime" value="${(endTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                        </div>
                    </div>

                    <button type="button" class="btn btn-sm btn-primary search">查询</button>
                </form>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover ">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th>项目名称</th>
                            <th>借款人</th>
                            <th>借款金额</th>
                            <th>借款期限</th>
                            <th>年化/活动(利率)</th>
                            <th>项目状态</th>
                            <th>发起时间</th>
                            <th>投资/还款记录</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list loanListDtos as loanListDto>
                        <tr>
                            <td>${loanListDto.id?string('0')}</td>
                            <td class="projectName"><span class="add-tooltip" data-placement="top" data-toggle="tooltip" data-original-title="${loanListDto.name}">${loanListDto.name}</span></td>
                            <td>${loanListDto.agentLoginName}</td>
                            <td class="td">${loanListDto.loanAmount/100}</td>
                            <td class="td">${loanListDto.periods}</td>
                            <td>${loanListDto.basicRate}/${loanListDto.activityRate}</td>
                            <td>${loanListDto.status.getDescription()}</td>
                            <td>${loanListDto.createdTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td><a class="invest_repay" href="/invests?loanId=${loanListDto.id?string('0')}">投资</a>/<a class="loan_repay" href="/loan-repay?loanId=${loanListDto.id?string('0')}&loginName=&repayStartDate=&repayEndDate=&repayStatus=&index=1&pageSize=10">还款记录</a></td>
                            <td><a class="loan_edit" href="/loan/${loanListDto.id?string('0')}">编辑</a></td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <!-- pagination  -->
                <nav>
                    <div>
                        <span class="bordern">总共${loanListCount}条,每页显示${pageSize}条</span>
                    </div>
                <#if loanListDtos?has_content>
                    <ul class="pagination">

                        <li>
                            <#if hasPreviousPage >
                            <a href="?status=${status!}&currentPageNo=${currentPageNo-1}&pageSize=${pageSize}&loanId=${loanId!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&loanName=${loanName!}" aria-label="Previous">
                            <#else>
                            <a href="#" aria-label="Previous">
                            </#if>
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                        </li>
                        <li><a>${currentPageNo}</a></li>
                        <li>
                            <#if hasNextPage >
                            <a href="?status=${status!}&currentPageNo=${currentPageNo+1}&pageSize=${pageSize}&loanId=${loanId!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&loanName=${loanName!}" aria-label="Next">
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