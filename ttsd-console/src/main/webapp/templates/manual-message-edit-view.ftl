<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="manual-message-edit.js" headLab="message-manage" sideLab="messageMan" title="编辑手动信息">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal article-form" action="/message-manage/manual-message/manual-message-edit-view" method="post">
            <div class="form-group">
                <form>
                    <label class="col-sm-1 control-label"> 收件人: </label>
                    <input type="checkbox" name="ALL_USER"/>
                    全部
                    <input type="checkbox" name="STAFF"/>
                    业务员
                    <input type="checkbox" name="CHANNEL_USER"/>
                    渠道用户
                    <input type="checkbox" name="STAFF_RECOMMEND_LEVEL_ONE"/>
                    业务员的一级推荐
                    <input type="checkbox" name="NORMAL_USER"/>
                    自然用户
                    <br/>
                    <input type="checkbox" name="IMPORT_USER"/>
                    导入用户
                    <input type="button" name="importUsers" value="导入用户"/>
                </form>
            </div>

            <div class="form-group">
                <label class="col-sm-1 control-label">标题: </label>
                <input type="hidden" name="articleId" class="articleId" value="<#if dto??>${dto.articleId?c!}</#if>">

                <div class="col-sm-4">
                    <input type="text" name="title" class="form-control article-title"
                           value="<#if dto??>${dto.title!}</#if>" placeholder="" datatype="*" errormsg="标题不能为空"/>
                </div>
                <div class="col-sm-4">
                    不超过40个字
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-1 control-label">编辑内容: </label>

                <div class="col-sm-10">
                    <script id="editor"
                            type="text/plain"><#if dto??>${dto.content!}</#if></script>
                    <input type="hidden" name="content" class="article-content"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-1 control-label">送达渠道: </label>
                <form>
                    <input type="checkbox" name="WEB" value="true"/>
                    站内
                    <input type="checkbox" name="APP_MESSAGE" value="true"/>
                    APP消息中心
                    <input type="checkbox" name="SMS" value="false"/>
                    短信
                    <input type="checkbox" name="MAIL" value="false"/>
                    邮件
                </form>
            </div>

            <div class="form-group">
                <label class="col-sm-1 control-label">操作: </label>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                <div class="col-sm-4">
                    <button type="button" class="btn jq-btn-form btn-primary article-save">提交</button>
                </div>
            </div>
        </form>
    </div>
</div>
<!-- content area end -->
</@global.main>