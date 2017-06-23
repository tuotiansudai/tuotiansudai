<#import "macro/global-dev.ftl" as global>
<#assign jsName = 'loan_detail_day' >

<#assign js = {"${jsName}":"http://localhost:3008/web/js/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/web/js/${jsName}.css"}>

<@global.main pageCss="${css.loan_detail_day}" pageJavascript="${js.loan_detail_day}" activeNav="我要投资" activeLeftNav="" title="日息宝">
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
                <h3>日息宝产品说明</h3>
            </div>
            <div class="model-content">
                <dl class="intro-list">
                    <dt>1.日息宝是什么？<i></i></dt>
                    <dd>日息宝是拓天速贷推出的一款高流动性的智能投资产品。投资人通过加入日息宝服务，将其资金授权拓天速贷出借给借款人，以投资于拓天速贷平台推荐的优质借款项目。加入到日息宝的资金将由拓天速贷平台系统代为分配投资标的。资金成功投资当日开始计息，按当日预期年化利率及计息本金每日结算收益。</dd>
                </dl>
                <dl class="intro-list">
                    <dt>2.日息宝是如何计算收益的？<i></i></dt>
                    <dd>日息宝以投资人实际持有的金额进行计算，当日收益=当日实际持有金额*当日实际年化收益率/365。</dd>
                </dl>
                <dl class="intro-list">
                    <dt>3.投资日息宝的收益如何结算？<i></i></dt>
                    <dd>日息宝的收益每日结算，次日可查看前一日的收益。<br />成功转入当日，该笔资金开始计息；成功转出当日，该笔转出资金不再计算收益。<br />用户投资日息宝所产生的收益，目前均由平台自动进行复投，您可以点击日息宝详情页中的累计收益查看。</dd>
                </dl>
                <dl class="intro-list">
                    <dt>4.加入日息宝的资金投入了什么项目？<i></i></dt>
                    <dd>您投入日息宝的资金会被随机分散投资于平台新发布的散标或待转让债权。</dd>
                </dl>
                <dl class="intro-list">
                    <dt>5.转入日息宝，有哪些注意事项？<i></i></dt>
                    <dd>&emsp;(1) 每日10:00-24:00，投资人可随时购买；无转入次数限制；<br />&emsp;(2) 最小转入金额：0.01元；<br />&emsp;(3) 转入金额限制：单人累计购买金额不超过50万元（当前账户中持有总金额)。</dd>
                </dl>
                <dl class="intro-list">
                    <dt>6.退出日息宝，有哪些注意事项？<i></i></dt>
                    <dd>(1) 关于退出时间：本金在3天锁定期后，可随时转出；本金所产生的收益可随时转出；<br />
                    (2) 关于退出金额： <br />
                        &emsp;&emsp;a. 每笔申请退出金额最小为0.01元；<br />
                        &emsp;&emsp;b. 单人当日累计退出金额不可超过10万元；<br />
                        &emsp;&emsp;c.可以选择部分或全额退出； <br />
                    (3) 关于结息方式： <br />
                        &emsp;&emsp;a. 退出本金在申请退出期间仍然计息，成功退出当日起不再计息，尚未申请退出的本金将继续投资计息；<br /> 
                        &emsp;&emsp;b. 退出成功后，系统按退出本金的比例结算对应收益； <br />
                    (4) 关于退出方式： 您提交退出申请后，系统将自动以债权转让方式退出并按批次撮合交易； <br />
                    (5) 关于退出次数限制：目前转出日息宝无次数限制；<br />
                    (6) 关于退出费用：目前转出日息宝无收费项，后期可能会根据平台实际情况进行调整，具体以转出页面中的提示为准；<br />
                    (7) 转出资金到账时间：一般情况下，申请转出的资金，平台工作人员次工作日进行审核，审核成功后到账。转出成功后，用户可以在账户余额中进行查看。</dd>
                </dl>
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