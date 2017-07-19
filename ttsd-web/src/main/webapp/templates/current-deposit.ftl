<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.day_turn_in}" pageJavascript="${js.day_turn_in}" activeNav="我要投资" activeLeftNav="" title="日息宝">
<div class="loan-detail-content loan-detail-new" id="currentDepositContent">
    <div class="borderBox clearfix no-border">
        <div class="loan-model">
            <div class="news-share bg-w">
                <h2 class="hd clearfix title-block clearfix">
                    <div class="fl title active"><a href="/current/deposit">日息宝转入</a></div>
                    <div class="fl title"><a href="/day-turn-out.ftl">日息宝转出</a></div>
                </h2>
                <div class="container-block loan-info">
                    <div class="content">
                        <form action="/current/deposit" method="post">
                            <div class="fotm-item">
                                <p>账户余额（元）：<span>${(balance / 100)?string("0.00")}</span></p>
                            </div>

                            <div class="fotm-item">
                                <p>今日最多可购买（元）：<span>${(personalMaxDeposit / 100)?string("0.00")}</span></p>
                            </div>

                            <div class="fotm-item">
                                <input type="text" class="int-item amount-input" placeholder="请输入金额"
                                       data-l-zero="deny"
                                       data-v-min="0.00"
                                       data-error-message="${errorMessage!}"
                                       data-deposit-amount="${depositAmount!(0)}"
                                       data-balance="${balance?c}"
                                       data-max-deposit-amount="100000">
                                <input type="hidden" name="amount">
                            </div>

                            <div class="fotm-item">
                                <p class="tip-info">当日计息，明日可查看收益</p>
                            </div>

                            <div class="fotm-item">
                                <@global.isAnonymous>
                                    <a href="/login?redirect=/current/deposit">确认转入</a>
                                </@global.isAnonymous>

                                <@global.isNotAnonymous>
                                    <input type="submit" class="submit-item deposit-submit" value="确认转入"/>
                                </@global.isNotAnonymous>
                            </div>

                            <div class="fotm-item">
                                <input type="checkbox" name="agreement" checked/>
                                <label for="agreement" class="agreement-text">我已阅读并同意《用户服务协议》、《产品服务协议》</label>
                            </div class="fotm-item">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>
                    </div>
                </div>
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
                            <span>拓天速贷平台项目都是抵押债权，每笔债权都对应着相应的抵押物。如果投资过程中发生了资金风险，抵押物资将会被处理来为用户的资金提供保障。</span>
                        </p>
                    </li>
                    <li>
                        <p class="intro-title">交易保障</p>

                        <p class="intro-icon icon-two"></p>

                        <p class="intro-text">
                            <span>拓天速贷接入了联动优势电子商务有限公司的资金托管系统。交易过程中的充值、投资、提现都在第三方支付平台进行，保证了资金流转的透明和安全。</span>
                        </p>
                    </li>
                    <li>
                        <p class="intro-title">信息保障</p>

                        <p class="intro-icon icon-three"></p>

                        <p class="intro-text">
                            <span>采用银行级别的数据传输加密技术，保障用户的信息安全。在同城和异地均建立灾备设备，避免因自然灾害导致用户信息的损失。</span>
                        </p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
</@global.main>