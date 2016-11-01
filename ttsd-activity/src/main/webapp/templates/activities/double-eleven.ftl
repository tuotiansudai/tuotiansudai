<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.double_eleven}" pageJavascript="${js.double_eleven}" activeNav="" activeLeftNav="" title="拓天英豪榜_英豪榜活动_拓天速贷" keywords="拓天英豪榜,英豪榜活动,投资排行榜,拓天速贷" description="拓天速贷投资排行榜活动,狂欢大轰趴,连嗨三周半,打响排行保卫战,拓天英豪榜投资活动每日神秘大奖悬赏最具实力的投资人.">
<div class="double-eleven-container">
    <div class="top-intro-img">
        <img src="${staticServer}/activity/images/double-eleven/top-intro.jpg" alt="双十一" width="100%" class="top-img">
        <img src="${staticServer}/activity/images/double-eleven/top-intro-phone.png" alt="双十一" width="100%" class="top-img-phone">
    </div>
    <div class="content-group">
        <div class="wp clearfix">
            <div class="reg-tag-current" style="display: none">
                <#include '../register.ftl' />
            </div>
            <div class="model-group actor-info">
                <div class="content-item after-line">
                    <p class="title-text">请问脱光一次值多少钱？别报警，拓天速贷此番祭出</p>
                    <p>脱光神器：</p>
                    <p><span><img src="${staticServer}/activity/images/double-eleven/info-text.png" width="100%"></span></p>
                    <p>女神喜欢的香槟玫瑰、法国大餐、星级酒店，统统满足她！所谓“但愿人长久，钱里共婵娟”。兄弟只能帮到这了，接下来你懂得哦！</p>
                </div>
            </div>
            <div class="model-group time-count">
                <div class="content-item after-line">
                    <div class="model-item">
                        <h3>
                            <span>
                                <i class="left-line"></i>
                                <img src="${staticServer}/activity/images/double-eleven/title-one.png" >
                                <i class="right-line"></i>
                            </span>
                        </h3>
                        <p>活动期间，每日上午11:11、下午1:11祭出一个预期年化收益在现有基础上增加1.1%的高息项目，</p>
                        <p>请您在“我要投资”页面找到带有“限时专享”标识的项目进行投资，即可参与活动。</p>
                    </div>
                    <div class="product-group">
                        <i class="date-icon">
                            <span>11.7</span>
                        </i>
                        <ul>
                            <li>
                                <p><img src="${staticServer}/activity/images/double-eleven/first-time.png" ></p>
                                <p class="tip-info">即将登场</p>
                                <p class="time-text">
                                    <span>09 : 58 : 37</span>
                                </p>
                                <p class="btn-item">
                                    <span>马上投资</span>
                                </p>
                            </li>
                            <li class="end">
                                <p><img src="${staticServer}/activity/images/double-eleven/twice-time.png" ></p>
                                <p class="tip-info">即将登场</p>
                                <p class="time-text">
                                    <span>09 : 58 : 37</span>
                                </p>
                                <p class="btn-item">
                                    <span>马上投资</span>
                                </p>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="model-group red-bag">
                <div class="content-item after-line">
                    <div class="model-item">
                        <h3>
                            <span>
                                <i class="left-line"></i>
                                <img src="${staticServer}/activity/images/double-eleven/title-two.png" >
                                <i class="right-line"></i>
                            </span>
                        </h3>
                        <p>活动期间，投资90天、180天或360天标的，<span>单笔1111元</span>以上即送<span>“脱光礼包”</span>(每人限领1个)，</p>
                        <p>礼包于活动结束后三个工作日内发放，用户可在PC端“我的账户-我的宝藏”或App端“我的财富-我的宝藏”中查看。</p>
                        <div class="coupon-group">
                            <h3>
                                <img src="${staticServer}/activity/images/double-eleven/bag-title.png" >
                            </h3>
                            <ul class="coupon-item">
                                <li>
                                    <p><span>20元</span>红包</p>
                                </li>
                                <li>
                                    <p><span>50元</span>红包</p>
                                </li>
                                <li>
                                    <p><span>100元</span>红包</p>
                                </li>
                                <li>
                                    <p><span>0.5%</span>加息券</p>
                                </li>
                            </ul>
                        </div>
                        <div class="loan-group">
                            <a href="/loan-list">马上投资</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="model-group invite-people">
                <div class="content-item after-line">
                    <div class="model-item">
                        <h3>
                            <span>
                                <i class="left-line"></i>
                                <img src="${staticServer}/activity/images/double-eleven/title-three.png" >
                                <i class="right-line"></i>
                            </span>
                        </h3>
                        <p class="font-14 text-c actor-phone">活动期间，新用户在平台完成注册、实名认证、绑卡、充值、投资均可获得一次100%中奖的抽奖机会；</p>
                        <ul class="loan-list-group">
                            <!-- 如果进行到下一个任务，添加active class给li标签
                                 如果已完成的任务，添加finish class给li标签
                            -->
                            <li class="finish">
                                <span class="step step-one"></span>
                                <a href="#" class="step-btn">已注册</a>
                            </li>
                            <li class="active">
                                <i class="after-icon"></i>
                                <span class="step step-two"></span>
                                <a href="#" class="step-btn">去认证</a>
                            </li>
                            <li>
                                <i class="after-icon"></i>
                                <span class="step step-three"></span>
                                <a href="#" class="step-btn">绑卡</a>
                            </li>
                            <li>
                                <i class="after-icon"></i>
                                <span class="step step-four"></span>
                                <a href="#" class="step-btn">充值</a>
                            </li>
                            <li>
                                <i class="after-icon"></i>
                                <span class="step step-five"></span>
                                <a href="#" class="step-btn">投资</a>
                            </li>
                        </ul>
                        <p class="font-14 text-c actor-phone">活动期间，每推荐一名好友注册也可获得一次抽奖机会；好友投资，还可再得一次抽奖机会。邀请越多机会越多。</p>
                        <dl class="actor-word">
                            <dt>活动期间</dt>
                            <dd>1、新用户在平台完成注册、实名认证、绑卡、充值、投资均可获得一次100%中奖的抽奖机会；</dd>
                            <dd>2、每推荐一名好友注册也可获得一次抽奖机会；好友投资，还可再得一次抽奖机会。邀请越多机会越多。</dd>
                        </dl>
                        <div class="loan-group">
                            <a href="/referrer/refer-list" class="font-18">立即邀请好友赢抽奖机会</a>
                        </div>
                    </div>
                    <#include "../model/nine-lottery.ftl"/>
                </div>
            </div>
            <div class="model-group tip-text">
                <div class="content-item">
                    <dl class="rule-list-group">
                        <dt>温馨提示：</dt>
                        <dd>1、抽奖活动的中奖结果将于次日由客服联系确认，红包在中奖后即时发放，实物奖品及话费、爱奇艺会员将于活动结束后七个工作日内统一安排发放，部分地区邮费自付，详询客服；</dd>
                        <dd>2、活动中如果出现恶意刷量等违规行为，拓天速贷将取消您获得奖励的资格，并有权撤销违规交易，拓天速贷在法律范围内保留对本活动的最终解释权。</dd>
                        <dd class="wx-code"><img src="${staticServer}/activity/images/double-eleven/wx-code.png" ></dd>
                        <dd class="rule-intro">抢先了解活动攻略，赚钱快人一步</dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
</div>
</@global.main>