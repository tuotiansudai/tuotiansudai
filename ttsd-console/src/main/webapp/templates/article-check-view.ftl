<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="article-check-view.js" headLab="announce-manage" sideLab="announceMan" title="文章预览">

<!-- content area begin -->
<div class="col-md-10" style="width: 600px; height: 800px;">
    <h3 id="title" align="center">${articleContent.getTitle()}</h3>
      
    <div class="content">
             <p>
        <span id="author">作者：${articleContent.getAuthor()}</span>
        <span id="createTime" style="margin-left: 30px">${articleContent.getCreateTime()?date}</span>
    </p>
             <p id="content" data-id=${articleContent.getId()?c}>${articleContent.getContent()}</p>
    </div>
    <div align="center">
        <input type="button" name="reject" style="margin-right: 70px" value="驳回" onclick="javascript:reject()"/>
        <input type="button" name="submit" style="margin-left: 70px" value="审批通过"/>
    </div>
</div>

<!-- content area end -->
</@global.main>