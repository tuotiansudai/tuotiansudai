<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.full_screen}" pageJavascript="${js.newguide}" activeNav="新手指引" activeLeftNav="" title="新手指引_拓天新手理财_拓天速贷" keywords="拓天速贷,新手理财,新手投资,小额理财,拓天速贷888红包" description="拓天速贷是中国P2P金融信息服务平台,为广大理财、投资、贷款的用户,提供多元化的投资选择和优质的综合理财服务,拓天速贷投资理财的用户可通过新手专区领取现金888元大奖,进行散标投资获得稳定收益.">
<style type="text/css">
    .header-container {
        position: absolute;
        top: 0;
        width: 100%;
        z-index: 102;
        display: block;
    }
    .nav-container {
        position: absolute;
        top: 30px;
        z-index: 100;
        display: block;
    }
    .footer-container {
        display: none;
    }
    #fp-nav{
        display: none;
    }
    @media screen and (max-width: 700px) {
        .header-container,.nav-container {
            display: none;
        }
    }

</style>
<a href="/" class="back-home">返回首页</a>
<div class="new-guide-container" id="assuranceEffect">
    <div class="section intro-text">
        <div class="page-width clearfix">
            <div class="flex-model">
                <p class="intro-title">我们是谁？</p>
                <p class="intro-english">Who Are We?</p>
                <p class="text-name">拓天速贷——互联网金融信息服务平台</p>
                <p class="intro-detail">以创新、诚信、专业作为核心价值观，坚持安全，公平，透明的原则，</br>
                    在为个人和企业提供急需的资金的同时，响应普惠金融的号召，</br>
                    为普通老百姓提供崭新的理财渠道选择。
                </p>
                <p class="intro-restext">以创新、诚信、专业作为核心价值观，坚持安全，公平，透明的原则，在为个人和企业提供急需的资金的同时，响应普惠金融的号召，为普通老百姓提供崭新的理财渠道选择。
                </p>
            </div>
            <div class="down clearfix"><b>试试鼠标滚动</b><img src="${staticServer}/images/sign/assure/down.png" class="down-icon" alt=""></div>
        </div>
    </div>
    <div class="section">
        <div class="page-width clearfix">
            <div class="mode-operation">
                <p class="mode-title">我们的<span>运作模式</span></p>
                <p class="title-word">Our Way Of Operation</p>
                <p class="pc-img"><img src="${staticServer}/images/sign/model-type.png"  width="55%" alt=""></p>
                <p class="responsive-img"><img src="${staticServer}/images/sign/res-model.png"  width="80%" alt=""></p>
            </div>
            <div class="down clearfix"><b class="text-gray">试试鼠标滚动</b><img src="${staticServer}/images/sign/assure/down-gray.png" class="down-icon" alt=""></div>
        </div>
    </div>
    <div class="section">
        <div class="page-width clearfix">
            <div class="mode-operation">
                <p class="mode-title">为什么选择<span>拓天速贷</span></p>
                <p class="title-word">Why Should You Select Us</p>
                <p class="pc-img"><img src="${staticServer}/images/sign/why-img.png"  width="80%" alt=""></p>
                <p class="responsive-img"><img src="${staticServer}/images/sign/res-kind.png"  width="70%" alt=""></p>
            </div>
            <div class="down clearfix"><b class="text-gray">试试鼠标滚动</b><img src="${staticServer}/images/sign/assure/down-gray.png" class="down-icon" alt=""></div>
        </div>
    </div>
    <div class="section">
        <div class="page-width clearfix">
            <div class="mode-operation">
                <p class="mode-title">拓天速贷<span>投资示例</span></p>
                <p class="title-word">An Example Of Investing</p>
                <p class="pc-img"><img src="${staticServer}/images/sign/choose-img.png"  width="40%" alt=""></p>
                <p class="responsive-img"><img src="${staticServer}/images/sign/res-case.png"  width="80%" alt=""></p>
            </div>
            <div class="down clearfix"><b class="text-gray">试试鼠标滚动</b><img src="${staticServer}/images/sign/assure/down-gray.png" class="down-icon" alt=""></div>
        </div>
    </div>
    <div class="section">
        <div class="page-width clearfix">
            <div class="mode-operation">
                <p class="mode-title">多种<span>投资方式</span>供您选择</p>
                <p class="title-word">Abundant Choices For Your Investment Needs</p>
                <p class="pc-img"><img src="${staticServer}/images/sign/tou-img.png"  width="60%" alt=""></p>
                <p class="responsive-img"><img src="${staticServer}/images/sign/res-type.png"  width="70%" alt=""></p>
            </div>
            <div class="down clearfix"><b class="text-gray">试试鼠标滚动</b><img src="${staticServer}/images/sign/assure/down-gray.png" class="down-icon" alt=""></div>
        </div>
    </div>
    <div class="section">
        <div class="page-width clearfix">
            <div class="mode-operation">
                <p class="mode-title">轻松四步<span>坐享收益</span></p>
                <p class="title-word">Enjoy Your Profit In Four Simple Steps</p>
                <p class="pc-img"><img src="${staticServer}/images/sign/liu-img.png"  width="65%" alt=""></p>
                <p class="responsive-img"><img src="${staticServer}/images/sign/res-register.png"  width="60%" alt=""></p>
                <p class="user-register">
                    <@global.isAnonymous>
                        <a href="/register/user">立即注册</a>
                    </@global.isAnonymous>

                    <@global.isNotAnonymous>
                        <a href="/loan-list">立即投资</a>
                    </@global.isNotAnonymous>
                </p>
            </div>
            <div class="down clearfix"><b class="text-gray">试试鼠标滚动</b><img src="${staticServer}/images/sign/assure/up-gray.png" class="down-icon" alt=""></div>
        </div>
    </div>
</div>
</@global.main>