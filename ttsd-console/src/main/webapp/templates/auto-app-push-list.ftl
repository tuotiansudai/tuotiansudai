<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="auto-app-push-list.js" headLab="app-push-manage" sideLab="autoAppPushManage" title="自动推送管理">

<!-- content area begin -->
<div class="col-md-10">
    <div class="tip-container">
        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <span class="txt"></span>
        </div>
    </div>
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    编号
                </th>
                <th>
                    通知名称
                </th>
                <th>
                    推送条件
                </th>
                <th>
                    推送渠道
                </th>
                <th>
                    推送模板
                </th>
                <th>
                    状态
                </th>
                <th>
                    操作
                </th>
                <th>
                    创建人
                </th>
                <th>
                    最后操作人
                </th>
            </tr>
            </thead>
            <tbody>
                <#--<#list pushAlerts as pushAlert>-->
                <tr>
                    <td>1</td>
                    <td>生日提醒</td>
                    <td>每月1日推送当月生日用户</td>
                    <td>Android / IOS</td>
                    <td>又长大一岁吧？是不是该多赚点钱，做点大人该做的事啦！生日快乐。</td>
                    <td>已启用</td>
                    <td>编辑 | 停用</td>
                    <td>系统</td>
                    <td>admin</td>
                </tr>
                <#--</#list>-->
            </tbody>
        </table>
    </div>

</div>
<!-- content area end -->
</@global.main>