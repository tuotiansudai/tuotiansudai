<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="promotion.js" headLab="activity-manage" sideLab="promotion" title="APP弹窗推送修改">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal promotion-form" method="post" action="/activity-console/activity-manage/promotion/edit">

            <div class="form-group">
                <label class="col-sm-2 control-label">活动名称: </label>
                <div class="col-sm-4">
                    <input type="hidden" name="id" class="jq-id" value="<#if promotion??>${promotion.id?c!}</#if>">
                    <input type="text" name="name" class="form-control promotion-name" value="<#if promotion??>${promotion.name!}</#if>" placeholder="" datatype="*" errormsg="活动名称不能为空">
                </div>
                <div class="col-sm-7">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">活动图片: </label>
                <div class="col-sm-4 ">
                    <input type="text" name="imageUrl" readonly value="<#if promotion??>${promotion.imageUrl!}</#if>" class="form-control promotionImageUrl" placeholder="" datatype="*" errormsg="活动图片不能为空">
                    <div class="imageUrl">
                        <#if promotion??&&promotion.imageUrl??>
                            <img style="width:100%" src="${staticServer}${promotion.imageUrl!}" alt="活动图片"/>
                        </#if>
                    </div>
                </div>
                <div class="col-sm-4 promotionImageUrl">
                    <input type="file" name="imageFile" imageWidth="500" imageHeight="660"/>
                </div>
                <div class="col-sm-4 text-danger">
                    (图片必须是500px * 660px)
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">APP点击定位到: </label>
                <div class="col-sm-4">
                    <select class="selectpicker linkUrl" name="linkUrl">
                        <#list appUrls as appUrl>
                            <option value="${appUrl.path}"  <#if promotion?? &&  appUrl.path == promotion.linkUrl>selected</#if>>${appUrl.description}</option>
                        </#list>
                    </select>
                    <div class="app-push-link jump-to-link">定位地址:<input type="text" class="form-control jump-link-text" name="jumpToLink" <#if promotion??>value="${promotion.jumpToLink!}"</#if> placeholder=""  maxlength="100" datatype="*" errormsg="定位地址不能为空"></div>
                </div>
                <div class="col-sm-7">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">推送时间: </label>

                <div class="col-sm-2">
                    <div class='input-group date' id='startTime'>
                        <input type='text' class="form-control start-time" name="startTime"
                               <#if promotion??>value="${(promotion.startTime?string("yyyy-MM-dd"))!}"</#if>
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
                               <#if promotion??>value="${(promotion.endTime?string("yyyy-MM-dd"))!}"</#if> datatype="*"
                               errormsg="请选择推送结束时间"/>
                <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
                </span>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">顺序: </label>
                <div class="col-sm-4">
                    <input type="text" name="seq" value="<#if promotion??>${promotion.seq!}</#if>" class="form-control seq" placeholder="" datatype="n" errormsg="顺序只能为正整数">
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