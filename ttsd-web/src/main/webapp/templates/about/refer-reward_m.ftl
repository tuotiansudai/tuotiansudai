<#import "../macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.bonus_rule}" pageJavascript="${m_js.bonus_rule}" title="推荐送奖金">
<div class="my-account-content bonus-rule recommend-rule" id="recommendRule">
    <div class="m-header"><em id="iconRecomend" class="icon-left"><i></i></em>推荐送奖金 </div>
    <div class="main-content">
        <div class="box-column">
            <h2>
                邀请好友投资，拿<em class="key">1%奖励</em>
            </h2>
            <div class="box-in">
            <span class="man-col">
                <i class="man img-friend"></i>
                <i>好友<br/>
购买50000元360天产品</i>
            </span>
                <span class="arrow-col">
                奖励1%
                <i class="icon-arrow"></i>
            </span>
                <span class="man-col">
                  <i class="man img-you"></i>
                <i>您 <br/>
                获得<em class="key">493.15元</em>奖励
                  </i>
            </span>
            </div>
        </div>

        <div class="recommend-note">
            1、好友所投项目放款后，奖励即发放至您的账户；<br/>
            2、推荐奖励=<em class="key"> 好友投资金额×1%×<i class="period">项目期限</i></em>；
        </div>

        <div class="learn-more">
            <a id="toRewardRules" href="javascript:;"> 了解更多规则></a>
        </div>
        <div class="rule-award-how">
            <i class="icon-kaola">

            </i>
            <b>如何获得奖励？</b>

            1、分享链接发送给好友或发朋友圈； <br/>
            2、好友通过此链接注册并投资；<br/>
            3、好友投资项目放款后，奖金即发放至您的账户；<br/>
        </div>

        <div class="share-box">
            <h2>立即分享拿奖励</h2>

            <div class="share-box-inner">
                <div id="sweep_face" class="share-list">
            <span class="share-face">
            <i></i>
            <div>当面扫</div>
                <div>查看邀请记录请下载拓天速贷APP</div>
        </span>


                </div>
            </div>

        </div>


    </div>

</div>

<div class="my-account-content bonus-rule" style="display: none" id="rewardRules">
    <div class="m-header"><em id="iconRewardRules" class="icon-left"><i class="fa fa-angle-left"></i></em>奖励规则 </div>
    <div class="main-content">
        <div class="box-column">
            <h2>
                邀请好友投资，拿<em class="key">1%奖励</em>
            </h2>
            <div class="box-in">
            <span class="man-col">
                <i class="man img-friend"></i>
                <i>好友<br/>
购买50000元360天产品</i>
            </span>
                <span class="arrow-col">
                奖励1%
                <i class="icon-arrow"></i>
            </span>
                <span class="man-col">
                  <i class="man img-you"></i>
                <i>您 <br/>
                获得<em class="key">493.15元</em>奖励
                  </i>
            </span>
            </div>
        </div>

        <div class="box-column">
            <h2>
                好友<em class="key">再邀人</em>投资，<em class="key">再拿1%</em>奖励
            </h2>
            <div class="box-in">
            <span class="man-col">
                <i class="man img-friend-one"></i>
                <i>好友邀请的好友 <br/>
购买50000元360天产品</i>
            </span>
                <span class="arrow-col">
                奖励1%
                <i class="icon-arrow"></i>
            </span>
                <span class="man-col">
                 <i class="man img-you"></i>
                <i>您还可以 <br/>
                获得<em class="key">493.15元</em>奖励
                  </i>

            </span>
            </div>
        </div>

        <div class="box-total">
            您共计获得<br/>
            493.15+493.15=<em class="key">986.30元</em> 奖励

        </div>

        <div class="rule-award">
            <b>奖励规则</b>

            1、好友所投项目放款后，奖励即发放至您的账户；<br/>
            2、推荐奖励=<em class="key">好友投资金额×1%× <i class="period">项目期限</i> </em> ；<br/>
            3、好友投资以及好友邀请的好友投资，您均可获得奖励；好友的好友再邀请人投资，您不再获得奖励；<br/>
            4、好友注册可得6888元体验金，体验完成再得668元现金红包。<br/>

        </div>

        <table>
            <caption>好友投资五万元</caption>
            <tr>
                <th>项目期限</th>
                <th>30天</th>
                <th>90天</th>
                <th>180天</th>
                <th>360天</th>
            </tr>
            <tr>
                <td class="title">获得奖励</td>
                <td>41.09元</td>
                <td>123.28元</td>
                <td>246.57元</td>
                <td>493.15元</td>
            </tr>
        </table>

        <table>
            <caption>好友的好友投资五万元</caption>
            <tr>
                <th>项目期限</th>
                <th>30天</th>
                <th>90天</th>
                <th>180天</th>
                <th>360天</th>
            </tr>
            <tr>
                <td class="title">获得奖励</td>
                <td>41.09元</td>
                <td>123.28元</td>
                <td>246.57元</td>
                <td>493.15元</td>
            </tr>
        </table>
    </div>


</div>

<div class="my-account-content bonus-rule recommend-rule" id="scanCode" style="display: none">
    <div class="m-header"><em id="iconScanCode" class="icon-left"><i class="fa fa-angle-left"></i></em>我的二维码 </div>
    <div class="main-content">
        <div class="code-item">
            <p>
                <span></span>
            </p>
            <p>扫描二维码，一起赚钱吧</p>
        </div>
        <dl class="info-item">
            <dt>扫码说明:</dt>
            <dd>1、好友扫描二维码成功，打开注册链接；</dd>
            <dd>2、好友按照提示完成注册并成功投资，您就可以获得奖励啦！</dd>
        </dl>
    </div>
</div>
</@global.main>
