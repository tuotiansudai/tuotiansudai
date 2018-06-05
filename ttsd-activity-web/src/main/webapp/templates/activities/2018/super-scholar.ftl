<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.super_scholar_2018}" pageJavascript="${js.super_scholar_2018}" activeNav="" activeLeftNav="" title='答题赢加"薪"_学霸升值季_拓天速贷' keywords="拓天速贷,答题赢加薪,邀请好友,返现奖励" description='拓天速贷答题赢加"薪"活动,用户每日答题,投资可获得年化0.2%-0.6%返现奖励,完成答题赠送最高0.5%加息券,分享答题、邀请好友注册、投资还可增加年化返现,返现+加息高至2%.'>
<div class="super-scholar-banner">

</div>
<div class="super-scholar-container">
    <h2 class="part-one-title">
        <div class="ball"></div>
    </h2>

    <@global.isAnonymous>
        <h4 class="notice"><span class="horn"></span>今日还未答题哦！赶快答题赢返现吧！</h4>
    </@global.isAnonymous>
    <@global.isNotAnonymous>
        <input id="isDoneQuestion" type="hidden" value="${doQuestion?c}">
        <#if doQuestion>
            <h4 class="notice isAnswered"><span class="horn"></span>今日已答题，投资可获丰厚返现，赶快投资吧！</h4>
        <#else>
            <h4 class="notice"><span class="horn"></span>今日还未答题哦！赶快答题赢返现吧！</h4>
        </#if>
    </@global.isNotAnonymous>
    <div class="to-question-tip">
        微信扫码关注“拓天速贷服务号”，<br/>
        回复“答题”，开始赢返现！
    </div>

    <div class="qrcode qrcode-pc" id="qrcodePC">
        <div class="code" id="qrcodeBox"><span class="logo"></span><img id="rqcodeImg" src="" alt=""></div>
        <span class="tips"></span>
    </div>
    <div class="qrcode qrcode-wap" id="qrcodeWap">
        <div class="code code-wap"><img src="" alt=""></div>
    </div>
    <#if !doQuestion?? || (doQuestion?? && !doQuestion)>
        <a class="immediate-answer" id="immediateAnswer" href="javascript:;"></a>
    <#else>
        <a class="immediate-answer already-answer" href="/activity/super-scholar/view/result"></a>
    </#if>

        <div class="my-reappearance">
            <div class="ball"></div>
        </div>
    <#if data!?size!=0>
        <div class="my_reappearance-table">
            <div class="reappearance-content" id="reappearanceContent">
                <table>
                    <thead>
                    <th>投资金额</th>
                    <th>年化投资金额</th>
                    <th>获取返现比率</th>
                    <th>获取返现金额</th>
                    <th>时间</th>
                    </thead>
                    <tbody id="reappearanceList" class="reappearance-list">

                        <#list data as invest>
                        <tr>
                            <td>${invest.amount}</td>
                            <td>${invest.annualizedAmount}</td>
                            <td>${invest.rewardRate}</td>
                            <td>${invest.reward}</td>
                            <td>${invest.investTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                        </tr>
                        </#list>

                    </tbody>
                </table>
            </div>
            <div class="get-more">
                <a class="look-btn" id="getMoreData" href="javascript:;">查看更多</a>
            </div>
        </div>
    <#else>
        <div class="my_reappearance-table">
            <div class="reappearance-content">
                <table>
                    <thead>
                    <th>投资金额</th>
                    <th>年化投资金额</th>
                    <th>获取返现比率</th>
                    <th>获取返现金额</th>
                    <th>时间</th>
                    </thead>
                    <tbody class="reappearance-list">

                    <tr><td class="no-data" colspan="5">暂无返现奖励~</td></tr>

                    </tbody>
                </table>
            </div>
            <div class="get-more">
                <a class="look-btn" id="getMoreData" href="javascript:;">查看更多</a>
            </div>
        </div>
    </#if>

        <div class="my-reappearance-wap">
            <a href="javascript:;" id="myReappearanceWapBtn">我的返现奖励</a>
        </div>
    <#if data!?size!=0>
    <#--m站我的返现奖励-->
        <div class="my_reappearance-table-wap" id="myReappearanceWap">
            <div class="reappearance-content" id="reappearanceContent">
                <table>
                    <thead>
                    <th>投资金额</th>
                    <th>年化投资金额</th>
                    <th>获取返现比率</th>
                    </thead>
                    <tbody id="reappearanceListWap" class="reappearance-list">

                        <#list data as invest>
                        <tr>
                            <td>${invest.amount}</td>
                            <td>${invest.annualizedAmount}</td>
                            <td>${invest.rewardRate}</td>
                        </tr>
                        </#list>

                    </tbody>
                </table>
            </div>

        </div>
    <#else>
        <div class="my_reappearance-table-wap" id="myReappearanceWap">
            <div class="reappearance-content">
                <table>
                    <thead>
                    <th>投资金额</th>
                    <th>年化投资金额</th>
                    <th>获取返现比率</th>
                    </thead>
                    <tbody class="reappearance-list">

                            <td colspan="3" class="no-data">暂无返现奖励~</td>

                    </tbody>
                </table>
            </div>

        </div>
    </#if>
<#--m站我的返现奖励-->
    <div class="to-loan"><a class="to-loan-btn" href="/loan-list">去投资</a></div>
    <div class="tips-and-rules">
        <div class="top"></div>
        <div class="bg">
            <div class="activity-rules">
                <h2>活动规则：</h2>
                <p>1、活动期间，用户每日登录均可获得1次答题机会；</p>
                <p>2、每日随机5道题目。根据答对题目数量，当日投资可获得年化0.2%-0.6%返现；</p>
                <table>
                    <tr>
                        <td>答对数量</td>
                        <td>1</td>
                        <td>2</td>
                        <td>3</td>
                        <td>4</td>
                        <td>5</td>
                    </tr>
                    <tr>
                        <td>返现比率（年化）</td>
                        <td>0.2%</td>
                        <td>0.3%</td>
                        <td>0.4%</td>
                        <td>0.5%</td>
                        <td>0.6%</td>
                    </tr>
                </table>
                <p>3、每日只要完成答题，就赠送最高0.5%的加息券；</p>
                <p>4、【学霸加分题一】分享答题页面，当日投资返现比例增加0.1%；</p>
                <p>5、【学霸加分题二】</p>
                <p>(1)每日邀请的好友只要有1位注册并实名认证，您当日投资增加0.2%返现；</p>
                <p>(2)每日邀请的好友只要有1位在注册当日投资60天以上项目，投资金额1000元以上，您当日投资再增加年化0.6%返现;</p>
            </div>
            <div class="kindky-tips">
                <div class="activity-rules">
                    <h2>温馨提示：</h2>
                    <p>1、用户每日答题，分享，邀请的好友注册实名认证，邀请的好友投资所获的返现比率仅限当日24:00前投资使用，如当日未投资，返现比率归零；</p>
                    <p>2、 用户需通过本活动页面的“邀请好友认证”按钮邀请好友注册认证和投资，方可获得对应的返现比例，通过其他方式邀请，无法获得本活动发放的返现比例；</p>
                    <p>3、用户每日答题、分享、邀请的好友注册实名认证、邀请的好友投资所获得的所有返现将于用户当日所投项目放款后24小时内统一发放到账户；</p>
                    <p>4、用户答题所获赠加息券即时发放，用户可在PC端“我的账户”或App端“我的-优惠券”中进行查看；</p>
                    <p>5、本活动仅限直投项目，债权转让、体验项目及新手专享项目不参与累计；</p>
                    <table>
                        <tr>
                            <td>60天 - 90天项目</td>
                            <td>年化投资额 = 实际投资额*90/360</td>
                        </tr>
                        <tr>
                            <td>120天 - 180天项目</td>
                            <td>年化投资额 = 实际投资额*180/360</td>
                        </tr>
                        <tr>
                            <td>330天 - 360天项目</td>
                            <td>年化投资额 = 实际投资金额</td>
                        </tr>
                    </table>
                    <p>6、活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</p>
                    <p>7、活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有；</p>
                </div>
            </div>
        </div>
        <div class="bot"></div>

    </div>

</div>

</@global.main>