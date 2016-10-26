<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="promotion.js" headLab="activity-manage" sideLab="promotion" title="APP弹窗推送添加">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal promotion-form" method="post" action="">
            <div class="form-group">
                <label class="col-sm-2 control-label">活动名称: </label>
                <div class="col-sm-4">
                    <input type="text" name="name" class="form-control promotion-name" value="" placeholder="" datatype="*" errormsg="活动名称不能为空">
                </div>
                <div class="col-sm-7">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">活动图片: </label>
                <div class="col-sm-4 ">
                    <input type="text" name="imageUrl" readonly value="" class="form-control promotionImageUrl" placeholder="" datatype="*" errormsg="活动图片不能为空">
                    <div class="imageUrl">
                        <#if promotion??&&promotion.imageUrl??>
                            <img style="width:100%" src="" alt="活动图片"/>
                        </#if>
                    </div>
                </div>
                <div class="col-sm-4 promotionImageUrl">
                    <input type="file" name="imageFile" imageWidth="530" imageHeight="660"/>
                </div>
                <div class="col-sm-4 text-danger">
                    (图片必须是530px * 660px)
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">目标地址: </label>
                <div class="col-sm-4 ">
                    <input type="text" name="linkUrl" class="form-control promotionLinkUrl" value=""  placeholder="" datatype="*" errormsg="目标地址不能为空">
                </div>
            </div>


            <div class="form-group">
                <label class="col-sm-2 control-label">推送时间: </label>

                <div class="col-sm-2">
                    <div class='input-group date' id='startTime'>
                        <input type='text' class="form-control start-time" name="startTime"
                               value=""
                               datatype="*" errormsg="请选择推送开始时间"/>
                <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
                </span>
                    </div>
                </div>
                <div class="line-size">-</div>
                <div class="col-sm-2">
                    <div class='input-group date' id='endTime'>
                        <input type='text' class="form-control end-time" name="endTime"
                               value=""
                               datatype="*" errormsg="请选择推送结束时间"/>
                <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
                </span>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">顺序: </label>
                <div class="col-sm-4">
                    <input type="text" name="seq" value="" class="form-control seq" placeholder="" datatype="n" errormsg="顺序只能为正整数">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label"></label>

                <div class="col-sm-4 form-error">
                </div>
            </div>
            <div class="form-group">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="col-sm-4">
                </div>

                <div class="col-sm-3">
                    <@security.authorize access="hasAnyAuthority('OPERATOR')">
                        <button type="button" class="btn jq-btn-form btn-primary promotion-confirm">确定</button>
                        <button type="button" class="btn jq-btn-form btn-primary promotion-cancel">取消</button>
                    </@security.authorize>
                </div>
            </div>

        </form>
    </div>
</div>
<!-- content area end -->
</@global.main>