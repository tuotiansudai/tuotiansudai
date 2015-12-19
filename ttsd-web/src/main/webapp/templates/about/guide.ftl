<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.full_screen}" pageJavascript="${js.about_us}" activeNav="" activeLeftNav="">

<div class="new-guide-container bg-w">
    <div class="new-guide-img"></div>
    <div class="content-info page-width">
        <h3 class="tc">什么是拓天速贷？</h3>

        <p>拓天速贷是一家以P2P形式为依托，面向个人投资人的理财融资平台。平台主打优质理财，低门槛提供稳健型理财产品。所有投资产品均被融资担保机构实地调查认证。平台由拓天伟业（北京）资产管理有限公司旗下的拓天伟业（北京）金融信息服务有限公司运营。拓天速贷主要是利用了现有互联网信息技术，将有融资需求的借款人与有富余理财资金的投资人进行在线信息配对。帮助投资人寻找到低门槛、低风险、高收益的理财产品。拓天速贷秉承用心服务，诚信经营的经营理念，坚持以人为本，日事日毕，正道经营，共存共赢的管理理念，以安全、诚信、专业、创新、长远为核心价值观，坚持兴企安国的公司使命，拓天速贷将成为国内一流的线上金融服务平台。</p>
    </div>

    <div class="content-a tc">
        <img src="${staticServer}/images/sign/aboutus/invest-process.png" alt="拓天速贷投资流程">
    </div>

    <div class="page-width">
        <div class="our-advantage-content">
            <h3 class="tc hide-dom">我们的优势</h3>
            <ul class="advantage-list hide-dom">
                <li>
                    <img src="${staticServer}/images/icons/about/help-n01.png" alt="投资优势" />
                    <b>1元起投，项目期限1月起，年化收益率最高14%。</b>
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

            <ul class="invest-category hide-dom">
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

            <ul class="invest-example clear-blank-m hide-dom">
                <li>
                    <img class="responsive-width" src="${staticServer}/images/sign/aboutus/example01.png"/>
                </li>
                <li>
                    <img class="responsive-width" src="${staticServer}/images/sign/aboutus/example02.png"/>
                </li>
                <li>
                    <img class="responsive-width" src="${staticServer}/images/sign/aboutus/example03.png"/>
                </li>
            </ul>

            <div class="register-flow-box clear-blank-m hide-dom" id="registerFlowStep">
                <ul class="step-register-tab clearfix">
                    <li class="first on"><s></s>1 注册<g></g></li>
                    <li class="second"><s></s>2 实名验证<g></g></li>
                    <li class="last"><s></s>3 充值投资<g></g></li>
                </ul>
                <div class="register-slide-out">
                    <img src="${staticServer}/images/icons/about/icon-arr-01.png" class="fl last-step">
                    <img src="${staticServer}/images/icons/about/icon-arr-02.png" class="fr next-step">
                    <ul class="slide-img-box">
                        <li style="display: block;"><img src="${staticServer}/images/sign/aboutus/process-w01.png"></li>
                        <li><img src="${staticServer}/images/sign/aboutus/process-w02.png"></li>
                        <li><img src="${staticServer}/images/sign/aboutus/process-w03.png"></li>
                    </ul>
                </div>
            </div>

            <div class="action-invest">

                进入我的账户 > 充值提现，充够您想要的投资金额 <br/>

                <a href="/register/user" class="btn-normal">免费注册</a>

            </div>
        </div>
    </div>

</div>


</@global.main>