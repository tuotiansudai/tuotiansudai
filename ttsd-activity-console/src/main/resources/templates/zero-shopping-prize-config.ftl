<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="zero-shopping-list.js" headLab="activity-manage" sideLab="zeroShoppingActivity" title="0元购活动管理">

<div class="col-md-10">
    <div class="panel panel-default">
        <div class="panel-body">
            <a class="btn btn-default btn-primary" href="/activity-console/activity-manage/zero-shopping/user-prize-list" role="button">"0"元购获奖记录</a>
            <a class="btn btn-default btn-primary" href="/activity-console/activity-manage/zero-shopping/config-prize-list" role="button">奖品管理</a>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>奖品名称</th>
                <th>奖品价值（元）</th>
                <th>获奖资格</th>
                <th>总数</th>
                <th>剩余数量</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list data as item>
                <tr>
                    <td>${item.prize.description}</td>
                    <td>${item.prize.marketPrice/100}</td>
                    <td>投资满${item.prize.investPrice/100}</td>
                    <td>${item.prizeTotal}</td>
                    <td>${item.prizeSurplus}</td>
                    <td>
                        <button data-prize-id="${item.id}" class="count-update btn btn-default btn-sm"
                                data-prize-total="${item.prizeTotal}" data-prize-surplus="${item.prizeSurplus}">更改数量
                        </button>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>

<div class="modal fade bs-example-modal-sm" id="updatePrizeCount" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">更新奖品数量</h4>
            </div>
            <form data-toggle="validator" method="POST" id="updatePrizeCountForm" action="/activity-console/activity-manage/zero-shopping/update-prize-count">
                <input id="id" type="hidden" name="id" value="">
                <div class="modal-body">
                    <div class="modal-body">
                        <label class="control-label" for="prizeTotal">总数</label>
                        <input id="prizeTotal" required name="prizeTotal" class="form-control" value=""/>
                    </div>
                    <div class="modal-body">
                        <label class="control-label" for="prizeSurplus">剩余数量</label>
                        <input id="prizeSurplus" required name="prizeSurplus" class="form-control" value=""/>
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="submit" type="button" class="btn btn-primary">提交</button>
                </div>
            </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<!-- content area end -->
</@global.main>