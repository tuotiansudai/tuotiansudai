<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<#import "macro/menu.ftl" as menu>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>公告管理</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${requestContext.getContextPath()}/style/libs/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${requestContext.getContextPath()}/style/index.css">
<@global.javascript pageJavascript="announce.js"></@global.javascript>
<@global.csrf></@global.csrf>
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
                            <input type="text" class="form-control jq-id" value="${(id?string('0'))!}">
                        </div>
                        <div class="form-group">
                            <label for="control-label">标题</label>
                            <input type="text" class="form-control jq-title" value="${title!}">
                        </div>
                        <button class="btn btn-primary search" type="button">查询</button>


                        <button class="btn btn-default pull-right publishAD" type="button"> 发布公告</button>

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
                        <#list announcementManagements as announcementManagement>
                            <tr>
                                <td>${(announcementManagement.id?string('0'))!}</td>
                                <td>${announcementManagement.title!}</td>
                                <td>${(announcementManagement.updateTime?string('yyyy-MM-dd'))!}</td>
                                <td><a href="/announceEdit/${(announcementManagement.id?string('0'))!}" class="btn btn-link"> 编辑</a> | <a href="#" class="btn btn-link jq-delete" data-id="${(announcementManagement.id?string('0'))!}">删除</a></td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <div class="row">
                    <!-- pagination  -->
                    <nav class="pagination-control">
                        <div><span class="bordern">总共${announcementManagementCount}条,每页显示${pageSize}条</span></div>
                        <ul class="pagination pull-left">

                            <li>
                                <#if hasPreviousPage >
                                    <a href="/announceManage?id=${(id?string('0'))!}&title=${title!}&currentPageNo=${currentPageNo-1}&pageSize=${pageSize}">
                                <#else>
                                    <a href="#">
                                </#if>
                                    <span>« Prev</span>
                                </a>
                            </li>
                            <li><a>${currentPageNo}</a></li>
                            <li>
                                <#if hasNextPage>
                                    <a href="/announceManage?id=${(id?string('0'))!}&title=${title!}&currentPageNo=${currentPageNo+1}&pageSize=${pageSize}">
                                <#else>
                                    <a href="#">
                                </#if>
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