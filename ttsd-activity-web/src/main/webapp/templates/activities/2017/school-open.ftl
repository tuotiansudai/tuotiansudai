<#import "../../macro/global.ftl" as global>

<@global.main pageCss="${css.school_open_2017}" pageJavascript="${js.school_open_2017}" activeNav="" activeLeftNav="" title="开学季活动_活动中心_拓天速贷" keywords="拓天速贷,拓天开学季,投资红包,京东E卡,实物大奖" description="拓天速贷谁是投资尖子生活动,每日登录可获一次免费抽取投资红包机会,活动期间,投资带有早鸟专享标签的项目前3名奖励100元京东E卡,累计投资前18名可获得实物大奖.">

<div class="banner-slide compliance-banner" id="bannerSlide">
    <div class="invest-tip tip-width">市场有风险，投资需谨慎！</div>
</div>

<div class="activity-frame-box page-width" id="activityPageFrame">

    <div class="box-column bg-luck-draw" id="luckDrawBox">
        <div class="column-title-col">
            <i class="word-num">01</i>
            <i class="word-title one"></i>
        </div>
        <div class="line-column"></div>
        <div class="reward-info">
            <b class="main-title">每日登录可获一次免费抽签机会</b>
            <span class="sub-title">当日所获抽签机会，仅限当日使用，<br>如当日未使用，则机会失效</span>
            <div class="reward-scroll">
                <ul class="scroll-inner user-record">
                </ul>
            </div>

            <div class="my-reward">
            <@global.isAnonymous>
               <em class="login-box">
                   <span class="to-login"></span>
                   后查看信息
               </em>

            </@global.isAnonymous>
            <@global.isNotAnonymous>

                    <b>您有<i class="my-number">${drawCount}</i>次抽签机会</b>
                    <span class="my-record-link">抽签记录></span>

            </@global.isNotAnonymous>
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
            <div class="img-reward"></div>
            <a href="/loan-list" class="btn-to-invest"></a>

        </div>
    </div>

    <div class="box-column" id="RankingBox">
        <div class="column-title-col">
            <i class="word-num">03</i>
            <i class="word-title three"></i>
        </div>
        <div class="line-column"></div>
        <div class="bardeen-left"></div>
        <div class="bardeen-right"></div>

        <p class="hack">活动期间，根据累计投资额进行实时排名，至9月24日24:00清算排名榜单，<br/>
            <i class="key">前18名</i>上榜的投资尖子生，<i class="key">可获实物大奖</i>。</p>

        <div class="winners-box">
            <div class="winner-top"></div>
            <div class="winner-center">
                <div class="top-column clearfix">
                    <span class="fl">我的投资总额：
                        <@global.isAnonymous><em class="to-text-login"> 登录后查看</em></@global.isAnonymous>
                        <@global.isNotAnonymous>${(investAmount/100)?string("0.00")}元</@global.isNotAnonymous>
                    </span>
                    <span class="fr">我的排名：
                        <@global.isAnonymous><em class="to-text-login"> 登录后查看 </em></@global.isAnonymous>
                        <@global.isNotAnonymous><#if investRanking gt 18 >未上榜<#else >${investRanking}</#if></@global.isNotAnonymous>
                    </span>
                </div>
                <div class="table-title clearfix">
                    <span>排名</span>
                    <span>用户名</span>
                    <span>投资总额</span>
                </div>
                <div class="table-list-box">
                    <ul class="table-list clearfix">
                        <#if (rankList?size > 0)>
                            <#list rankList as rank>
                                <li>
                                    <span>${rank_index+1}</span>
                                    <span>${rank.loginName}</span>
                                    <span>${(rank.sumAmount/100)?string("0.00")}</span>
                                </li>
                            </#list>
                        </#if>
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
                <span class="re-name">盖尔斯（GUESS）手表</span>
            </div>
        </div>
            <div class="reward-other-list-out" >
                <ul class="reward-other-list clearfix" id="rewardOtherBox">
                    <li>
                        <i>4</i>
                        <em class="img-reward-in re04"></em>
                        <span>红米Note4X</span>
                    </li>
                    <li>
                        <i>5</i>
                        <em class="img-reward-in re05"></em>
                        <span>尼康数码相机</span>
                    </li>
                    <li>
                        <i>6</i>
                        <em class="img-reward-in re06"></em>
                        <span>西部数据移动硬盘1TB</span>
                    </li>
                    <li>
                        <i>7</i>
                        <em class="img-reward-in re07"></em>
                        <span>诺泰眼部按摩仪</span>
                    </li>
                    <li>
                        <i>8</i>
                        <em class="img-reward-in re08"></em>
                        <span>爱华仕旅行箱24英寸</span>
                    </li>
                    <li>
                        <i>9</i>
                        <em class="img-reward-in re09"></em>
                        <span>华为荣耀手环</span>
                    </li>
                    <li>
                        <i>10</i>
                        <em class="img-reward-in re10"></em>
                        <span>美的1.5L蒸汽挂烫机</span>
                    </li>
                    <li>
                        <i>11</i>
                        <em class="img-reward-in re11"></em>
                        <span>NIKE训练背包</span>
                    </li>
                    <li>
                        <i>12</i>
                        <em class="img-reward-in re12"></em>
                        <span>也酷车载冰箱10L</span>
                    </li>
                    <li>
                        <i>13</i>
                        <em class="img-reward-in re13"></em>
                        <span>漫步者多媒体音箱</span>
                    </li>
                    <li>
                        <i>14</i>
                        <em class="img-reward-in re14"></em>
                        <span>乐视EB20无线蓝牙耳机</span>
                    </li>
                    <li>
                        <i>15</i>
                        <em class="img-reward-in re15"></em>
                        <span>好视力LED台灯</span>
                    </li>
                    <li>
                        <i>16</i>
                        <em class="img-reward-in re16"></em>
                        <span>电弧点烟器</span>
                    </li>
                    <li>
                        <i>17</i>
                        <em class="img-reward-in re17"></em>
                        <span>颈肩捶打按摩器</span>
                    </li>
                    <li>
                        <i>18</i>
                        <em class="img-reward-in re18"></em>
                        <span>乐扣乐扣保温杯</span>
                    </li>
                </ul>
            </div>

    </div>

    <div class="box-column box-notice">
        <b class="title-notice"></b>

        1.如早鸟专项标投资5万元以上的同学不足3名，则不足部分的奖励不予发放;<br/>
        2.本活动仅限直投项目，债权转让及新手专享项目不参与累计；<br/>
        3.用户在本活动中所获所有红包、加息券及体验金奖励即时发放，可在PC端“我的账户”或App端“我的”中进行查看；<br/>
        4.实物奖品颜色、型号可能与图片略有差异，以用户最终收到的实物为准；<br/>
        5.实物奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；<br/>
        6.为保证活动的公平性，获奖用户在活动期间内所投的所有项目不允许进行债权转让，如进行转让，则取消获奖资格；<br/>
        7.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；<br/>
        8.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。<br/>
    </div>

    <div class="tip-list-frame" style="display: none">
        <!-- 真实奖品的提示 -->
        <div class="tip-list" data-return="concrete">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="reward-text">恭喜您抽中了<em class="prizeValue"></em>！</p>
            </div>
            <div class="btn-list"><a href="/loan-list" class="go-on go-close"></a></div>
        </div>

        <!--虚拟奖品的提示-->
        <div class="tip-list" data-return="virtual">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="reward-text">恭喜您抽中了<em class="prizeValue"></em></p>
            </div>
            <div class="btn-list"><a href="/loan-list" class="go-on go-close"></a></div>
        </div>

        <!--虚拟奖品的提示-->
        <div class="tip-list" data-return="experience">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="reward-text">恭喜您抽中了<em class="prizeValue"></em></p>
            </div>
            <div class="btn-list"><a href="/loan-list" class="go-on go-close"></a></div>
        </div>

        <!--没有抽奖机会-->
        <div class="tip-list" data-return="nochance">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您暂无抽奖机会啦～</p>
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
            <span>奖品名称</span>
            <span>日期时间</span>
        </div>
        <div class="own-record-box">
            <ul class="own-record"></ul>
        </div>
    </div>

</div>
    <#include "../../module/login-tip.ftl" />
</@global.main>