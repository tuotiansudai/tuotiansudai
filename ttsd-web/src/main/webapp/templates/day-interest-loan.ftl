<#import "macro/global-dev.ftl" as global>
<#assign jsName = 'loan_detail_day' >

<#assign js = {"${jsName}":"http://localhost:3008/web/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/web/js/${jsName}.css"}>

<@global.main pageCss="${css.loan_detail_day}" pageJavascript="${js.loan_detail_day}" activeNav="我要投资" activeLeftNav="" title="日息宝">
<div class="loan-detail-content loan-detail-new" id="experienceLoanDetailContent" data-loan-progress="${loan.progress?string.computer}">

    <div class="borderBox clearfix no-border">
        <div class="loan-model">
            <div class="news-share bg-w">
                <h2 class="hd clearfix title-block clearfix">
                    <div class="fl title">日息宝</div>
                    <span class="new-free">3天锁定期</span>
                    <span class="new-free">本息复投</span>
                    <span class="new-free">本息复投</span>
                </h2>
                <div class="container-block loan-info">
                    <div class="content">
                        <div class="row loan-number-detail clearfix">
                            <div class="col-md-3">
                                <div class="title">昨日收益（元)</div>
                                <div class="number red">
                                    6.88
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="title">累计收益（元）</div>
                                <div class="number">6.88</div>
                            </div>
                            <div class="col-md-3">
                                <div class="title">总资产（元）</div>
                                <div class="number">
                                    <span>1212</span>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="title">预期年化收益</div>
                                <div class="number">
                                    <em>6.88</em><span>%</span>
                                </div>
                            </div>
                        </div>
                        <div class="loan-active-detail">
                            <br/>
                                <span class="title">还款方式：</span>
                                到期付息,体验金收回。
                        </div>
                    </div> <#-- .content end tag -->
                </div> <#-- .container-block end tag -->
            </div>
        </div>

        <div class="chart-info-responsive bg-w">
            起投金额：${loan.minInvestAmount}元(体验金)<br/>
            项目期限：${loan.duration}天<br/>
            起息时间：即投即生息<br/>
            还款方式：到期付息,体验金收回。<br/>
            投资要求：新手体验标仅能使用新手体验券进行投资。<br/>
            项目简介：此项目为拓天速贷体验项目，是由拓天速贷设立的专门提供给新注册客户，进行投资体验的虚拟项目。<br/>
        </div>
        <div class="bg-w borderBox mt-20 project-model">
            <div class="model-nav">
                <h3>日息宝产品说明</h3>
            </div>
            <div class="model-content">
                <ul class="info-list">
                    <li>1、拓天体验项目是由拓天速贷专门提供给客户体验平台流程的体验项目。</li>
                    <li>2、仅拥有平台体验金的用户才能投资拓天体验金项目。</li>
                    <li>3、拓天体验金项目50起投；项目到期后，平台收回本金，体验金产生的收益归用户所有。</li>
                    <li>4、拓天体验金项目不可转让。</li>
                    <li>5、用户在累计投资直投项目（债权转让项目和拓天体验金项目除外）满1000元后，方可提现体验金产生的收益。</li>
                    <li>6、如有疑问请联系在线客服或拨打400-169-1188。</li>
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