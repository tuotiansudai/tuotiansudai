<#import "wechat-global.ftl" as global>

<@global.main pageCss="${css.invite_friends_openWare}" pageJavascript="${js.invite_friends_openWare}"  title="拓天速贷3周年庆,红包!返现!分奖池!">

<div class="invite-friend-container" id="openWareContainer" data-countdown='${endTime!}'>
    <div class="invite-banner">
        <div class="risk-tip">市场有风险，投资需谨慎！</div>
    </div>
    <div class="invite-friend-fonts"></div>
    <div class="red-ware">
        <div class="invite-wrap">

            <#if isHelp>
                <a class="cash-btn circle-btn" href="/m/account"></a>
            <#else>
                    <form action="/activity/third-anniversary/open-red-envelope" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input type="hidden" name="originator" value="${originator}">
                        <input type="submit" class="open-btn circle-btn" value=""></input>
                    </form>
            </#if>
            <#if isHelp>
                <p>您已成功拆红包<br/>
                    目前可瓜分现金<strong>${reward}元</strong><br/>
                <#--活动结束后发放至拓天速贷个人账户-->
                    倒计时结束后可提现
                </p>
            <#else>
                <p>您的好友邀请您拆红包<br/>
                    共同瓜分<br/>
                    最高<strong>${reward}元</strong>的现金
                </p>
            </#if>
            <a class="to-join-btn" href="javascript:;"></a>
        </div>
    </div>
    <div class="part part-one marginTop100">
        <a class="circle left-circle" href="javascript:;"></a>
        <a class="circle right-circle" href="javascript:;"></a>
        <div class="part-title"></div>
        <div class="main-content">
            <div class="time">
            </div>
        </div>

    </div>
    <div class="bolang-wrap">
        <div class="part part-two marginTop100" id="getWare">
            <span class="circle left-circle"></span>
            <span class="circle right-circle"></span>
            <div class="part-title"></div>
            <div class="main-content">
                <ul>
                    <ul>
                        <#if helpFriend!?if_exists?size !=0 >
                            <#list helpFriend as friend>
                                <li class="clearfix">
                                    <div class="fl">${friend.mobile?substring(0, 3)}
                                        ****${friend.mobile?substring(7)}</div>
                                    <div class="date-time fr">${friend.createdTime?string('yyyy-MM-dd HH:mm:ss')}</div>
                                </li>
                            </#list>
                        </#if>
                    </ul>
                </ul>
            </div>

        </div>
        <div class="bolang2"></div>
    </div>

    <div class="part part-three marginTop100">
        <span class="circle left-circle"></span>
        <span class="circle right-circle"></span>
        <div class="part-title"></div>
        <div class="main-content">
            <p>1.活动期间，在平台投资过的用户通过微信“拓天速贷服务号”进入活动页面，可分享现金红包给好友，邀请好友拆红包；</p>
            <p>2.拆开红包的好友需未注册过拓天速贷账户，并在活动页面完成注册+实名认证，实名认证后方可拆开红包；</p>
            <p>3.拆红包流程：点击活动页面的“拆红包”按钮-注册拓天速贷账户-完成实名认证-拆红包成功；</p>
            <p>4.投资用户及其好友的现金红包金额，将于分享成功后72小时内发放至双方用户账户，您可以直接提现；</p>
            <p>5.截止发放时间为止，如果拆开红包的好友未登录拓天速贷并进行实名认证，好友将无法收到现金；</p>
            <p>6.本活动仅限直投项目，债权转让、体验项目及新手专享项目不参与累计；</p>
            <p>7.年化投资额计算公式：</p>
            <div class="calculation-formula">
                <table>
                    <tr>
                        <td>60天-90天项目</td>
                        <td class="last-td"><span>年化投资额=</span><span>实际投资额*90/360</span></td>
                    </tr>
                    <tr>
                        <td>120天-180天项目</td>
                        <td class="last-td"><span>年化投资额=</span><span>实际投资额*180/360</span></td>
                    </tr>
                    <tr>
                        <td>330天-360天项目</td>
                        <td class="last-td"><span>年化投资额=</span><span>实际投资额</span></td>
                    </tr>
                </table>

            </div>
            <p>8.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</p>
            <p>9.活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</p>

        </div>

    </div>
    <#include "../module/login-tip.ftl" />
    <div class="wechat-tip" style="display: none">
        <div class="open-ware-tip">
            <form action="/activity/third-anniversary/open-red-envelope" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <input type="hidden" name="originator" value="${originator}">
                <input type="submit" class="open-now open-ware-btn" href="javascript:;" value=""></input>

                <a class="no-open open-ware-btn" href="javascript:;"></a>
            </form>
        </div>

    </div>
    <div class="bolang"></div>
    <div class="bolang bolang2" style="display: none"></div>
</div>
<script>
    var originator = '${originator}';
    webServer = '${webServer}';
    commonStaticServer = '${commonStaticServer}';
</script>
</@global.main>