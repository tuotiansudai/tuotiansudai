<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.loan_detail}" pageJavascript="${js.experience_loan_detail}" activeNav="我要投资" activeLeftNav="" title="新手体验项目">
<div class="loan-detail-content loan-detail-new" data-loan-progress="${loan.progress?string.computer}">

    <div class="borderBox clearfix no-border">
        <div class="loan-model">
            <div class="news-share bg-w">
                <h2 class="hd clearfix title-block new-free-tag">
                    <div class="fl title">${loan.name}</div>
                    <span class="new-free"></span>
                </h2>
                <div class="container-block loan-info">
                    <div class="content">
                        <div class="row loan-number-detail">
                            <div class="col-md-4">
                                <div class="title">预期年化收益</div>
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
                        <div class="row loan-active-detail">
                            <div class="col-md-6">
                                <span class="title">还款方式：</span>
                                到期付息,体验金收回。
                            </div>
                        </div>
                    </div> <#-- .content end tag -->
                </div> <#-- .container-block end tag -->
            </div>
            <div class="blank-middle"></div>
            <div class="account-info bg-w">
                <h5 class="l-title">拓天速贷提醒您：投资非存款，投资需谨慎！</h5>
                <#if loan.loanStatus == 'RAISING'>
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
                                       class="text-input-amount fr position-width"/>
                            </dd>

                            <dd class="expected-interest-dd tc mb-20">
                                <span>预计总收益：</span>
                                <span class="principal-income">0.00</span> 元
                            </dd>

                            <dd class="mb-20">
                                <@global.isAnonymous>
                                    <a class="btn-pay btn-normal" href="/register/user">马上投资</a>
                                </@global.isAnonymous>
                                <@global.isNotAnonymous>
                                    <button id="investSubmit" class="btn-pay btn-normal" type="button">马上投资
                                    </button>
                                </@global.isNotAnonymous>
                            </dd>
                        </dl>
                    </form>
                </#if>
                <#if ["REPAYING", "COMPLETE"]?seq_contains(loan.loanStatus)>
                    <form action="/loan-list" method="get">
                        <dl class="account-list">
                            <dd class="img-status">
                                <img src="${staticServer}/images/sign/loan/${loan.loanStatus?lower_case}.png" alt=""/>
                            </dd>
                            <dd>
                                <button class="btn-pay btn-normal" type="submit">查看其他项目</button>
                            </dd>
                        </dl>
                    </form>
                </#if>
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
                    <li>1、体验金是由拓天速贷提供给平台客户用来投资拓天体验金项目的本金，有效期为3天，50元起投。</li>
                    <li>2、拓天体验金项目仅限用户使用体验金投资，项目到期后，平台回收体验金，收益归用户所有。</li>
                    <li>3、体验金不能转出，但体验金投资产生的收益可以提现。</li>
                    <li>4、用户首次提现体验金投资所产生的收益时，需要投资其他定期项目（债权转让项目除外）累计满1000元才可以提现。</li>
                    <li>5、新注册用户可以获得6888元体验金。</li>
                    <li>6、关注并参与平台活动可获取更多体验金。</li>
                    <li>本活动规则解释权归拓天速贷所有，如有疑问请联系在线客服或拨打400-169-1188</li>
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

    <div id="freeSuccess" style="display: none;">
        <div class="success-info-tip">
            <i class="icon-tip"></i>
            <div class="detail-word">
                <h2>恭喜您体验成功！</h2> 体验收益发放后需实名认证并进行过投资后方可提现 <a href="/register/account" class="key">立即认证>></a>
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