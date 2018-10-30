<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="upload_apk.js" headLab="security" sideLab="appVersion" title="app版本管理">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal article-form" action="/announce-manage/article/create" method="post">

            <input type="hidden" class="jq-id" >
            <div class="form-group">
                <label class="col-sm-1 control-label">apk包: </label>
                <div class="col-sm-4 ">
                    <input type="text" name="thumbPicture" value="" readonly class="form-control article-thumbPicture"
                           placeholder="" datatype="*" errormsg="apk包不能为空" >
                </div>
                <div class="col-sm-4 thumbPicture">
                    <input type="file" name="apkFile" imageWidth="140" imageHeight="140"/>
                </div>
                <div class="col-sm-4 text-danger">
                    (apk)
                </div>

            </div>

            <div class="form-group">
                <label class="col-sm-1 control-label">version.json文件: </label>
                <div class="col-sm-4">
                    <input type="text" name="showPicture" value="" readonly class="form-control article-showPicture"
                           placeholder="" imageWidth="750" imageHeight="340">
                </div>
                <div class="col-sm-4 showPicture">
                    <input type="file" name="versionFile" imageWidth="750" imageHeight="340"/>
                </div>
                <div class="col-sm-4 text-danger">
                       (version.json)
                </div>
            </div>


            <div class="form-group">
                <label class="col-sm-2 control-label"></label>

                <div class="col-sm-4 form-error">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">操作: </label>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="col-sm-4">
                    <button type="button" class="btn jq-btn-form btn-primary article-save">更新</button>
                </div>
            </div>
        </form>
    </div>
</div>
<!-- content area end -->
</@global.main>