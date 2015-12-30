<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.full_screen}" pageJavascript="${js.newguide}" activeNav="新手指引" activeLeftNav="" title="新手指引">
<style type="text/css">
    .header-container {
        position: absolute;
        top: 0;
        width: 100%;
        z-index: 100;
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
<div class="newguide-container" id="assuranceEffect">
    <div class="section intro-text">
        <div class="page-width clearfix">
            <div class="first-model">
                <p class="intro-title">我们是谁？</p>
                <p class="intro-english">Who we are ?</p>
                <p class="text-name">拓天速贷——互联网金融信息服务平台</p>
                <p class="intro-detail">以创新、诚信、专业作为核心价值观，坚持安全，公平，透明的原则，</br>
                    在为个人和企业提供急需的资金的同时，响应普惠金融的号召，</br>
                    为普通老百姓提供崭新的理财渠道选择。
                </p>
            </div>
            <div class="down clearfix"><b>试试鼠标滚动</b><img src="/images/sign/assure/down.png" class="down-icon" alt=""></div>
        </div>
    </div>
    <div class="section">
        <div class="page-width clearfix">
            <p>我们的<span>运作模式</span></br>The mode of operation </p>
            <p><img src="" alt=""></p>
            <div class="down clearfix"><b class="text-gray">试试鼠标滚动</b><img src="/images/sign/assure/down-gray.png" class="down-icon" alt=""></div>
        </div>
    </div>
    <div class="section">
        <div class="page-width clearfix">
            <div class="down clearfix"><b class="text-gray">试试鼠标滚动</b><img src="/images/sign/assure/down-gray.png" class="down-icon" alt=""></div>
         </div>
    </div>
    <div class="section intro-text">
        <div class="page-width clearfix">
            <div class="down clearfix"><b class="text-gray">试试鼠标滚动</b><img src="/images/sign/assure/down-gray.png" class="down-icon" alt=""></div>
        </div>
    </div>
    <div class="section">
        <div class="page-width clearfix">
            <div class="down clearfix"><b class="text-gray">试试鼠标滚动</b><img src="/images/sign/assure/down-gray.png" class="down-icon" alt=""></div>
        </div>
    </div>
    <div class="section">
        <div class="page-width clearfix">
            <div class="down clearfix"><b class="text-gray">试试鼠标滚动</b><img src="/images/sign/assure/up-gray.png" class="down-icon" alt=""></div>
         </div>
    </div>
</div>
</@global.main>