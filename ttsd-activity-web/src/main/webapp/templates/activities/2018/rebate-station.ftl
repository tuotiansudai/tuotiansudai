<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.rebate_station_2018}" pageJavascript="${js.rebate_station_2018}" activeNav="" activeLeftNav="" title="返利加油站_助力返现_拓天速贷" keywords="拓天速贷,邀请好友,助力返现,现金奖励" description='拓天速贷返利加油站活动,活动期间用户分享投资后专属助力链接至微信群/朋友圈/任意好友,邀请好友助力返现,好友助力后可平分现金奖励,分享活动链接,呼朋唤友向钱冲.'>
<div class="rebate-station-banner">
    <div class="invest_btn"></div>
</div>
<div class="invest_btn_m_container">
    <div class="invest_btn_m"></div>
</div>
<div class="activity-wrap-main">
    <div class="circle"></div>
    <div class="glasses"></div>
    <div class="ship"></div>
    <div class="container container1 pcmSite">
        <div class="star"></div>
        <div class="outBox">
            <div class="middleBox">
                <div class="innerBox">
                    <div class="box part1">
                        <div class="desc">
                            <p>1、活动期间，用户投资后分享该笔投资专属助力链接至微信群/朋友圈/任意好友，好友参加助力后可获得返现；</p>
                            <p>2、根据累计助力好友人数，该投资用户可享受该笔投资额的0.2%-1%（年化）返现奖励。（注：无论好友是否为拓天速贷注册用户，均可参加助力）</p>
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>最少助力人数</th>
                                    <th>2</th>
                                    <th>8</th>
                                    <th>18</th>
                                    <th>58</th>
                                    <th>88</th>
                                    <th>108</th>
                                </tr>
                                <tr>
                                    <th>返现利率（年化）</th>
                                    <th>0.2%</th>
                                    <th>0.5%</th>
                                    <th>0.6%</th>
                                    <th>0.7%</th>
                                    <th>0.8%</th>
                                    <th>1%</th>
                                </tr>
                                </thead>
                            </table>
                            <table class="table_m">
                                <thead>
                                <tr>
                                    <th>最少助力人数</th>
                                    <th>返现利率（年化）</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>2</td>
                                    <td>0.2%</td>
                                </tr>
                                <tr>
                                    <td>8</td>
                                    <td>0.5%</td>
                                </tr>
                                <tr>
                                    <td>18</td>
                                    <td>0.6%</td>
                                </tr>
                                <tr>
                                    <td>58</td>
                                    <td>0.7%</td>
                                </tr>
                                <tr>
                                    <td>88</td>
                                    <td>0.8%</td>
                                </tr>
                                <tr>
                                    <td>108</td>
                                    <td>1%</td>
                                </tr>
                                </tbody>
                            </table>
                            <p>3、活动期间，为投资用户直接助力的好友，可平分与投资用户等额的现金奖励。</p>
                            <p>4、助力有效期：用户投资成功后24小时之内</p>
                            <div class="example_pic"></div>
                            <div class="cashBack">
                                <div class="title">
                                    <div class="cash_icon"></div>
                                    我的返现项目
                                </div>
                                <div class="already_login">
                                    <div class="table_container <#if investHelp!?size ==0>table_none</#if>">
                                        <table class="cashBack_list">
                                            <thead>
                                            <tr class="table_header">
                                                <th>投资金额</th>
                                                <th>年化投资金额</th>
                                                <th>已助力好友（人）</th>
                                                <th>获取现金奖励</th>
                                                <th>好友助力倒计时</th>
                                                <th>邀请好友助力</th>
                                            </tr>
                                            </thead>
                                            <#if investHelp!?size!=0>
                                                <tbody>
                                                    <#list investHelp as invest>
                                                    <tr>
                                                        <td>${(invest.investAmount/100)?string('0.00')}</td>
                                                        <td>${(invest.annualizedAmount/100)?string('0.00')}</td>
                                                        <td>${invest.helpUserCount}</td>
                                                        <td>${(invest.reward/100)?string('0.00')}</td>
                                                        <td class="overTime"
                                                            data-overtime="${invest.endTime?string('yyyy-MM-dd HH:mm:ss')}"></td>
                                                        <td class="handle_btn"
                                                            data-help-id="${invest.id}"
                                                            data-overtime="${invest.endTime?string('yyyy-MM-dd HH:mm:ss')}">
                                                            去邀请
                                                        </td>
                                                    </tr>
                                                    </#list>
                                                </tbody>
                                            </#if>
                                        </table>
                                        <#if investHelp!?size ==0>
                                            <div class="tipText tipText_pc">
                                                投资成功后，即可邀请好友助力获取高额返现
                                            </div>
                                        </#if>
                                        <table class="cashBack_list_m">
                                            <thead>
                                            <tr class="table_header">
                                                <th>投资金额</th>
                                                <th>好友助力倒计时</th>
                                                <th>邀请好友助力</th>
                                            </tr>
                                            </thead>
                                            <#if investHelp!?size!=0>
                                                <tbody>
                                                    <#list investHelp as invest>
                                                    <tr>
                                                        <td>${(invest.investAmount/100)?string('0.00')}</td>
                                                        <td class="overTime"
                                                            data-overtime="${invest.endTime?string('yyyy-MM-dd HH:mm:ss')}"></td>
                                                        <td class="handle_btn"
                                                            data-help-id="${invest.id}"
                                                            data-overtime="${invest.endTime?string('yyyy-MM-dd HH:mm:ss')}">
                                                            去邀请
                                                        </td>
                                                    </tr>
                                                    </#list>
                                                </tbody>
                                            </#if>
                                        </table>
                                        <#if investHelp!?size ==0>
                                            <div class="tipText tipText_mobile">
                                                投资成功后，即可邀请好友助力获取高额返现
                                            </div>
                                        </#if>
                                    </div>
                                    <div class="see_more"><#if investHelp!?size gt 5>查看更多></#if></div>
                                    <div class="see_less" style="display: none">收起></div>
                                </div>
                                <div class="no_login no_login1">
                                    <table class="cashBack_list">
                                        <table class="cashBack_list">
                                            <thead>
                                            <tr class="table_header">
                                                <th>投资金额</th>
                                                <th>年化投资金额</th>
                                                <th>已助力好友（人）</th>
                                                <th>获取现金奖励</th>
                                                <th>好友助力倒计时</th>
                                                <th>好友助力倒计时</th>
                                            </tr>
                                            </thead>
                                        </table>
                                        <div class="tipText tipText_pc">
                                            投资成功后，即可邀请好友助力获取高额返现
                                        </div>
                                    </table>
                                    <table class="cashBack_list_m">
                                        <thead>
                                        <tr class="table_header">
                                            <th>投资金额</th>
                                            <th>好友助力倒计时</th>
                                            <th>邀请好友助力</th>
                                        </tr>
                                        </thead>
                                    </table>
                                    <div class="tipText tipText_mobile">
                                        投资成功后，即可邀请好友助力获取高额返现
                                    </div>
                                </div>
                                <div class="help_list">
                                    <div class="title">
                                        <div class="leftPic"></div>
                                        <div class="rightPic"></div>
                                    </div>
                                    <div class="content">
                                        <#if rewardRecords!?size!=0>
                                            <div class="my_help_list" style="height: 140px;overflow: hidden">
                                                <#list rewardRecords as record>
                                                    <p>用户${record.mobile}，获取${(record.reward/100)?string('0.00')}元现金</p>
                                                </#list>
                                            </div>
                                        <#else>
                                            <div class="no_login2">
                                                还没有用户获得投资好友助力返现
                                            </div>
                                        </#if>
                                    </div>
                                    <div class="state_btn"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="wood topLeft"></div>
        <div class="wood topRight"></div>
        <div class="wood bottomLeft"></div>
        <div class="wood bottomRight"></div>
    </div>
<#--app独有页面begin-->
    <div class="container containerApp" style="display: none">
        <div class="star"></div>
        <div class="outBox">
            <div class="middleBox">
                <div class="innerBox">
                    <div class="box part1">
                        <div class="desc">
                            <p class="desc-activity">活动期间，用户投资后邀请好友助力，根据助力好友人数，该笔投资可获0.2%-1%（年化）返现。</p>
                            <div class="line"></div>
                            <div class="progress">
                                <p><strong>参与流程：</strong><br/>
                                    第一步：用户在拓天速贷上<strong>投资</strong>；<br/>
                                    第二步：扫描二维码或微信搜索关注“<strong>拓天速贷服务号</strong>”，回复口令“<strong>返利加油站</strong>”，进入页面；<br/>
                                    第三步：找到“<strong>邀请好友助力，最高返现1%×2</strong>”活动版块；<br/>
                                    第四步：在“我的返现项目”中，点击【<strong>去邀请</strong>】按钮，按提示步骤操作即可。</p>

                            </div>
                            <img style="width: 100%" class="qrcode"
                                 src="../../../activity/images/add_rates_wechat.png"/>
                            <div class="cashBack">
                                <div class="title">
                                    <div class="cash_icon"></div>
                                    我的返现项目
                                </div>
                                <div class="already_login">
                                    <div class="table_container <#if investHelp!?size ==0>table_none</#if>">

                                        <table class="cashBack_list_m">
                                            <thead>
                                            <tr class="table_header">
                                                <th>投资金额</th>
                                                <th>好友助力倒计时</th>
                                                <th>获取现金奖励</th>
                                            </tr>
                                            </thead>
                                            <#if investHelp!?size!=0>
                                                <tbody>
                                                    <#list investHelp as invest>
                                                    <tr>
                                                        <td>${(invest.investAmount/100)?string('0.00')}</td>
                                                        <td class="overTime"
                                                            data-overtime="${invest.endTime?string('yyyy-MM-dd HH:mm:ss')}"></td>
                                                        <td class="handle_btn">
                                                        ${(invest.reward/100)?string('0.00')}元
                                                        </td>
                                                    </tr>
                                                    </#list>
                                                </tbody>
                                            </#if>
                                        </table>
                                        <#if investHelp!?size ==0>
                                            <div class="tipText tipText_mobile">
                                                投资成功后，即可邀请好友助力获取高额返现
                                            </div>
                                        </#if>
                                    </div>
                                    <div class="see_more"><#if investHelp!?size gt 5>查看更多></#if></div>
                                    <div class="see_less" style="display: none">收起></div>
                                </div>
                                <div class="no_login no_login1">

                                    <table class="cashBack_list_m">
                                        <thead>
                                        <tr class="table_header">
                                            <th>投资金额</th>
                                            <th>好友助力倒计时</th>
                                            <th>获取现金奖励</th>
                                        </tr>
                                        </thead>
                                    </table>
                                    <div class="tipText tipText_mobile">
                                        投资成功后，即可邀请好友助力获取高额返现
                                    </div>
                                </div>
                                <a class="to-loan-btn" href="loan-list"></a>

                            </div>


                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="wood topLeft"></div>
        <div class="wood topRight"></div>
        <div class="wood bottomLeft"></div>
        <div class="wood bottomRight"></div>
    </div>
<#--app页面end-->
    <div class="go_ahead"></div>
    <div class="container pcmSite">
        <div class="outBox">
            <div class="middleBox">
                <div class="innerBox">
                    <div class="box part2">
                        <div class="part2_pc">
                            <div class="title">
                                <p>活动期间，用户登录后分享活动链接至微信群/朋友圈/任意好友，<span class="strong">邀请好友</span>为您助力，每<span
                                        class="strong">多邀请1</span></p>
                                <p><span class="strong">人</span>点击，可获得<span class="strong">0.2元现金</span>奖励，最高可获得<span
                                        class="strong">10元现金</span>奖励。</p>
                            </div>
                            <div>
                                <div class="content">
                                    <div class="person_pic"></div>
                                    <div class="weachat_wrapper">
                                        <div class="active_one"></div>
                                    </div>
                                    <div class="desc">
                                        <div>
                                            <div class="scan_icon"></div>
                                            使用微信扫一扫
                                        </div>
                                        <div>邀请好友助力得现金奖励</div>
                                    </div>
                                    <div class="already_login">
                                        <div class="news">您已成功邀请<#if everyoneHelp??>${everyoneHelp.helpUserCount}<#else>
                                            0</#if>人，获得返现<#if everyoneHelp??>${(everyoneHelp.reward/100)?string('0.00')}<#else>
                                            0.00</#if>元，<span
                                                class="strong everyone_detail invite_everyone_detail"
                                                style="cursor: pointer" data-start-time="${activityStartTime}"
                                                data-over-time="${activityEndTime}" data-own-help="${existOwnHelp?c}">查看邀请详情></span>
                                        </div>
                                    </div>

                                    <div class="no_login">
                                        <div class="login_btn"></div>
                                    </div>
                                    <div class="end_time">活动有效期：05.02-05.31</div>
                                </div>
                            </div>
                        </div>
                        <div class="part2_mobile">
                            <div class="title">
                                <p>活动期间，用户登录后分享活动链接至微信群/朋友圈/任意好友，<span class="strong">邀请好友</span>为您助力，每<span
                                        class="strong">多邀请1</span>
                                    <span class="strong">人</span>点击，可获得<span class="strong">0.2元现金</span>奖励，最高可获得<span
                                            class="strong">10元现金</span>奖励。</p>
                            </div>
                            <div class="already_login">
                                <div class="news">您已成功邀请<span
                                        class="strong"><#if everyoneHelp??>${everyoneHelp.helpUserCount}<#else>0</#if>
                                    人</span>，获得返现<span
                                        class="strong"><#if everyoneHelp??>${(everyoneHelp.reward/100)?string('0.00')}<#else>
                                    0.00</#if>元</span>，
                                </div>
                                <div class="strong everyone_detail invite_everyone_detail" style="cursor: pointer"
                                     data-start-time="${activityStartTime}" data-over-time="${activityEndTime}"
                                     data-own-help="${existOwnHelp?c}">查看邀请详情>
                                </div>
                                <div class="invite_friends_help invite_everyone_detail"></div>
                            </div>
                            <div class="no_login">
                                <div class="login_btn"></div>
                                <div class="login_text">登录后可查看分享链接</div>
                                <div class="duration_time">活动有效期：05.02-05.31</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="wood topLeft"></div>
        <div class="wood topRight"></div>
        <div class="wood bottomLeft"></div>
        <div class="wood bottomRight"></div>
    </div>
    <div class="container containerApp" style="display: none">
        <div class="star"></div>
        <div class="outBox">
            <div class="middleBox">
                <div class="innerBox">
                    <div class="box part1">
                        <div class="desc">
                            <@global.isAnonymous>
                                <div class="login_btn"></div>
                                <p class="no-login-tip">登录后可查看已获取奖励</p>
                            </@global.isAnonymous>
                            <@global.isNotAnonymous>
                                <div class="login-news">您已成功邀请<strong><#if everyoneHelp??>${everyoneHelp.helpUserCount}<#else>
                                    0</#if></strong>人，获得返现<strong><#if everyoneHelp??>${(everyoneHelp.reward/100)?string('0.00')}</strong><#else>
                                    0.00</#if>元
                                </div>
                            </@global.isNotAnonymous>

                            <p class="desc-activity">活动期间，使用微信邀请好友助力，每多邀请1人为您助力，可获得0.2元现金，最高可获10元现金奖励。</p>
                            <div class="line"></div>
                            <div class="progress">
                                <p><strong>参与流程：</strong><br/>
                                    第一步：扫描二维码或微信搜索关注”<strong>拓天速贷服务号</strong>”，回复口令“<strong>返利加油站</strong>”，进入页面；<br/>
                                    第二步：找到“<strong>人人可领10元现金</strong>”活动版块；<br/>
                                    第三步：点击【<strong>邀请好友来助力</strong>】按钮，按提示步骤操作即可。</p>

                            </div>
                            <img style="width: 100%" class="qrcode"
                                 src="../../../activity/images/add_rates_wechat.png"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="wood topLeft"></div>
        <div class="wood topRight"></div>
        <div class="wood bottomLeft"></div>
        <div class="wood bottomRight"></div>
    </div>
    <div class="add_rates"></div>
    <div class="container">
        <div class="outBox">
            <div class="middleBox">
                <div class="innerBox">
                    <div class="box part3">
                        <div class="title">
                            <p>活动期间，微信扫描下方二维码或搜索<span class="strong">“拓天速贷服务号”</span>关注公众号，<span class="strong">回复口令“向钱冲”</span>，
                            </p>
                            <p>即可<span class="strong">获得0.5%加息券2张</span>，每人限领一次。</p>
                        </div>
                        <img class="content" src="../../../activity/images/add_rates_wechat.png"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="wood topLeft"></div>
        <div class="wood topRight"></div>
        <div class="wood bottomLeft"></div>
        <div class="wood bottomRight"></div>
    </div>
    <div class="tips_container">
        <h3 class="tips_title_pc">温馨提示：</h3>
        <h3 class="tips_title_mobile">
            <div class="square_icon"></div>
            <div class="square_icon"></div>
            <div class="square_icon"></div>
            温馨提示
            <div class="square_icon left_spacing"></div>
            <div class="square_icon"></div>
            <div class="square_icon"></div>
        </h3>
        <p>1. 活动期间，老用户每笔投资成功后，均可在活动页面生成一个该笔投资专属的助力链接，老用户需分享此链接方可为该笔投资助力加息；</p>
        <p>2. 每笔投资助力人数累计时间截止至该笔投资后24小时，超过24小时后的好友助力，不再计入该笔投资助力人数；</p>
        <p>3. 每人每日最多可帮助5名好友助力；</p>
        <p>4. 老用户及其好友的投资加息返现奖励将于项目放款后24小时后发放至拓天速贷账户；</p>
        <p>5.截止发放时间为止，如果助力好友未登录拓天速贷并进行实名认证，将无法收到现金；</p>
        <p>6. “人人可领10元现金”活动，每个注册用户于活动期间仅限参与一次，用户第一次分享后24小时内参与助力的有效；</p>
        <p>7. “人人可领10元现金”奖励将于用户分享倒计时结束后24小时统一发放至用户拓天速贷账户；截止发放时间为止，如果未登录拓天速贷并完成实名认证，将无法收到奖励；</p>
        <p>8. 活动中所获加息券即时发放，用户可在PC端“我的账户”或App端“我的-优惠券”中进行查看；</p>
        <p>9. 本活动仅限直投项目，债权转让、体验项目及新手专享项目不参与累计；</p>
        <p>10. 年化投资额计算公式：</p>
        <table>
            <tr>
                <th>60天 - 90天项目</th>
                <th>年化投资额 = 实际投资额*90/360</th>
            </tr>
            <tr>
                <th>120天 - 180天项目</th>
                <th>年化投资额 = 实际投资额*180/360</th>
            </tr>
            <tr>
                <th>330天 - 360天项目</th>
                <th>年化投资额 = 实际投资金额</th>
            </tr>
        </table>
        <p>11. 活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</p>
        <p>12. 活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</p>
    </div>
</div>
<div class="help_popModal">
    <div class="content">
        <div class="join_num">您已成功邀请<span class="strong"><span class="invite_count">0</span></span>人</div>
        <div class="get_cash">获得返现<span class="strong"><span class="invite_reward">0.00</span></span>元</div>
        <div class="duration_time">活动有效期：05.02-05.31</div>
        <div class="invite_weachat">
            <div class="desc_text">使用微信扫一扫<br/>邀请好友助力得现金奖励</div>
        </div>
        <div class="active_one_pop"></div>
        <div class="friends">已助力好友</div>
        <div class="list_tip_text" style="display: none" id="helpPopText">您还没有获得好友助力，快去邀请吧</div>
        <div class="friend_list friend_list_everyone" style="display: none">
        </div>
        <div class="close_btn"></div>
    </div>
</div>
<div class="cashBack_popModal">
    <div class="content">
        <div class="userName"></div>
        <div class="title">
            <div class="left_topIcon"></div>
            <div class="text">
                <div>您已经获得<span class="strong"><span class="has_get"></span>元</span>助力现金奖励</div>
                <div class="differ_friends" style="display: none">还差<span class="differ_count"></span>个好友助力即可得到<span
                        class="strong"><span class="differ_amount"></span>元</span>现金奖励
                </div>
            </div>
            <div class="right_topIcon"></div>
        </div>
        <div class="percent_wrapper">
            <div class="line">
                <div class="light_line"></div>
            </div>
            <div class="circle_wrapper circle1_wrapper">
                <div class="circle circle1"></div>
                <div class="person_num">2人</div>
            </div>
            <div class="circle_wrapper circle2_wrapper">
                <div class="circle circle2"></div>
                <div class="person_num">8人</div>
            </div>
            <div class="circle_wrapper circle3_wrapper">
                <div class="circle circle3"></div>
                <div class="person_num">18人</div>
            </div>
            <div class="circle_wrapper circle4_wrapper">
                <div class="circle circle4"></div>
                <div class="person_num">58人</div>
            </div>
            <div class="circle_wrapper circle5_wrapper">
                <div class="circle circle5"></div>
                <div class="person_num">88人</div>
            </div>
            <div class="circle_wrapper circle6_wrapper">
                <div class="circle circle6"></div>
                <div class="person_num">108人</div>
            </div>
        </div>
        <div class="mid_content">
            <div class="invite_weachat">
                <div class="desc_text">使用微信扫一扫<br/>邀请好友助力得现金奖励</div>
                <div class="active_two"></div>
            </div>
            <div class="countDownTime">
                <div class="title">倒计时</div>
                <div class="countDown_wrapper">
                    <div class="time_num_wrapper">
                        <div class="time_num hour1"></div>
                        <div class="time_num hour2"></div>
                        <span class="icon">:</span>
                        <div class="time_num minutes1"></div>
                        <div class="time_num minutes2"></div>
                        <span class="icon">:</span>
                        <div class="time_num seconds1"></div>
                        <div class="time_num seconds2"></div>
                    </div>
                </div>
                <div class="time_over">已结束</div>
            </div>
        </div>
        <div class="friends">已助力好友</div>
        <div class="list_tip_text" style="display: none">您还没有获得好友助力，快去邀请吧</div>
        <div class="friend_list" style="display: none">
        </div>
        <div class="close_btn"></div>
    </div>
</div>
    <#include "../../module/login-tip.ftl" />

</@global.main>

