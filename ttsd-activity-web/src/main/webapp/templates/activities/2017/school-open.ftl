<#import "../../macro/global-dev.ftl" as global>

<#assign jsName = 'school_open_2017' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.css"}>
<@global.main pageCss="${css.school_open_2017}" pageJavascript="${js.school_open_2017}" activeNav="" activeLeftNav="" title="开学季活动_活动中心_拓天速贷" keywords="拓天速贷,拓天开学季,现金红包,京东E卡,实物大奖" description="拓天速贷谁是投资尖子生活动,每日登录可获一次免费抽取现金红包机会,活动期间,投资带有早鸟专享标签的项目前3名奖励100元京东E卡,累计投资前18名可获得实物大奖.">

<div class="banner-slide" id="bannerSlide"></div>

<div class="activity-frame-box page-width" id="activityPageFrame">

    <div class="box-column bg-luck-draw" id="luckDrawBox">
        <div class="column-title-col">
            <i class="word-num">01</i>
            <i class="word-title one"></i>
        </div>
        <div class="line-column"></div>
        <div class="reward-info">
            <b>每日登录可获一次免费抽签机会</b>
            <div class="reward-scroll">
                <ul class="scroll-inner user-record">
                    <li>151＊＊＊＊2223抽动了50元红包</li>
                    <li>152＊＊＊＊2223抽动了50元红包</li>
                    <li>153＊＊＊＊2223抽动了50元红包</li>
                </ul>
            </div>
            <div class="my-reward">
                <b>您有<i class="my-number">1</i>次抽签机会</b>
                <span class="my-record-link">抽签记录></span>
            </div>
        </div>

        <div class="luck-draw-box">
            <div class="draw-bucket"></div>
        </div>

        <div class="bardeen-left"></div>
        <div class="bardeen-right"></div>
    </div>

    <div class="box-column">
        <div class="column-title-col">
            <i class="word-num">02</i>
            <i class="word-title two"></i>
        </div>
        <div class="line-column"></div>
        <div class="bardeen-left"></div>
        <div class="bardeen-right"></div>

        <div class="morning-bird-special">
            <b class="info-tip">活动期间，投资带有“早鸟专享”标签的项目</b>
            <div class="special-img"></div>

            <span class="info-tip-reward">
                <em>根据投资先后，前3名投资该项目5万元以上的同学，奖励<i class="reward-name">100元京东E卡</i></em>
                <i class="note">（用户可在电脑端或App端“早鸟专享”项目中的“出借记录”查询获奖情况）</i>
            </span>

            <img src="" class="img-reward">

            <a href="#" class="btn-to-invest"></a>

        </div>
    </div>

    <div class="box-column">
        <div class="column-title-col">
            <i class="word-num">03</i>
            <i class="word-title three"></i>
        </div>
        <div class="line-column"></div>
        <div class="bardeen-left"></div>
        <div class="bardeen-right"></div>

        <p class="hack">活动期间，根据累计投资额进行实时排名，至9月15日24:00清算排名榜单，<br/>
            <i class="key">前18名</i>上榜的投资尖子生，<i class="key">可获实物大奖</i>。</p>

        <div class="winners-box">
            <div class="winner-top"></div>
            <div class="winner-center">
                <div class="top-column clearfix">
                    <span class="fl">我的投资总额：20999000元</span>
                    <span class="fr">我的排名：3</span>
                </div>
                <div class="table-title clearfix">
                    <span>排名</span>
                    <span>用户名</span>
                    <span>投资总额</span>
                </div>
                <div class="table-list-box">
                    <ul class="table-list clearfix">
                        <li>
                            <span>1</span>
                            <span>134＊＊＊＊2345</span>
                            <span>900000</span>
                        </li>
                        <li>
                            <span>1</span>
                            <span>134＊＊＊＊2345</span>
                            <span>900000</span>
                        </li>
                        <li>
                            <span>1</span>
                            <span>134＊＊＊＊2345</span>
                            <span>900000</span>
                        </li>
                        <li>
                            <span>1</span>
                            <span>134＊＊＊＊2345</span>
                            <span>900000</span>
                        </li>
                        <li>
                            <span>1</span>
                            <span>134＊＊＊＊2345</span>
                            <span>900000</span>
                        </li>
                        <li>
                            <span>1</span>
                            <span>134＊＊＊＊2345</span>
                            <span>900000</span>
                        </li>
                    </ul>

                </div>

                <div class="page-number">
                    <i class="icon-left"></i>
                    <span class="page-index">1</span>
                    <i class="icon-right"></i>
                </div>
            </div>
            <div class="winner-bottom"></div>
        </div>

        <div class="reward-section">
            <div class="align-line"></div>
            <div class="section-title"><em>活动奖品</em></div>
        </div>

        <div class="reward-box-list-out clearfix">

            <div class="reward-box-list ">
                <div class="inner two">
                    <i class="icon-two"></i>
                    <span class="re-img"></span>
                </div>
                <span class="re-name">小米电视43英寸</span>
            </div>

            <div class="reward-box-list ">
                <div class="inner one">
                    <i class="icon-one"></i>
                    <span class="re-img"></span>
                </div>
                <span class="re-name">Apple iPad9.7英寸</span>
            </div>

            <div class="reward-box-list ">
                <div class="inner three">
                    <i class="icon-three"></i>
                    <span class="re-img"></span>
                </div>
                <span class="re-name">丹尼尔惠灵顿手表</span>
            </div>
        </div>

            <ul class="reward-other-list clearfix">
                <li>
                    <i>9</i>
                    <span>红米note4x</span>
                </li>
                <li>
                    <i>10</i>
                    <span>红米note4x</span>
                </li>
                <li>
                    <i>11</i>
                    <span>红米note4x</span>
                </li>
                <li>
                    <i>12</i>
                    <span>红米note4x</span>
                </li>
            </ul>
    </div>

    <div class="box-column box-notice">
        <b class="title-notice"></b>
        1.当日所获抽签机会，仅限当日使用，如当日未使用，则机会失效; <br/>
        2.如早鸟专项标投资5万元以上的同学不足3名，则不足部分的奖励不予发放;<br/>
        3.本活动仅限直投项目，债权转让及新手专享项目不参与累计；<br/>
        4.用户在本活动中所获所有红包、加息券及体验金奖励即时发放，可在PC端“我的账户”或App端“我的”中进行查看；<br/>
        5.实物奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；<br/>
        6.为保证活动的公平性，获奖用户在活动期间内所投的所有项目不允许进行债权转让，如进行转让，则取消获奖资格；<br/>
        7.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；<br/>
        8.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。<br/>
    </div>


    <div class="tip-list-frame">
        <!-- 真实奖品的提示 -->
        <div class="tip-list" data-return="concrete">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="reward-text">恭喜您抽中了<em class="prizeValue"></em>！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close"></a></div>
        </div>

        <!--虚拟奖品的提示-->
        <div class="tip-list" data-return="virtual">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="reward-text">恭喜您抽中了<em class="prizeValue"></em></p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close"></a></div>
        </div>

        <!--没有抽奖机会-->
        <div class="tip-list" data-return="nochance">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您暂无抽奖机会啦～</p>
                <p class="des-text">明天再来吧~</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="no-reward go-close"></a></div>
        </div>

        <!--不在活动时间范围内-->
        <div class="tip-list" data-return="expired">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">不在活动时间内~</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="no-reward go-close"></a></div>
        </div>

    </div>
    <div class="layer-reward-list" style="display: none">
        <div class="own-record-title">
            <span>日期时间</span>
            <span>奖品名称</span>
        </div>
        <div class="own-record-box">
            <ul class="own-record">
                <li>
                    <span>2017-09-13 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
                <li>
                    <span>2017-09-13 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
                <li>
                    <span>2017-09-13 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
                <li>
                    <span>2017-09-13 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
                <li>
                    <span>2017-09-13 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
                <li>
                    <span>2017-09-13 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
                <li>
                    <span>2017-09-13 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
                <li>
                    <span>2017-09-13 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
                <li>
                    <span>2017-09-13 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
                <li>
                    <span>2017-09-15 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
                <li>
                    <span>2017-09-15 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
                <li>
                    <span>2017-09-15 15:30:23</span>
                    <span>2.9元红包</span>
                </li>
            </ul>
        </div>
    </div>

</div>

</@global.main>