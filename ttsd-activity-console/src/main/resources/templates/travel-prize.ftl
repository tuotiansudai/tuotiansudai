<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="travel-prize.js" headLab="activity-manage" sideLab="activityCenter" title="活动中心管理">

<div class="col-md-10">
    <div class="panel panel-default">
        <div class="panel-body">
            <a class="btn btn-default btn-primary" href="/activity-console/activity-manage/travel/user-travel-list" role="button">旅游奖品获奖记录</a>
            <a class="btn btn-default" href="/activity-console/activity-manage/travel/travel-prize-list" role="button">旅游奖品更新</a>
            <a class="btn btn-default" href="#" role="button">奢侈品获奖记录</a>
            <a class="btn btn-default" href="#" role="button">奢侈品更新</a>
        </div>
    </div>

    <form class="form-horizontal" action="/activity-console/activity-manage/travel/travel-prize" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="hidden" name="id" value="${data.id?string.computer}"/>
        <div class="form-group clearfix">
            <label class="col-sm-1 control-label">奖品名称: </label>
            <div class="col-sm-2">
                <input type="text" name="name" class="form-control" value="${data.name}">
            </div>
        </div>
        <div class="form-group clearfix">
            <label class="col-sm-1 control-label">奖品描述: </label>
            <div class="col-sm-2">
                <input type="text" name="description" class="form-control" value="${data.description}">
            </div>
        </div>
        <div class="form-group clearfix">
            <label class="col-sm-1 control-label">奖品价值</label>
            <div class="col-sm-2">
                <div class="input-group">
                    <input type="text" name="price" class="form-control" value="${data.price}">
                    <div class="input-group-addon">元</div>
                </div>
            </div>
        </div>
        <div class="form-group clearfix">
            <label class="col-sm-1 control-label">奖品图片: </label>
            <div class="col-sm-4 ">
                <input type="text" name="image" readonly class="form-control" value="${data.image}">
                <div class="thumbImage">
                </div>
            </div>
            <div class="col-sm-4 text-danger">
                (图片最大为:250px * 250px)
            </div>
        </div>
        <div class="form-group clearfix">
            <label class="col-sm-1 control-label">获奖资格</label>
            <div class="col-sm-2">
                <div class="input-group">
                    <div class="input-group-addon">投资满</div>
                    <input type="text" name="investAmount" class="form-control" value="${data.investAmount}">
                    <div class="input-group-addon">元</div>
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-3">
                <input class="btn btn-default btn-submit" type="submit" value="更新">
            </div>
        </div>
    </form>
</div>
<!-- content area end -->

<!-- Modal -->
<div class="modal fade" id="confirm-modal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <h5>确认更新？</h5>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-default btn-submit">确认</button>
            </div>
        </div>
    </div>
</div>
</@global.main>