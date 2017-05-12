<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="content-manage" sideLab="articleMan" title="文章预览">

<!-- content area begin -->
<div class="col-md-10" style="width: 600px; height: 800px;">
    <h3 id="title" align="center">${articleContent.getTitle()}</h3>
      
    <div class="content">
             <p>
        <span id="author">作者：${articleContent.getAuthor()}</span>
        <span id="createTime" style="margin-left: 30px">${articleContent.getCreateTime()?date}</span>
        <#if articleContent.getTimingTime()??>
            <span id="timingTime" style="margin-left: 30px">定时发布时间:${articleContent.getTimingTime()?string('yyyy-MM-dd HH:mm')}</span>
        </#if>
    </p>
             <p id="content">${articleContent.getContent()}</p>
    </div>

    <div>
        <p>展示图</p>
        <img src="${commonStaticServer}${articleContent.getShowPicture()}" alt="展示图"/>
    </div>

    <div>
        <p>缩略图</p>
        <img src="${commonStaticServer}${articleContent.getThumbPicture()}" alt="缩略图"/>
    </div>
</div>

<!-- content area end -->
</@global.main>