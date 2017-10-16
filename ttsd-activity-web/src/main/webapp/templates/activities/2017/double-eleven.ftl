<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.double11_2017}" pageJavascript="${js.double11_2017}" activeNav="" activeLeftNav="" title="双11活动_活动中心_拓天速贷" keywords="拓天速贷,体验金,抽奖奖励,京东E卡" description="拓天速贷双11活动,活动期间投资顺序为奇数的用户,可获得投资额的1.1倍体验金奖励,投资顺序为偶数的用户可获得抽奖机会,累计投资额每满10万元获赠100元京东E卡.">

<div class="banner">

</div>
<div  id="double11">
    <!--第一部分 begin-->
    <div class="alone-gifts page-width">

       <div class="con-width">
           <div class="alone-title"></div>
           <div class="alone-rules">
               <dl>
                   <dd>1. 活动期间投资活动项目，且投资顺序为奇数的用户，可获得其该笔投资额的<i>111111</i><strong>倍体验金</strong>奖励，体验金多投多得，上不封顶；</dd>
                   <dd>2.活动期间投资活动项目，且投资顺序为 <strong>偶数</strong>的用户，可获赠 <strong> 一次抽奖机会。</strong>每人每日最多可累计增加10次抽奖机会，如用户当日新增抽奖机会超过10次，则机会不再随投资次数的累计而增加。</dd>
               </dl>
           </div>

           <div class="gift-circle-frame">
               <div class="gift-circle-out">
                   <div class="pointer-img"></div>
                   <div class="rotate-btn"></div>
               </div>
               <div class="small-btn" id="draw_btn">立即抽奖</div>
               <div class="gift-circle-detail">
                       <div class="lottery-times">
                          <#--<p> 剩余抽奖次数：<span id="leftDrawCount">8</span></p>-->
                           <p><a href="javascript:;">登录</a>后查看抽奖次数</p>
                       </div>

                       <div class="gift-info-box">
                           <ul class="gift-record clearfix">
                               <li class="active"><span>中奖记录</span></li>
                               <li><span>我的奖品</span></li>
                           </ul>
                           <div class="record-list">
                               <ul class="record-model user-record" >
                                   <li>恭喜139****7148抽中 20元现金红包</li> <li>恭喜139****7148抽中 20元现金红包</li> <li>恭喜139****7148抽中 20元现金红包</li> <li>恭喜139****7148抽中 20元现金红包</li> <li>恭喜139****7148抽中 20元现金红包</li> <li>恭喜139****7148抽中 20元现金红包</li> <li>恭喜139****7148抽中 20元现金红包</li> <li>恭喜139****7148抽中 20元现金红包</li>
                               </ul>
                               <ul class="record-model own-record" style="display: none"></ul>
                           </div>
                       </div>
                   </div>
               </div>

       </div>
    </div>
    <!--第一部分 end-->

    <!--第二部分 begin-->
    <div class="jde-cart page-width">
        <div class="jde-title"></div>
        <div class="con-width">
            <div class="jde-rules">
                <dl>
                    <dd> 活动期间，累计投资额每满10万元，即可获赠 <strong>100元京东E卡</strong></dd>
                    <dd> 以此类推，如投资满100万可获赠 <strong> 1000元京东E卡，多投夺得，上不封顶。</strong></dd>
                </dl>
            </div>
            <div class="total-money">
                <p>累计投资金额：103,989.00元</p>
                <div class="awarded">已获取</div>
                <div class="reward"> <span>100</span> </div>
                <div class="to-login"><a href="javascript:;">登录后查看已获得奖励></a></div>
                <div class="small-btn">获得更多奖励</div>
            </div>
        </div>
    </div>
    <!--第二部分 end-->
    <#--<#include "../module/login-tip.ftl" />-->
    <!--温馨提示 begin-->
    <div class="kindly-tips">
        <div class="con-width">
            <dl>
                <dt>温馨提示</dt>
                <dd>1.活动仅限60天以上直投项目，“0元购”项目、债权转让、新手项目及体验项目不参与本活动；</dd>
                <dd>2.用户投资顺序可以在项目详情下的“出借记录”中查看，用户投标成功后所获奖励类型将以消息提醒形式提示告知；</dd>
                <dd>3.抽奖机会即投即生成，投资顺序为奇数的用户可即刻进入活动页面进行抽奖；</dd>
                <dd>4.投资所获体验金奖励将于项目放款后发放，用户可在PC端“我的账户”或App端“我的”中查看；</dd>
                <dd>5.抽奖所获优惠券、体验金即时发放，用户可在PC端“我的账户”或App端“我的”中查看；</dd>
                <dd>6.所有实物奖品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；</dd>
                <dd>7.为保证活动的公平性，京东E卡获奖用户在活动期间内所投的所有项目不允许进行债权转让，如进行转让，则取消获奖资格；</dd>
                <dd>8.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</dd>
                <dd>8.活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</dd>

            </dl>
        </div>
    </div>
    <!--温馨提示 end-->
    <#--弹框提示-->
    <#include "../../module/login-tip.ftl" />
    <#--奖品提示-->
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
