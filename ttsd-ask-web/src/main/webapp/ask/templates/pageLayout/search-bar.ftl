<div class="borderBox tc">
    <div class="pc-search-box clearfix" id="searchBoxTool">
        <form method="get" action="/question/search" class="clearfix">
            <input type="text" class="input-search fl" name="keyword">
            <button type="submit" class="btn btn-search fl">搜索</button>
            <a href="/question" class="btn want-question fl">我要提问</a>
        </form>
    <#if (keywordQuestions.data.count)??>
        <div class="search-result clearfix tl">为您搜索到“<em>${keywordQuestions.data.count}</em>”个相关问题</div></#if>
    </div>
    <div class="mobile-menu">
        <a href="/question" class="btn-main want-question">我要提问</a>
        <a href="/question/my-questions" class="btn-main my-question">我的提问</a>
        <a href="/answer/my-answers" class="btn-main my-answer">我的回答</a>
    </div>

</div>