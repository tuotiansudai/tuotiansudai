<#import "macro/global-dev.ftl" as global>
<#assign jsName = 'day_turn_out' >

<#assign js = {"${jsName}":"http://localhost:3008/web/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/web/js/${jsName}.css"}>

<@global.main pageCss="${css.day_turn_out}" pageJavascript="${js.day_turn_out}" activeNav="我要投资" activeLeftNav="" title="日息宝">
<div class="loan-detail-content loan-detail-new" id="dayLoanDetailContent" data-loan-progress="${loan.progress?string.computer}">

    <div class="borderBox clearfix no-border">
        <div class="loan-model">
            <div class="news-share bg-w">
                <h2 class="hd clearfix title-block clearfix">
                    <div class="fl title">日息宝</div>
                    <span class="label-item">3天锁定期</span>
                    <span class="label-item">本息复投</span>
                    <span class="label-item">本息复投</span>
                </h2>
                <div class="container-block loan-info">
                    <div class="content">
                        <div class="row loan-number-detail clearfix">
                            <div class="col-md-3 tl">
                                <div class="title">昨日收益（元)</div>
                                <div class="number red">
                                    6.88
                                </div>
                            </div>
                            <div class="col-md-3 tc">
                                <div class="title">累计收益（元）</div>
                                <div class="number">6.88</div>
                            </div>
                            <div class="col-md-3 tc">
                                <div class="title">总资产（元）</div>
                                <div class="number">
                                    <span>1212</span>
                                </div>
                            </div>
                            <div class="col-md-3 tr">
                                <div class="title last">预期年化收益</div>
                                <div class="number">
                                    <em>6.88</em><span>%</span>
                                </div>
                            </div>
                        </div>
                        <div class="row loan-active-detail clearfix">
                            <div class="col-md-12">
                                <div class="progress-bar">
                                    <div class="progress-inner" style="width: 49.00%"></div>
                                </div>
                                <div class="surplus-item">
                                    <span class="fl">今日剩余（万元）</span>
                                    <span class="fr">55.54</span>
                                </div>
                            </div>
                            <div class="col-md-12">
                                <span class="tip-item">持有越多，收益越高</span>
                                <button class="turn-disabled fr" disabled="">计算收益中</button>
                                <a href="" class="turn-in fr">转入</a>
                                <a href="" class="turn-out fr">转出</a>
                            </div>
                        </div>
                    </div> <#-- .content end tag -->
                </div> <#-- .container-block end tag -->
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
    <div id="freeSuccess" data-account="${isAccount}" style="display: none">
        <div class="success-info-tip">
            <i class="icon-tip"></i>
            <#--没有实名认证-->
            <div class="detail-word">
                <h2>投资成功！</h2> 您已成功投资体验金<span class="finish-amount"></span>元 <br/>
                收益到账后后，需要实名认证并投资方可提现 <a href="/register/account" class="key">立即立即认证>></a>
                <div class="pad-m-tb" style="padding-left:50px;">
                    <button type="button" class="btn-normal close-free">确认</button>
                </div>
            </div>

            <#--已经实名认证-->
            <div class="detail-word" style="display: none;">
                <h2>投资成功！</h2> 您已成功投资体验金<span class="finish-amount"></span>元
                <div class="pad-m-tb" style="padding-left:50px;">
                    <button type="button" class="btn-normal close-free">确认</button>
                </div>
            </div>
        </div>
    </div>
    <#include "component/coupon-alert.ftl" />
</div>
    <#include "component/red-envelope-float.ftl" />
</@global.main>