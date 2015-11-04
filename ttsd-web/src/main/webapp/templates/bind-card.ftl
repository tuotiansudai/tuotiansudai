<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="${js.bind_card}" activeNav="我的账户" activeLeftNav="个人资料" title="绑定银行卡">
<div class="bind-card">
    <h4 class="column-title"><em class="tc">绑定银行卡</em></h4>
    <div class="card-list">
        <#if bindCardStatus == "unbindCard">
        <#--用户尚未绑定银行卡快捷支付-->
            <form action="" style="display: block" method="post" target="_blank">
                <div class="item-block">
                    <span class="name">真实姓名：${userName}</span>
                </div>
                <div class="item-block">
                    <ol class="select-bank">
                        <p>选择银行:</p>
                        <li>
                            <input data-name="CMB" type="radio" name="bank" id="bank-zs" checked="checked">
                            <label for="bank-zs"><img src="/images/bank/CMB.jpg" alt=""></label>
                        </li>
                        <li>
                            <input data-name="ICBC" type="radio" name="bank" id="bank-gs">
                            <label for="bank-gs"><img src="/images/bank/ICBC.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CMBC" type="radio" name="bank" id="bank-ms">
                            <label for="bank-ms"><img src="/images/bank/CMBC.jpg" alt=""> </label>
                        </li>
                        <li class="m-right-0">
                            <input data-name="CCB" type="radio" name="bank" id="bank-js">
                            <label for="bank-js"><img src="/images/bank/CCB.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="BOC" type="radio" name="bank" id="bank-zg">
                            <label for="bank-zg"><img src="/images/bank/BOC.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="SPDB" type="radio" name="bank" id="bank-pf">
                            <label for="bank-pf"><img src="/images/bank/SPDB.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CIB" type="radio" name="bank" id="bank-xy">
                            <label for="bank-xy"><img src="/images/bank/CIB.jpg" alt=""> </label>
                        </li>
                        <li class="m-right-0">
                            <input data-name="COMM" type="radio" name="bank" id="bank-jt">
                            <label for="bank-jt"><img src="/images/bank/COMM.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CEB" type="radio" name="bank" id="bank-gd">
                            <label for="bank-gd"><img src="/images/bank/CEB.jpg" alt=""> </label>
                        </li>
                        <li><input data-name="PSBC" type="radio" name="bank" id="bank-yz">
                            <label for="bank-yz"><img src="/images/bank/PSBC.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="HXB" type="radio" name="bank" id="bank-hx">
                            <label for="bank-hx"><img src="/images/bank/HXB.jpg" alt=""> </label>
                        </li>
                        <li class="m-right-0">
                            <input data-name="BJBANK" type="radio" name="bank" id="bank-bj">
                            <label for="bank-bj"><img src="/images/bank/BJBANK.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CITIC" type="radio" name="bank" id="bank-zx">
                            <label for="bank-zx"><img src="/images/bank/CITIC.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="WZCB" type="radio" name="bank" id="bank-wz"><label for="bank-wz"><img
                                src="/images/bank/WZCB.jpg" alt=""> </label></li>
                        <li>
                            <input data-name="SHRCB" type="radio" name="bank" id="bank-s">
                            <label for="bank-s"><img src="/images/bank/SHRCB.jpg" alt=""> </label>
                        </li>
                        <li class="m-right-0">
                            <input data-name="ABC" type="radio" name="bank-n" id="bank-ny">
                            <label for="bank-ny"><img src="../images/bank/ABC.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="GDB" type="radio" name="bank" id="bank-gf">
                            <label for="bank-gf"><img src="/images/bank/GDB.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="BEA" type="radio" name="bank" id="bank-dy">
                            <label for="bank-dy"><img src="/images/bank/BEA.jpg" alt=""> </label>
                        </li>
                    </ol>
                </div>
                <div class="item-block">
                    <span class="name">银行卡：</span>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input name="cardNumber" class="input-bankcard" type="text" placeholder="输入卡号" value=""/>
                </div>
                    <input type="submit" class="bind-card-submit grey" disabled="disabled" value="确认绑定"/>
            </form>
        <#--用户尚未绑定银行卡快捷支付-->
        </#if>
        <#if bindCardStatus == "commonBindCard" ||  bindCardStatus == "specialBindCard">
        <#--未开通快捷支付-->
            <div class="card-box">
                <form class="open-fast-pay-form" action="/agreement" method="post" target="_blank">
                    <h4 class="hd-card">
                        <span class="logo-card"><img src="${staticServer}/images/bindcard/logo-${bankCode}.png" /></span>
                        <span class="user">${userName}</span>
                    </h4>
                    <div class="card-num">${cardNumber}</div>
                    <div class="options">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <a class="card-edit" href="">修改</a>
                        <#if bindCardStatus == "specialBindCard">
                            <input type="hidden" name="fastPay" value="true"/>
                            <a class="open-fast-pay" href="javascript:">开通快捷支付</a>
                        </#if>
                    </div>
                </form>
            </div>
        <#--未开通快捷支付-->

        </#if>
    </div>
    <div class="tips">
        <b>温馨提示:</b><br/>
        1、不支持提现至信用卡账户。<br/>
        2、由于银行卡保护机制均由联动优势提供，故您的银行卡将通过拓天平台绑定到联动优势平台上进行第三方托管。<br/>
        3、如果您的借记卡是中国工商银行、中国农业银行、中国建设银行、华夏银行、中国银行、中国邮政储蓄银行、浦发银行、交通银行、民生银行、广发银行、中信银行、兴业银行、光大银行、招商银行和平安银行，方可开通快捷支付。<br/>
        4、当您的账户余额为零的时候，系统才会支持您更换银行卡操作。<br/>
        5、如果您已经开通快捷支付，系统不再支持您更换银行卡。<br/>
    </div>
</div>

<div id="pop-bind-card" class="pad-m recharge-plat" style="display: none;">
    <p>请在新打开的联动优势页面绑卡完成后选择：</p>
    <div class="ret">
        <p>充值成功：<a href="/account" class="btn-success"  data-category="确认成功" data-label="recharge">确认成功</a></p>
        <p>充值失败：<a href="" class="btn-normal" data-category="重新绑卡" data-label="recharge">重新绑卡</a>
            <span class="help">查看<a href="#"  target="_blank" data-category="查看帮助中心" data-label="recharge">帮助中心</a></span>
        </p>
        <p>遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-22:00）</p>
    </div>
</div>

<div id="pop-fast-pay" class="pad-m recharge-plat" style="display: none;">
    <p>请在新打开的联动优势页面绑卡完成后选择：</p>
    <div class="ret">
        <p>充值成功：<a href="/account" class="btn-success"  data-category="确认成功" data-label="recharge">确认成功</a></p>
        <p>充值失败：<a href="" class="btn-normal" data-category="重新开通" data-label="recharge">重新开通</a>
            <span class="help">查看<a href="#"  target="_blank" data-category="查看帮助中心" data-label="recharge">帮助中心</a></span>
        </p>
        <p>遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-22:00）</p>
    </div>
</div>
</@global.main>