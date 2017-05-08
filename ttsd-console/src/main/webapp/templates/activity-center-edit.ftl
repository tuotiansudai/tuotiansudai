<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="activity-center.js" headLab="activity-manage" sideLab="activityCenter" title="添加活动">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal activity-form" method="post">
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
                <label class="col-sm-2 control-label">活动顺序: </label>

                <div class="col-sm-4">
                    <input type="text" name="seq" class="form-control activity-seq"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${(dto.seq?c)!}</#if>" placeholder="" datatype="*"
                           errormsg="活动顺序不能为空">
                </div>
                <div class="col-sm-7">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">活动名称: </label>

                <div class="col-sm-4">
                    <input type="text" name="title" class="form-control activity-title"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.title!}</#if>" placeholder="" datatype="*" errormsg="活动名称不能为空">
                </div>
                <div class="col-sm-7">

                </div>

            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">活动横图(移动端): </label>

                <div class="col-sm-4 ">
                    <input type="text" name="appPictureUrl" readonly class="form-control appPictureUrl"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.appPictureUrl!}</#if>"
                           placeholder="" datatype="*" errormsg="活动图(移动端)不能为空">

                    <div class="appPictureImage">
                        <#if dto??&&dto.appPictureUrl??>
                            <img style="width:100%" src="${commonStaticServer}${dto.appPictureUrl!}" alt="活动图(移动端)"/>
                        </#if>
                    </div>
                </div>
                <#if !(dto??) || dto??&&dto.status != 'TO_APPROVE'>
                    <div class="col-sm-4 appPicture">
                        <input type="file" name="appPictureImage" imageWidth="750" imageHeight="350"/>
                    </div>
                    <div class="col-sm-4 text-danger">
                        (图片大小为:750px * 350px)
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">活动竖图(移动端): </label>

                <div class="col-sm-4 ">
                    <input type="text" name="appVerticalPictureUrl" readonly class="form-control appVerticalPictureUrl"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.appVerticalPictureUrl!}</#if>"
                           placeholder="" datatype="*" errormsg="活动竖图(移动端)不能为空">

                    <div class="appVerticalPictureImage">
                        <#if dto??&&dto.appVerticalPictureUrl??>
                            <img style="width:100%" src="${commonStaticServer}${dto.appVerticalPictureUrl!}" alt="活动竖图(移动端)"/>
                        </#if>
                    </div>
                </div>
                <#if !(dto??) || dto??&&dto.status != 'TO_APPROVE'>
                    <div class="col-sm-4 appVerticalPicture">
                        <input type="file" name="appVerticalPictureImage" imageWidth="500" imageHeight="660"/>
                    </div>
                    <div class="col-sm-4 text-danger">
                        (图片大小为:500px * 660px)
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">活动图(PC端): </label>

                <div class="col-sm-4 ">
                    <input type="text" name="webPictureUrl" readonly class="form-control webPictureUrl"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.webPictureUrl!}</#if>"
                           placeholder="" datatype="*" errormsg="活动图(PC端)不能为空">

                    <div class="webPictureImage">
                        <#if dto??&&dto.webPictureUrl??>
                            <img style="width:100%" src="${commonStaticServer}${dto.webPictureUrl!}" alt="活动图(PC端)"/>
                        </#if>
                    </div>
                </div>
                <#if !(dto??) || dto??&&dto.status != 'TO_APPROVE'>
                    <div class="col-sm-4 webPicture">
                        <input type="file" name="webPictureImage" imageWidth="1920" imageHeight="350"/>
                    </div>
                    <div class="col-sm-4 text-danger">
                        (图片大小为:1920px * 350px)
                    </div>
                </#if>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">APP点击定位到: </label>
                <div class="col-sm-4">
                    <select class="selectpicker appActivityUrl" name="appActivityUrl">
                        <#list appUrls as appUrl>
                            <option value="${appUrl.path}" <#if dto?? && appUrl.path == dto.appActivityUrl>selected</#if>>${appUrl.description}</option>
                        </#list>
                    </select>
                    <div class="app-push-link jump-to-link">定位地址:<input type="text" class="form-control jump-link-text" name="jumpToLink" <#if dto??>value="${dto.jumpToLink!}"</#if> placeholder=""  maxlength="100" datatype="*" errormsg="定位地址不能为空"></div>
                </div>
                <div class="col-sm-7">
                </div>

            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">目标地址(PC端):</label>

                <div class="col-sm-4">
                    <input type="text" name="webActivityUrl" class="form-control webActivityUrl"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.webActivityUrl!}</#if>" placeholder="" datatype="*"
                           errormsg="目标地址(PC端)不能为空">
                </div>
                <div class="col-sm-7">
                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">活动介绍: </label>

                <div class="col-sm-4">
                    <input type="text" name="description" class="form-control activity-description"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.description!}</#if>" placeholder="" datatype="*" errormsg="活动介绍不能为空">
                </div>
                <div class="col-sm-7">

                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">分享标题(移动端): </label>

                <div class="col-sm-4">
                    <input type="text" name="shareTitle" class="form-control activity-description"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.shareTitle!}</#if>" placeholder="" >
                </div>
                <div class="col-sm-7">

                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">分享内容(移动端): </label>

                <div class="col-sm-4">
                    <input type="text" name="shareContent" class="form-control activity-description"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.shareContent!}</#if>" placeholder="">
                </div>
                <div class="col-sm-7">

                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">分享链接(移动端): </label>

                <div class="col-sm-4">
                    <input type="text" name="shareUrl" class="form-control activity-description"
                           <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                           value="<#if dto??>${dto.shareUrl!}</#if>" placeholder="">
                </div>
                <div class="col-sm-7">

                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">活动类型: </label>

                <div class="col-sm-4" style="padding-top:7px;">
                    <label style="float:left;">长期活动:</label>
                    <input type="radio" name="longTerm"  class="col-sm-1 activity-longTerm"
                           id="longTerm"
                           style="box-shadow: none;"
                           value="longTerm"
                           <#if dto??&&dto.status == 'TO_APPROVE'>disabled</#if> <#if !(dto??) || dto??&&dto.longTerm=='longTerm' >
                           checked</#if> placeholder="">
                    <label style="float:left;">非长期活动:</label>
                    <input type="radio" name="longTerm"
                           class="col-sm-1 activity-longTerm" id="notLongTerm"
                           style="box-shadow: none;"
                           value="notLongTerm"
                           <#if dto??&&dto.status == 'TO_APPROVE'>disabled</#if> <#if dto??&&dto.longTerm == 'notLongTerm'>
                           checked</#if> placeholder="">
                </div>
                <div class="col-sm-7">
                </div>
            </div>

            <div class="form-group" id="activityTime" style="display: <#if dto?? && dto.longTerm !='longTerm'>block<#else>none</#if>">
                <label class="col-sm-2 control-label">活动时间: </label>

                <div class="date col-sm-2">
                    <div class="input-group" id='datetimepicker1'>
                        <input type='text' class="form-control activatedTime" name="activatedTime"
                               <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                               value="<#if dto??&&dto.activatedTime??>${dto.activatedTime?string('yyyy-MM-dd HH:mm')}</#if>"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                    </div>
                </div>
                <div style="float:left;text-align:center;line-height:34px;">-</div>
                <div class="date col-sm-2">
                    <div class="input-group" id='datetimepicker2'>
                        <input type='text' class="form-control expiredTime" name="expiredTime"
                               <#if dto??&&dto.status == 'TO_APPROVE'>readonly</#if>
                               value="<#if dto??&&dto.expiredTime??>${dto.expiredTime?string('yyyy-MM-dd HH:mm')}</#if>"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
                    </div>
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
                <@security.authorize access="hasAnyAuthority('OPERATOR', 'ADMIN')">
                    <#if !(dto??) || dto?? && ["APPROVED", "REJECTION"]?seq_contains(dto.status)>
                        <div class="col-sm-7">
                            <button type="button" class="btn jq-btn-form btn-primary activity-to_approve">提交审核</button>
                        </div>
                    </#if>
                </@security.authorize>
                <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                    <#if dto?? && ["TO_APPROVE"]?seq_contains(dto.status)>
                        <div class="col-sm-3">
                            <button type="button" class="btn jq-btn-form btn-primary activity-rejection">驳回</button>
                            <button type="button" class="btn jq-btn-form btn-primary activity-approved">审核通过</button>
                        </div>
                    </#if>

                </@security.authorize>
            </div>
        </form>
    </div>
</div>
<!-- content area end -->
</@global.main>