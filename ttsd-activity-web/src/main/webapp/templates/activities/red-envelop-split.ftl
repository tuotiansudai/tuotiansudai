<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.red_envelop_split}" pageJavascript="${js.red_envelop_split}" activeNav="" activeLeftNav="" title="邀请好友送红包" keywords="拓天速贷,新手投资,新手加息券,新手红包" description="一次体验，受益终生，拓天速贷一周年，这是用心的回馈">

<div class="red-envelop-container" id="redEnvelopSplit">

        <div class="register-section-box">
            <div class="invite-box">
                <em class="message-tip">您有20.88元红包待领取！</em>
                <span class="amount"><i>20.88</i>元</span>
                <span class="kind">投资红包</span>
            </div>

            <div class="flow-box invite-friend">
                <span class="reward-msg">
                    <em>邀好友注册，您将有机会领取20.88元投资红包，好友还能领取8.88元红包哦！</em>
                </span>
                <a href="javascript:void(0)" data-url="${referrerUrl}" class="normal-button to-invite-friend" >立即邀请赚红包</a>
            </div>
        </div>

        <div class="red-envelop-detail">
            <div class="envelop-button clearfix">
                <span class="active">活动规则</span>
                <span>我的红包</span>
            </div>
            <div class="envelop-box clearfix">
                <div class="activity-rule clearfix">
                    <span class="square"><i>给好友发送邀请链接</i></span>
                    <span class="arrow">&nbsp;</span>
                    <span class="square middle"><i>好友完成注册领取8.88元红包</i></span>
                    <span class="arrow">&nbsp;</span>
                    <span class="square"><i>邀请成功赚取红包</i></span>
                </div>

                <ul class="activity-msg-list">
                    <li><i>1</i>活动期间（1月23号--2月5号），通过本页面邀请好友注册即可参与活动；</li>
                    <li><i>2</i>本次活动与邀请好友机制奖励共享，好友投资您依然可以获得1%的推荐奖励；</li>
                    <li><i>3</i>好友通过您的分享链接注册即可获得8.88元红包以及新手5888元体验金和588元优惠券；</li>
                    <li><i>4</i>根据您邀请好友注册的人数不同，您可以获得的红包金额如下：<br/>
                        <table class="table-reward">
                            <tr>
                                <th>邀请好友人数</th>
                                <th>可获红包金额</th>
                            </tr>
                            <tr>
                                <td>1名</td>
                                <td>5.88元</td>
                            </tr>

                            <tr>
                                <td>2名</td>
                                <td>7.88元</td>
                            </tr>
                            <tr>
                                <td>3名</td>
                                <td>9.88元</td>
                            </tr>
                            <tr>
                                <td>4名</td>
                                <td>13.88元</td>
                            </tr>
                            <tr>
                                <td>5名</td>
                                <td>15.88元</td>
                            </tr>
                            <tr>
                                <td>5名以上</td>
                                <td>20.88元</td>
                            </tr>
                        </table>
                    </li>
                    <li><i>5</i>活动结束后3个工作日内，红包即发放至“我的”——“优惠券”当中；</li>
                    <li><i>6</i>分享有效期：1月23号--2月5号；</li>
                    <li><i>7</i>活动遵守拓天速贷法律声明，最终解释权归拓天速贷平台所有 。</li>

                </ul>
            </div>

            <div class="envelop-box clearfix">
            <#if loginName?? >
                <div class="unanonymous-box clearfix">
                    <ul class="my-red-envelop clearfix">
                        <li>成功邀请人数<br/>
                            <em>${referrerCount}</em>人
                        </li>
                        <li>赚取红包<br/>
                            <em>${redEnvelopAmount}</em>元
                        </li>
                    </ul>
                    <div class="my-red-list clearfix">
                        <#if referrerList??>
                            <ul>
                                <#list referrerList as referrer>
                                    <li>
                                        <span class="fl">${referrer.mobile}<br>已注册</span>
                                        <span class="fr">${referrer.registerTime?string("yyyy-MM-dd")}</span>
                                    </li>
                                </#list>
                            </ul>
                        <#else>
                            <span class="no-data">
                            您还未成功邀请好友注册，快去邀请好友拿红包吧！
                        </span>
                        </#if>
                    </div>
                </div>
             <#else>
                 <div class="anonymous-box">
                     <span>您还未登录，登录完成后，再来查看吧！</span>
                     <a class="normal-button" href="/login">去登录</a>
                 </div>
            </#if>
            </div>
        </div>
    </div>

</@global.main>