<#import "../../macro/global-dev.ftl" as global>

<#--定义jsName，这里test_demo为ftl 文件引用得 jsx文件名,css和js同名,如果需要模拟 只需要修改这个名字就行-->

<#assign jsName = 'give_iphonex_2017' >

<#assign js = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/activity/js/${jsName}.css"}>
<@global.main pageCss="${css.give_iphonex_2017}" pageJavascript="${js.give_iphonex_2017}" activeNav="" activeLeftNav="" title="不花钱拿走iPhoneX" keywords="拓天速贷,活动中心,iPhoneX,抽奖,现金红包" description="拓天周年庆-英雄排位场活动,每天24点计算当日新增投资排名,上榜者可获得实物大奖及加息券奖励,奖励丰厚礼物多多.">


<#--<@global.main pageCss="${css.jsName}" pageJavascript="${js.jsName}" activeLeftNav="" title="登录拓天速贷_拓天速贷" keywords="拓天速贷,拓天会员,新手理财,拓天速贷用户" description="拓天速贷为投资理财人士提供规范、安全、专业的互联网金融信息服务,让您获得稳定收益和高收益的投资理财产品.">-->
<div class="phone-banner">
</div>
<div class="activity-wrap">
    <div class="activity-page-frame page-width1200" id="activityPageFrame">
        <div class="lottery-wrap">
            <div class="iphone-fonts clearfix">
            </div>
            <div class="lottery-content clearfix page-width">
                <div class="fl">
                    <div class="lottery-circle">
                        <div class="rotater"></div>
                    </div>
                    <a class="draw-btn redBtn">立即抽奖</a>
                </div>


                <div class="fr">
                    <div class="rules">
                        <div class="rules-con">
                            <div class="pos"></div>
                            <h4>抽奖规则</h4>
                            <p>活动期间，单笔投资额每满 1 万元可获得一次抽奖机会，如一次性投资 5 万元可获得 5 次抽奖机会，以此类推，机会多投多得，上不封顶。</p>
                        </div>
                    </div>
                    <div class="recodes">
                        <div class="recodes-con">
                            <div class="pos"></div>
                            <h4>中奖名单</h4>
                            <ul>
                                <li>恭喜   185****9765    抽中88元体验金</li>
                                <li>恭喜   185****9765    抽中888元体验金</li>
                                <li>恭喜   185****9765    抽中588元红包</li>
                                <li>恭喜   185****9765    抽中iPhoneX</li>
                            </ul>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="invest-wrap page-width1200">
           <div class="page-width">
               <div class="invest-fonts"></div>
               <div class="total-annual-wrap">
                   <div class="total-annual">
                       <div class="pos"></div>
                       <div class="instruction">
                           <p> “ 活动期间，计算累计年化投资额，累计年化投资额≥100万元的用户，即可免费获赠iPhoneX （64GB）；累计年化投资额不满100万元的用户，根据累计年化投资额最高奖励1888元现金，真金白银助您买iPhoneX! ”<strong>(注：现金奖励不可累计获得)</strong></p>
                       </div>
                       <a class="computational" id="computational">计算公式</a>
                       <div class="formula">
                           <div class="fl">
                               计算公式
                           </div>
                           <div class="fr">
                               <h4>年化投资额计算公式</h4>
                               <table>
                                   <tr><td>60天～90天项目</td><td>年化投资额＝实际投资额/4</td></tr>
                                   <tr><td>120天～180天项目</td><td>年化投资额＝实际投资额/2</td></tr>
                                   <tr><td>330天～360天项目</td><td>年化投资额＝实际投资额</td></tr>
                               </table>
                           </div>
                       </div>

                   </div>

               </div>
               <div class="chart">
                   fdfdf
               </div>
               <div class="invest-on redBtn">立即投资</div>
               <div class="exemple_wrap">
                   <div class="exemple">
                       <h4 class="font-title">举个栗子</h4>
                       <p class="font-content">拓小天在活动期间投资3个月项目20万元，6个月项目10万元，12个月项目90万元，则拓小天在活动期间年化投资额=200000/4+100000/2+900000=1000000，可免费获赠iPhoneX奖励。</p>
                   </div>
               </div>
           </div>
            <p></p> <p></p> <p></p> <p></p>
        </div>
        <div class="kindly-reminder">
            <h4>温馨提示</h4>
            <p>
                1、本活动仅限直投项目，债权转让不参与累计；<br/>
                2、在活动二中，iphone X与现金奖励不可同时获得；
                3、抽奖活动中所获的红包、加息券、体验金奖励将即时发放，用户可在PC端“我的账户”或App端“我的”中进行查看；<br/>
                4、现金奖励将于活动结束后三个工作日内发放至用户账户；<br/>
                5、iPhoneX奖励发放时间将以国内实际预购及发售情况为准，如出现断货情况，请您耐心等待，拓天速贷客服将于活动结束后7个工作日内与获奖用户取得联系，请保持手机畅通，若在7个工作日内无法联系，将视为自动放弃奖励；<br/>
                6、为保证获奖结果的公平性，获奖用户在活动期间所进行的所有投标，不允许进行债权转让，如有转让，拓天速贷将取消其获奖资格；<br/>
                7、活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；<br/>
                8、活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。<br/>
            </p>
        </div>
    </div>
</div>

</@global.main>

