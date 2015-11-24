<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="" activeLeftNav="">
<style type="text/css">
    .full-screen {
        width:100%!important;
        margin:0;
    }
    .footer-container {
        margin-top:0;
    }
</style>
<div class="new-guide-container bg-w">
    <div class="ad-guide-img"></div>
    <div class="content-info page-width">
        <h3 class="tc">什么是拓天速贷？</h3>

        <p>拓天速贷是基于互联网的金融信息服务平台，由拓天伟业（北京）资产管理有限公司旗下的拓天伟业（北京）金融信息服务有限公司运营。拓天速贷以透明、公平、高效为原则，为有资金需求的小微企业及有出借需求的个人提供规范、安全、专业的信息服务。拓天速贷秉承用心服务，诚信经营的经营理念，坚持以人为本，日事日毕，正道经营，共存共赢的管理理念，以安全、诚信、专业、创新、长远为核心价值观，坚持兴企安国的公司使命，拓天速贷将成为国内一流的互联网金融信息服务平台。</p>
    </div>

    <div class="contentA tc">
        <img src="${staticServer}/images/sign/aboutus/invest-process.png" alt="拓天速贷投资流程">
    </div>

    <div class="page-width">
        <div class="our-advantage-content">
            <h3 class="tc">我们的优势</h3>
            <ul class="advantage-list">
                <li>
                    <img src="${staticServer}/images/icons/about/help-n01.png" alt="投资优势" />
                    <b>50元起投，项目期限1月起，年化收益率最高14%。</b>
                </li>
                <li>
                    <img src="${staticServer}/images/icons/about/help-n02.png" alt="投资优势" />
                    <b>投资人的资金是在第三方资金托管平台联动优势托管。</b>
                </li>
                <li>
                    <img src="${staticServer}/images/icons/about/help-n03.png" alt="投资优势" />
                    <b>专业风控团队层层把关，完善的风险保障制度。</b>
                </li>
            </ul>

            <ul class="invest-category">
                <li>

                    <h3>
                        <img src="${staticServer}/images/icons/about/icon-s01.png" class="fl">
                        <span>普通投资<br/>
                        <b>活期理财，存取随心</b></span>
                    </h3>
                    <p>普通投资年化收益9%起，远超余额宝、理财通。提现资金最快当天到账。</p>
                </li>
                <li>

                    <h3><img src="${staticServer}/images/icons/about/icon-s02.png" class="fl">
                        <span>加息投资<br/>
                        <b>自主理财，趣味灵活</b></span>
                    </h3>
                    <p>加息投资多种期限可供选择，年化收益最多14%，远超银行理财。</p>
                </li>
                <li>

                    <h3>
                        <img src="${staticServer}/images/icons/about/icon-s03.png" class="fl">
                        <span>定向投资<br/>
                        <b>定期理财，多种期限</b></span>
                    </h3>
                    <p>定向投资多种期限可供选择，但投资的时候需要投资定向投资密码
                        对投资更有保障。</p>
                </li>
            </ul>

            <ul class="invest-example clear-blank-m">
                <li>
                    <img src="${staticServer}/images/sign/aboutus/example01.png"/>
                </li>
                <li>
                    <img src="${staticServer}/images/sign/aboutus/example02.png"/>
                </li>
                <li>
                    <img src="${staticServer}/images/sign/aboutus/example03.png"/>
                </li>
            </ul>

            <div class="register-flow-box clear-blank-m" id="registerFlowStep">
                <ul class="step-register-tab clearfix">
                    <li class="first on"><s></s>1 注册<g></g></li>
                    <li class="second"><s></s>2 实名验证<g></g></li>
                    <li class="last"><s></s>3 充值投资<g></g></li>
                </ul>
                <div class="register-slide-out">
                    <img src="${staticServer}/images/icons/about/icon-arr-01.png" class="fl last">
                    <img src="${staticServer}/images/icons/about/icon-arr-02.png" class="fr next">
                    <ul class="slide-img-box">
                        <li style="display: block;"><img src="${staticServer}/images/sign/aboutus/process-w01.png"></li>
                        <li><img src="${staticServer}/images/sign/aboutus/process-w02.png"></li>
                        <li><img src="${staticServer}/images/sign/aboutus/process-w03.png"></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

</div>


</@global.main>