<#import "macro/global.ftl" as global>

<@global.main pageCss="${css.current_redeem}" pageJavascript="${js.current_redeem}" activeNav="我要投资" activeLeftNav="" title="日息宝">
<div class="loan-detail-content loan-detail-new">

    <div class="borderBox clearfix no-border">
        <div class="loan-model">
            <div class="news-share bg-w">
                <h2 class="hd clearfix title-block clearfix">
                    <div class="fl title"><a href="/day-turn-in.ftl">日息宝转入</a></div>
                    <div class="fl title active"><a href="/current/redeem">日息宝转出</a></div>
                </h2>
                <div class="container-block loan-info">
                    <div class="content">
                        <form action="/current/redeem" method="post" id="formOut">
                            <div class="fotm-item">
                                <p>
                                    今日还可以赎回（元）：<span>${redeemRemainAmount!}</span>
                                </p>
                            </div>
                            
                            <div class="fotm-item">
                                <input type="text" name="amount" class="int-item" placeholder="请输入金额" data-limit="${redeemRemainAmount!}" id="turnOut">
                                <div class="info-item">
                                   当日最多可转出${redeemMaxAmount!}元
                                </div>
                            </div>

                            <div class="fotm-item">
                                <@global.isAnonymous>
                                    <a href="/login?redirect=/current/redeem">确认转出</a>
                                </@global.isAnonymous>

                                <@global.isNotAnonymous>
                                    <input type="submit" class="submit-item" value="确认转出" disabled />
                                </@global.isNotAnonymous>

                            </div>
                            <div class="fotm-item">
                                <p class="tip-info">1.转出后资金将返回账户余额，下一个工作日到账，节假日顺延；</p>
                                <p class="tip-info">2.最终到账时间以实际到账时间为准。</p>
                            </div>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>
                    </div> 
                </div>
            </div>
        </div>

        <div class="bg-w borderBox mt-20 project-model">
            <div class="model-nav">
                <h3>日息宝转出说明</h3>
            </div>
            <div class="model-content">
                <ul class="info-list">
                    <li>1.您的每日可赎回金额，会根据您当日赎回剩余额度与可以赎回的金额进行比较，取最小值。</li>
                    <li>2.现阶段用户每日最多可以赎回的额度为10万元。</li>
                    <li>3.用户在平台发起转出申请后，平台将会在次工作日统一进行审核转出操作，转出成功后相关资金将会返还到您的账户余额，请注意查看（节假日顺延）。</li>
                    <li>4.最终到账时间以实际到账时间为准。</li>
                </ul>
            </div>
        </div>
        <div class="bg-w borderBox mt-20 project-model">
            <div class="model-nav">
                <h3>安全保障</h3>
            </div>
            <div class="model-content">
                <ul class="text-list">
                    <li>
                        <p class="intro-title">资金保障</p>

                        <p class="intro-icon icon-one"></p>

                        <p class="intro-text">
                            <span>拓天速贷平台项目都是抵押债权，每笔债权都对应着相应的抵押物。如果投资过程中发生了资金风险，抵押物资将会被处理来为用户的资金提供保障。</span></p>
                    </li>
                    <li>
                        <p class="intro-title">交易保障</p>

                        <p class="intro-icon icon-two"></p>

                        <p class="intro-text">
                            <span>拓天速贷接入了联动优势电子商务有限公司的资金托管系统。交易过程中的充值、投资、提现都在第三方支付平台进行，保证了资金流转的透明和安全。</span></p>
                    </li>
                    <li>
                        <p class="intro-title">信息保障</p>

                        <p class="intro-icon icon-three"></p>

                        <p class="intro-text"><span>采用银行级别的数据传输加密技术，保障用户的信息安全。在同城和异地均建立灾备设备，避免因自然灾害导致用户信息的损失。</span></p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <#include "component/coupon-alert.ftl" />
</div>
    <#include "component/red-envelope-float.ftl" />
</@global.main>