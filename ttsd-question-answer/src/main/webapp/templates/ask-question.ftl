<#import "macro/global-dev.ftl" as global>
<@global.main pageCss="${css.main}" pageJavascript="${js.main}">
<div class="question-container">
    <div class="article-content fl">
        <div class="ask-question-box clearfix">
            <div class="m-title">我要提问</div>
            <div class="ask-question-con">
                <form name="askQuestion" class="form-question">
                    <dl class="ask-question-list">
                        <dt>一句话描述您的问题</dt>
                        <dd class="clearfix">
                            <input placeholder="请 登录 或 注册 后再进行提问" class="ask-con">
                            <span class="words-tip fr">0/30</span>
                        </dd>
                        <dt>问题补充（选填）</dt>
                        <dd class="clearfix">
                            <textarea placeholder="请注意不要随意透漏您的个人信息"></textarea>
                            <span class="words-tip fr">0/10000</span>
                        </dd>
                        <dt>选择问题分类 <i class="fr">最多选择三个分类</i> </dt>
                        <dd class="tag-list">
                            <span class="tag checked">证券</span>
                            <span class="tag">银行</span>
                            <span class="tag">外汇</span>
                            <span class="tag">股票</span>
                            <span class="tag">理财</span>
                            <span class="tag">期货</span>
                            <span class="tag">P2P</span>
                            <span class="tag">信用卡</span>
                            <span class="tag">贷款</span>
                            <span class="tag">信托</span>
                            <span class="tag">基金</span>
                            <span class="tag">众筹</span>
                            <span class="tag">其他</span>
                        </dd>
                        <dd class="dd-captcha">
                            <input type="text" placeholder="请输入验证码" class="captcha input-short" name="captcha">
                            <img src="https://tuotiansudai.com/login/captcha?1468219031989" alt="">
                        </dd>
                        <dd class="tc ask-button">
                            <button class="btn">提问</button>
                        </dd>
                    </dl>
                </form>
            </div>
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