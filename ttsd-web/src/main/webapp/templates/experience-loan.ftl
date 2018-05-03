<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.loan_detail_experience}" pageJavascript="${js.loan_detail_experience}" activeNav="我要投资" activeLeftNav="" title="新手体验项目">
<div class="loan-detail-content loan-detail-new" id="experienceLoanDetailContent" data-loan-progress="${loan.progress?string.computer}" data-estimate="${estimate?string('true', 'false')}">

    <div class="borderBox clearfix no-border">
        <div class="loan-model">
            <div class="news-share bg-w">
                <h2 class="hd clearfix title-block new-free-tag">
                    <div class="fl title">${loan.name}</div>
                    <span class="new-free"></span>
                </h2>
                <div class="container-block loan-info">
                    <div class="content">
                        <div class="row loan-number-detail clearfix">
                            <div class="col-md-4">
                                <div class="title">约定年化利率</div>
                                <div class="number red">
                                    <@percentInteger>${loan.baseRate}</@percentInteger><@percentFraction>${loan.baseRate}</@percentFraction>
                                    <span>%</span>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="title">项目期限</div>
                                <div class="number">${loan.duration}<span>天</span></div>
                            </div>
                            <div class="col-md-5">
                                <div class="title">起投金额</div>
                                <div class="number">
                                    <em><@amount>${loan.minInvestAmount?string.computer}</@amount></em><span>元(体验金)</span>
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
            <div class="blank-middle"></div>
            <div class="account-info bg-w">
                <h5 class="l-title"><span id="riskTips" class="risk-tips">保守型<em></em><i class="risk-tip-content extra-rate-popup">该项目适合投资偏好类型为保守型的用户</i></span>拓天速贷提醒您：市场有风险，投资需谨慎！</h5>
                    <form action="/experience-invest" method="post" id="investForm">
                        <dl class="account-list new-text account-list-new">
                            <input type="hidden" name="loanId" value="1"/>

                            <dd class="clearfix">
                                <span class="fl">体验金余额：</span>
                                <em class="fr account-amount" data-user-balance="${experienceBalance?string.computer}">${(experienceBalance / 100)?string("0.00")} 元</em>
                            </dd>
                            <dd class="invest-amount tl" <#if loan.loanStatus == "PREHEAT">style="display: none"</#if>>
                                <span class="fl">投资金额(体验金)：</span>
                                <input type="text" name="amount" data-l-zero="deny" data-v-min="0.00" data-min-invest-amount="${loan.minInvestAmount}"
                                       placeholder="0.00" value="${(experienceBalance / 100)?string("0.00")}"
                                       class="text-input-amount fr position-width-new"/>
                            </dd>

                            <dd class="expected-interest-dd tc ">
                                <span>预计总收益：</span>
                                <span class="principal-income">0.00</span> 元
                            </dd>

                            <dd>
                                <@global.isAnonymous>
                                    <a class="btn-pay btn-normal" href="/login">马上投资</a>
                                </@global.isAnonymous>
                                <@global.isNotAnonymous>
                                    <button id="investSubmit" class="btn-pay btn-normal" type="submit">马上投资</button>
                                </@global.isNotAnonymous>

                                <div class="error-box" style="display: none"></div>
                            </dd>
                        </dl>

                    </form>

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
                <h3>项目描述</h3>
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
                收益到账后后，需要实名认证并投资方可提现 <a href="/register/account" class="key">立即认证>></a>
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
<#--风险测评-->
<div id="riskAssessment" class="pad-m popLayer" style="display: none; padding-top:50px;padding-bottom: 0">
    <div class="tc text-m">根据监管要求，出借人在出借前需进行投资偏好评估，取消则默认为保守型（可承受风险能力为最低）。是否进行评估？</div>
    <div class="tc person-info-btn" style="margin-top:40px;">
        <button id="cancelAssessment" class="btn  btn-cancel btn-close btn-close-turn-on" type="button">取消</button>&nbsp;&nbsp;&nbsp;
        <button id="confirmAssessment" class="btn btn-success btn-turn-off" type="button">确认</button>
    </div>
</div>

</@global.main>