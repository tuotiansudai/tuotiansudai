<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.give_iphonex_2017}" pageJavascript="${js.give_iphonex_2017}" activeNav="" activeLeftNav="" title="免费送iPhoneX_活动中心_拓天速贷" keywords="拓天速贷,iPhoneX,抽奖,现金奖励" description="拓天速贷不花钱免费送iPhoneX活动,活动期间单笔投资额每满1万元获得一次抽奖机会,累计年化投资额大于100万元可免费获得iPhoneX,不满100万元的用户可获最高1888元现金奖励.
">
<div class="phone-banner compliance-banner">
    <div class="invest-tip tip-width">市场有风险，投资需谨慎！</div>
</div>
<div class="activity-wrap" id="iphonex">
    <div class="activity-page-frame page-width1200" id="activityPageFrame">
        <div class="lottery-wrap">
            <div class="iphone-fonts clearfix">
            </div>
            <div class="lottery-content gift-circle-frame clearfix page-width">
                <div class="rules-wap">
                    <div class="rules-con">
                        <div class="pos"></div>
                        <h4>抽奖规则</h4>
                        <p>活动期间，单笔投资额每满 1 万元可获得一次抽奖机会，如一次性投资 5 万元可获得 5 次抽奖机会，以此类推，机会多投多得，上不封顶。</p>
                    </div>
                </div>
                <div class="fl">
                    <div class="lottery-circle gift-circle-out ">
                        <div class="rotate-btn"></div>
                        <div class="rotater"></div>
                    </div>

                    <@global.isAnonymous><a class="red-invest draw-btn redBtn">立即抽奖</a></@global.isAnonymous>
                    <@global.isNotAnonymous>
                            <a class="red-invest invest-btn redBtn to-invest" style="display: none">立即投资</a>
                            <a class="red-invest draw-btn redBtn" style="display: none">立即抽奖</a>
                     </@global.isNotAnonymous>
                    <div class="chance">您还有 <strong id="draw_chance">${drawCount}</strong> 次抽奖机会</div>

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
                            <div class="record-list">
                                <ul class="user-record">
                                </ul>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="invest-wrap">
           <div class="page-width">
               <div class="invest-fonts"></div>
               <div class="total-annual-wrap commonStyle">
                   <div class="total-annual childStyle">
                       <div class="pos"></div>
                       <div class="instruction common-style-font">
                           <p> “ 活动期间，计算累计年化投资额，累计年化投资额≥100万元的用户，即可免费获赠iPhoneX （64GB）；累计年化投资额不满100万元的用户，根据累计年化投资额最高奖励1888元现金，真金白银助您买iPhoneX! ”<strong>(注：现金奖励不可累计获得)</strong></p>
                       </div>
                      <div class="formula-wrap clearfix">
                          <#--<a class="computational" id="computational">计算公式</a>-->
                          <#--<div class="formula clearfix" id="formualCon" >-->
                              <div class="formula-box">
                                  <h4>年化投资额计算公式</h4>
                                  <div class="bottom-box">
                                      <table>
                                          <tr><td>60天～90天项目</td><td>年化投资额＝实际投资额/4</td></tr>
                                          <tr><td>120天～180天项目</td><td>年化投资额＝实际投资额/2</td></tr>
                                          <tr><td>330天～360天项目</td><td>年化投资额＝实际投资额</td></tr>
                                      </table>
                                  </div>
                              </div>
                              <div class="formula-fonts" id="computational">
                                  计算公式
                                  <div class="formula-box" id="formula_box">
                                      <h4>年化投资额计算公式</h4>
                                      <div class="bottom-box">
                                          <table>
                                              <tr><td>60天～90天项目</td><td>年化投资额＝实际投资额/4</td></tr>
                                              <tr><td>120天～180天项目</td><td>年化投资额＝实际投资额/2</td></tr>
                                              <tr><td>330天～360天项目</td><td>年化投资额＝实际投资额</td></tr>
                                          </table>
                                      </div>
                                  </div>
                              </div>


                          <#--</div>-->
                      </div>

                   </div>

               </div>
               <div class="chart">

               </div>
               <a class="invest-on redBtn to-invest">立即投资</a>
               <div class="exemple-wrap wap-parent">
                   <div class="exemple childStyle">
                       <div class="pos"></div>
                       <h4 class="font-title">举个栗子</h4>
                       <p class="font-content">拓小天在活动期间投资3个月项目20万元，6个月项目10万元，12个月项目90万元，则拓小天在活动期间年化投资额=200000/4+100000/2+900000=1000000，可免费获赠iPhoneX奖励。</p>
                   </div>
               </div>
           </div>

        </div>
        <div class="kindly-reminder-wrap wap-parent">
            <div class="childStyle">
                <div class="pos pos2"></div>
                <h4>温馨提示</h4>
                <p>
                    1、本活动仅限直投项目，债权转让及体验金不参与累计；<br/>
                    2、在活动二中，iPhoneX与现金奖励不可同时获得；<br/>
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
    <#include "../../module/login-tip.ftl" />
    <div class="tip-list-frame">
        <!-- 真实奖品的提示 -->
        <div class="tip-list" data-return="concrete">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text"></p>
                <p class="reward-text"><em class="prizeValue"></em>！</p>
                <p class="des-text">请实名认证后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">确定</a></div>
        </div>

        <!--虚拟奖品的提示-->
        <div class="tip-list" data-return="virtual">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="success-text"></p>
                <p class="reward-text"><em class="prizeValue"></em></p>
                <p class="des-text">请实名认证后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-on go-close">确定</a></div>
        </div>

        <!--没有抽奖机会-->
        <div class="tip-list" data-return="nochance">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您暂无抽奖机会啦～</p>
                <p class="des-text">快去投资赢取抽奖机会吧</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close canble-btn">取消</a><a href="/loan-list" class="go-close">去投资</a></div>
        </div>

        <!--不在活动时间范围内-->
        <div class="tip-list" data-return="expired">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">不在活动时间内~</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">确定</a></div>
        </div>

        <!--实名认证-->
        <div class="tip-list" data-return="authentication">
            <div class="close-btn go-close"></div>
            <div class="text-tip">
                <p class="login-text">您还未实名认证~</p>
                <p class="des-text">请实名认证后再来抽奖吧！</p>
            </div>
            <div class="btn-list"><a href="javascript:void(0)" class="go-close">确定</a></div>
        </div>
    </div>
</div>

</@global.main>

