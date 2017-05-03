<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="article-check-view.js" headLab="content-manage" sideLab="articleMan" title="文章预览">

<!-- content area begin -->
<div class="col-md-10" style="width: 600px; height: 800px;">
    <h3 id="title" align="center">${articleContent.getTitle()}</h3>
      
    <div class="content">
             <p>
        <span id="author">作者：${articleContent.getAuthor()}</span>
        <span id="createTime" style="margin-left: 30px">创建时间:${articleContent.getCreateTime()?date}</span>
        <#if articleContent.getTimingTime()??>
            <span id="timingTime" style="margin-left: 30px">定时发布时间:${articleContent.getTimingTime()?string('yyyy-MM-dd HH:mm')}</span>
        </#if>
    </p>
             <p id="content" data-id=${articleContent.getArticleId()?c}>${articleContent.getContent()}</p>
    </div>

    <div>
        <p>展示图</p>
        <img src="${commonStaticServer}${articleContent.getShowPicture()}" alt="展示图"/>
    </div>

    <div>
        <p>缩略图</p>
        <img src="${commonStaticServer}${articleContent.getThumbPicture()}" alt="缩略图"/>
    </div>

    <div align="center">
        <input type="button" name="reject" style="margin-right: 70px" value="驳回" id="reject"/>
        <input type="button" name="submit" style="margin-left: 70px" value="审批通过" id="checkPass"/>
    </div>
    <form action="" class="form-inline query-build" id="queryArticle">
    <#if title??><input type="text" value="${title}" name="title" style="display: none"/></#if>
    <#if articleSectionType??><input type="text" value="${articleSectionType}" name="articleSectionType" style="display: none"/></#if>
    <#if status??><input type="text" value="${status}" name="status" style="display: none"/></#if>
    <#if index??><input type="text" value="${index}" name="index" style="display: none"/></#if>
    </form>
</div>

<!-- content area end -->
</@global.main>