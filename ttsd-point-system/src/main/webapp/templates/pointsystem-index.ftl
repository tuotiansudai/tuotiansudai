<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.pointsystem_index}" pageJavascript="${js.pointsystem_index}" activeNav="积分商城" activeLeftNav="" title="积分商城">


<div class="global-member-store">
    <div class="store-top">
        <div class="store-login">
            <div class="login-model hide">
                <p>
                    <a href="${webServer}/login" class="login-btn">立即登录</a>
                </p>
                <p>
                   <a href="${webServer}/register/user" class="regist-btn">新用户注册></a>
                </p> 
            </div>
            <div class="user-info">
                <h3>我的积分</h3>
                <p>1,900,300</p>
                <p><span data-url="${webServer}/pointsystem/signIn" id="signBtn">签到</span></p>
            </div>
            <ul class="other-list">
                <li class="right-line">
                    <p><span>做任务赚积分</span></p>
                    <p><a href="/pointsystem/task">去做任务</a></p>
                </li>
                <li>
                    <p><span>投资赚积分</span></p>
                    <p><a href="${webServer}/loan-list">去投资</a></p>
                </li>
            </ul>
            <div class="user-trade">
                <a href="#">兑换记录></a> | <a href="#">积分明细></a>
            </div>
            <div class="sign-layer" id="signLayer">
                <div class="sign-layer-list">
                    <div class="sign-top">
                        <div class="close-btn" id="closeSign"></div>
                        <p class="sign-text">签到成功，领取5积分！</p>
                        <p class="tomorrow-text">明日可领10积分</p>
                        <p class="img-beans">
                            <img src="${staticServer}/pointsystem/images/sign-beans.png"/>
                            <span class="add-dou">
                                +5
                            </span>
                        </p>
                        <p class="intro-text">连续签到，积分翻倍送，最多每天可领<span>80</span>积分！</p>
                    </div>
                    <div class="sign-bottom">
                        <ul>
                            <li>
                                <p class="day-name">第1天</p>
                                <p class="day-beans">
                                    <span>5</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第2天</p>
                                <p class="day-beans">
                                    <span>10</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第3天</p>
                                <p class="day-beans">
                                    <span>20</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第4天</p>
                                <p class="day-beans">
                                    <span>40</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第5天</p>
                                <p class="day-beans">
                                    <span>80</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第6天</p>
                                <p class="day-beans">
                                    <span>80</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li>
                                <p class="day-name">第7天</p>
                                <p class="day-beans">
                                    <span>80</span>
                                    <i class="bean-img"></i>
                                </p>
                            </li>
                            <li class="last-day">
                                <p class="day-name">第N天</p>
                                <p class="day-beans">
                                    <span>...</span>
                                </p>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="store-title">
        <span>
            <i class="title-left"></i>
            虚拟商品
            <i class="title-right"></i>
        </span>
    </div>
    <div class="store-material">
        <div class="wp clearfix">
            <ul class="material-list">
                <li class="big-gift">
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img coupon-bg">
                        <span><i>0.5</i>%</span>
                        <span>加息券</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">0.5%加息券</span>
                        <span class="price-text">尊享价：<i>2000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
                <li class="big-gift">
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img bag-bg">
                        <span><i>500</i>元</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">500元现金红包</span>
                        <span class="price-text">尊享价：<i>5000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
                <li>
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img coupon-bg">
                        <span><i>0.5</i>%</span>
                        <span>加息券</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">0.5%加息券</span>
                        <span class="price-text">尊享价：<i>2000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
                <li>
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img bag-bg">
                        <span><i>500</i>元</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">500元现金红包</span>
                        <span class="price-text">尊享价：<i>5000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
                <li>
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img coupon-bg">
                        <span><i>0.5</i>%</span>
                        <span>加息券</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">0.5%加息券</span>
                        <span class="price-text">尊享价：<i>2000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
                <li>
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img bag-bg">
                        <span><i>500</i>元</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">500元现金红包</span>
                        <span class="price-text">尊享价：<i>5000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
                <li>
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img coupon-bg">
                        <span><i>0.5</i>%</span>
                        <span>加息券</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">0.5%加息券</span>
                        <span class="price-text">尊享价：<i>2000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
                <li>
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img bag-bg">
                        <span><i>500</i>元</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">500元现金红包</span>
                        <span class="price-text">尊享价：<i>5000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
            </ul>
        </div>
    </div>
    <div class="store-title">
        <span>
            <i class="title-left"></i>
            实物商品
            <i class="title-right"></i>
        </span>
    </div>
    <div class="store-material">
        <div class="wp clearfix">
            <ul class="material-list">
                <li class="big-gift">
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img coupon-bg">
                        <span><i>0.5</i>%</span>
                        <span>加息券</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">0.5%加息券</span>
                        <span class="price-text">尊享价：<i>2000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
                <li class="big-gift">
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img bag-bg">
                        <span><i>500</i>元</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">500元现金红包</span>
                        <span class="price-text">尊享价：<i>5000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
                <li>
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img coupon-bg">
                        <span><i>0.5</i>%</span>
                        <span>加息券</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">0.5%加息券</span>
                        <span class="price-text">尊享价：<i>2000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
                <li>
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img bag-bg">
                        <span><i>500</i>元</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">500元现金红包</span>
                        <span class="price-text">尊享价：<i>5000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
                <li>
                    <p class="num-text">剩余100件</p>
                    <p class="mater-img coupon-bg">
                        <span><i>0.5</i>%</span>
                        <span>加息券</span>
                    </p>
                    <p class="convert-btn">
                        <span class="name-text">0.5%加息券</span>
                        <span class="price-text">尊享价：<i>2000</i>积分</span>
                        <a href="#">
                            <span class="get-btn">立即兑换</span>
                        </a>
                    </p>
                </li>
            </ul>
        </div>
    </div>
</div>




</@global.main>