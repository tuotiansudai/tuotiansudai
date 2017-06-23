<#import "macro/global-dev.ftl" as global>
<#assign jsName = 'day_turn_in' >

<#assign js = {"${jsName}":"http://localhost:3008/web/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/web/js/${jsName}.css"}>

<@global.main pageCss="${css.day_turn_in}" pageJavascript="${js.day_turn_in}" activeNav="我要投资" activeLeftNav="" title="日息宝">
<div class="loan-detail-content loan-detail-new" id="dayLoanDetailContent" data-loan-progress="${loan.progress?string.computer}">

    <div class="borderBox clearfix no-border">
        <div class="loan-model">
            <div class="news-share bg-w">
                <div class="container-block loan-info">
                    <div class="content">
                        dsfd
                    </div> <#-- .content end tag -->
                </div> <#-- .container-block end tag -->
            </div>
        </div>

        <div class="bg-w borderBox mt-20 project-model">
            <div class="model-nav">
                <h3>日息宝转入说明</h3>
            </div>
            <div class="model-content">
                <ul class="info-list">
                    <li>１.当前阶段所有用户在平台可购的日息宝总额最多为50万元。</li>
                    <li>2.在购买时所展示的今日最多可购买份额，是平台结合您的限额情况与平台当日剩余可售份额计算而得的结果。</li>
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