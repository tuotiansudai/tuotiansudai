<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="activity-center.js" headLab="activity-manage" sideLab="activityCenter" title="添加活动">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal activity-form"  method="post">
            <div class="form-group">
                <input type="hidden" name="activityId" class="activityId" value="<#if dto??>${dto.activityId?c!}</#if>">
                <label class="col-sm-2 control-label">渠道: </label>
                <div class="col-sm-4">
                <#list sources as source>
                    <#if ['ANDROID','IOS','WEB']?seq_contains(source.name())>
                        <input type="checkbox" name="source" class="activity-source" value="${source.name()}"
                               <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                               <#if dto??&&dto.source?seq_contains(source.name())>checked</#if>/>${source.name()}
                    </#if>
                </#list>

                </div>
                <div class="col-sm-7">

                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">活动名称: </label>
                <div class="col-sm-4">
                    <input type="text" name="title"  class="form-control activity-title" <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.title!}</#if>" placeholder="" datatype="*" errormsg="活动名称不能为空">
                </div>
                <div class="col-sm-7">

                </div>

            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">活动图(移动端): </label>
                <div class="col-sm-4 ">
                    <input type="text" name="appPictureUrl"  readonly class="form-control appPictureUrl" <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.appPictureUrl!}</#if>"
                           placeholder="" datatype="*" errormsg="活动图(移动端)不能为空" >
                    <div class="appPictureImage">
                        <#if dto??&&dto.appPictureUrl??>
                            <img style="width:100%" src="/${dto.appPictureUrl!}" alt="活动图(移动端)"/>
                        </#if>
                    </div>
                </div>
                <#if dto??&&dto.status != 'TO_APPROVE'>
                    <div class="col-sm-4 appPicture">
                        <input type="file" name="appPictureImage" imageWidth="750" imageHeight="340"/>
                    </div>
                    <div class="col-sm-4 text-danger">
                        (图片大小为:750px * 340px)
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">活动图(PC端): </label>
                <div class="col-sm-4 ">
                    <input type="text" name="webPictureUrl"  readonly class="form-control webPictureUrl"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.webPictureUrl!}</#if>"
                           placeholder="" datatype="*" errormsg="活动图(PC端)不能为空" >
                    <div class="webPictureImage">
                        <#if dto??&&dto.webPictureUrl??>
                            <img style="width:100%" src="/${dto.webPictureUrl!}" alt="活动图(PC端)"/>
                        </#if>
                    </div>
                </div>
                <#if dto??&&dto.status != 'TO_APPROVE'>
                    <div class="col-sm-4 webPicture">
                        <input type="file" name="webPictureImage" imageWidth="1920" imageHeight="350"/>
                    </div>
                    <div class="col-sm-4 text-danger">
                        (图片大小为:1920px * 350px)
                    </div>
                </#if>


            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">目标地址(移动端): </label>
                <div class="col-sm-4">
                    <input type="text" name="appActivityUrl"  class="form-control appActivityUrl"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.appActivityUrl!}</#if>" placeholder="" datatype="*" errormsg="目标地址(移动端)不能为空">
                </div>
                <div class="col-sm-7">
                </div>

            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">目标地址(PC端):</label>
                <div class="col-sm-4">
                    <input type="text" name="webActivityUrl"  class="form-control webActivityUrl"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.webActivityUrl!}</#if>" placeholder="" datatype="*" errormsg="目标地址(PC端)不能为空">
                </div>
                <div class="col-sm-7">
                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">活动介绍: </label>
                <div class="col-sm-4">
                    <input type="text" name="description"  class="form-control activity-description"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.description!}</#if>" placeholder="" datatype="*" errormsg="活动介绍不能为空">
                </div>
                <div class="col-sm-7">

                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">结束时间: </label>
                <div class='input-group date col-sm-3' id='datetimepicker1'>
                    <input type='text' class="form-control" name="expiredTime"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.expiredTime?string('yyyy-MM-dd HH:mm')}</#if>"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                </div>
                <div class="col-sm-7">

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
                <@security.authorize access="hasAuthority('OPERATOR')">
                    <#if !(dto??) || dto?? && ["APPROVED", "REJECTION"]?seq_contains(dto.status)>
                        <div class="col-sm-7">
                            <button type="button" class="btn jq-btn-form btn-primary activity-to_approve" >提交审核</button>
                        </div>
                    </#if>
                </@security.authorize>
                <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                    <#if dto?? && ["TO_APPROVE"]?seq_contains(dto.status)>
                        <div class="col-sm-3">
                            <button type="button" class="btn jq-btn-form btn-primary activity-rejection" >驳回</button>
                            <button type="button" class="btn jq-btn-form btn-primary activity-approved" >审核通过</button>
                        </div>
                    </#if>

                </@security.authorize>
            </div>
        </form>
    </div>
</div>
<!-- content area end -->
</@global.main>