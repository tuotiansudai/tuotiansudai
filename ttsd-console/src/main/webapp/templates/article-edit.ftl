<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="article-edit.js" headLab="announce-manage" sideLab="announceMan" title="添加信息">

<!-- content area begin -->
<div class="col-md-10">
    <div class="row">
        <form class="form-horizontal article-form" action="/announce-manage/article/create" method="post">
            <div class="form-group">
                <label class="col-sm-1 control-label">标题: </label>
                <input type="hidden" name="articleId" class="articleId" value="<#if dto??>${dto.id!}</#if>">
                <div class="col-sm-4">
                    <input type="text" name="title"  class="form-control article-title" value="<#if dto??>${dto.title!}</#if>" placeholder="" datatype="*" errormsg="标题不能为空">
                </div>
                <div class="col-sm-4">
                    最多17个中文字符
                </div>
            </div>

            <input type="hidden" class="jq-id" >
            <div class="form-group">
                <label class="col-sm-1 control-label">作者: </label>

                <div class="col-sm-4">
                    <input type="text" name="author" value="<#if dto??>${dto.author!}</#if>" class="form-control article-author" placeholder="" datatype="*" errormsg="作者不能为空">
                </div>
                <div class="col-sm-5">
                    <input type="checkbox" name="carousel"<#if dto??&&dto.carousel>checked</#if>>轮播展示
                </div>

            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">缩略图: </label>
                <div class="col-sm-4 ">
                    <input type="text" name="thumbPicture" value="<#if dto??>${dto.thumbPicture!}</#if>" readonly class="form-control article-thumbPicture" placeholder="" datatype="*" errormsg="缩略图不能为空">
                    <div class="thumbPictureImage">
                        <#if dto??&&dto.thumbPicture??>
                            <img style="width:100%" src="/upload/${dto.thumbPicture!}" alt="缩略图"/>
                        </#if>
                    </div>
                </div>
                <div class="col-sm-4 thumbPicture">
                    <input type="file" name="thumbPictureUrl"/>
                </div>


            </div>

            <div class="form-group">
                <label class="col-sm-1 control-label">展示图: </label>
                <div class="col-sm-4">
                    <input type="text" name="showPicture" value="<#if dto??>${dto.showPicture!}</#if>" readonly class="form-control article-showPicture" placeholder="" datatype="*" errormsg="展示图不能为空">
                    <div class="showPictureImage">
                        <#if dto??&&dto.showPicture??>
                            <img style="width:100%" src="/upload/${dto.showPicture!}" alt="展示图"/>
                        </#if>
                    </div>
                </div>
                <div class="col-sm-4 showPicture">
                    <input type="file" name="showPictureUrl"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">文章来源: </label>

                <div class="col-sm-4">
                    <input type="text" name="source" value="<#if dto??>${dto.source!}</#if>" class="form-control article-source" placeholder="" datatype="*" errormsg="文章来源不能为空">
                </div>
            </div>
            <div class="form-group">
                <label  class="col-sm-1 control-label">栏目: </label>
                <div class="col-sm-2">
                    <select class="selectpicker" name="section" >
                        <#list sectionList as sectionItem>
                            <option value="${sectionItem.name()}" <#if dto?? && dto.section?? && sectionItem == dto.section>selected</#if>> ${sectionItem.getArticleSectionTypeName()}</option>
                        </#list>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-1 control-label">编辑内容: </label>

                <div class="col-sm-10">
                    <script id="editor"
                            type="text/plain"><#if dto??>${dto.content!}</#if></script>
                    <input type="hidden" name="content" class="article-content" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label"></label>

                <div class="col-sm-10">
                    <button type="button" class="btn">预览</button>
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
                    <button type="button" class="btn jq-btn-form btn-primary article-save">提交审核</button>
                </div>
            </div>
        </form>
    </div>
</div>
<!-- content area end -->
</@global.main>