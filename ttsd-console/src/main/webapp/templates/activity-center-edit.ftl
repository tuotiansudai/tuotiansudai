<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="activity-center.js" headLab="activity-manage" sideLab="activityCenter" title="添加活动">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal activity-form" action="" method="post">
            <div class="form-group">
                <label class="col-sm-2 control-label">渠道: </label>
                <div class="col-sm-4">
                    <input type="checkbox" name="source" class="activity-source"/>WEB
                    <input type="checkbox" name="source" class="activity-source"/>IOS
                    <input type="checkbox" name="source" class="activity-source"/>ANDROID
                </div>
                <div class="col-sm-7">

                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">活动名称: </label>
                <div class="col-sm-4">
                    <input type="text" name="title"  class="form-control activity-title"  placeholder="" datatype="*" errormsg="活动名称不能为空">
                </div>
                <div class="col-sm-7">

                </div>

            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">活动图(移动端): </label>
                <div class="col-sm-4 ">
                    <input type="text" name="appPictureUrl"  readonly class="form-control appPictureUrl"
                           placeholder="" datatype="*" errormsg="活动图(移动端)不能为空" >
                    <div class="appPictureImage">
                    </div>
                </div>
                <div class="col-sm-4 appPicture">
                    <input type="file" name="appPictureImage" imageWidth="750" imageHeight="340"/>
                </div>
                <div class="col-sm-4 text-danger">
                    (图片大小为:750px * 340px)
                </div>

            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">活动图(PC端): </label>
                <div class="col-sm-4 ">
                    <input type="text" name="webPictureUrl"  readonly class="form-control webPictureUrl"
                           placeholder="" datatype="*" errormsg="活动图(PC端)不能为空" >
                    <div class="webPictureImage">
                    </div>
                </div>
                <div class="col-sm-4 webPicture">
                    <input type="file" name="webPictureImage" imageWidth="1920" imageHeight="350"/>
                </div>
                <div class="col-sm-4 text-danger">
                    (图片大小为:1920px * 350px)
                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">目标地址(移动端): </label>
                <div class="col-sm-4">
                    <input type="text" name="appActivityUrl"  class="form-control appActivityUrl"  placeholder="" datatype="*" errormsg="目标地址(移动端)不能为空">
                </div>
                <div class="col-sm-7">
                </div>

            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label">目标地址(PC端):</label>
                <div class="col-sm-4">
                    <input type="text" name="webActivityUrl"  class="form-control webActivityUrl"  placeholder="" datatype="*" errormsg="目标地址(PC端)不能为空">
                </div>
                <div class="col-sm-7">
                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">活动介绍: </label>
                <div class="col-sm-4">
                    <input type="text" name="description"  class="form-control activity-description"  placeholder="" datatype="*" errormsg="活动介绍不能为空">
                </div>
                <div class="col-sm-7">

                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label">结束时间: </label>
                <div class='input-group date col-sm-3' id='datetimepicker1'>
                    <input type='text' class="form-control" name="expiredTime"
                           value=""/>
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
                <div class="col-sm-7">
                    <button type="button" class="btn jq-btn-form btn-primary activity-save">提交审核</button>
                </div>
            </div>
        </form>
    </div>
</div>
<!-- content area end -->
</@global.main>