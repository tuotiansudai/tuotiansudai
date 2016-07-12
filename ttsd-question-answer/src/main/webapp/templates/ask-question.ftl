<#import "macro/global-dev.ftl" as global>
<@global.main pageCss="${css.main}" pageJavascript="${js.main}">
    <div class="borderBox tc">
        <a href="#" class="btn-main">我要提问</a>
    </div>
<div class="question-container">

    <div class="article-content fl">
        <div class="borderBox clearfix">
DDD
        </div>

    </div>
    <div class="aside-frame fr">
        <div class="profile-box">
            <i class="profile"></i>
            <ul class="welcome-info">
                <li class="username">CG007008,您好</li>
                <li>提问有新回答 <br/>
                    <a href="#"> 点击查看</a>
                </li>
            </ul>
            <div class="button-layer">
                <a href="#" class="btn">我的提问<em>(4)</em></a>
                <a href="#" class="btn">我的回答<em>(8)</em></a>
            </div>
            <div class="vertical-line"></div>
        </div>

        <div class="hot-questions margin-top-10 clearfix">
            <div class="qa-title">热门问题分类</div>
            <ul class="qa-list clearfix">
                <li><a href="javascript:void(0);" class="active">证劵</a></li>
                <li><a href="javascript:void(0);" >银行</a></li>
                <li><a href="javascript:void(0);" >期货</a></li>
                <li><a href="javascript:void(0);" >P2P</a></li>
                <li><a href="javascript:void(0);" >信托</a></li>
                <li><a href="javascript:void(0);" >贷款</a></li>
                <li><a href="javascript:void(0);" >基金</a></li>
                <li><a href="javascript:void(0);" >众筹</a></li>
                <li><a href="javascript:void(0);" >理财</a></li>
                <li><a href="javascript:void(0);" >信用卡</a></li>
                <li><a href="javascript:void(0);" >外汇</a></li>
                <li><a href="javascript:void(0);" >股票</a></li>
                <li><a href="javascript:void(0);" >其他</a></li>
            </ul>
        </div>

        <img src="${staticServer}/images/welfare.jpg" alt="新人送福利" class="margin-top-10">

    </div>
</div>

</@global.main>