<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="绑定银行卡" pageCss="${css.bind_card}">
</@global.head>
<body>
<#include "header.ftl" />
<div class="main">

    <ul class="email-nav">
        <li><a href="javascript:;">账户总览</a></li>
        <li><a href="javascript:;">投资记录</a></li>
        <li><a href="javascript:;">债权转让</a></li>
        <li><a href="javascript:;">资金管理</a></li>
        <li><a href="javascript:;">个人资产</a></li>
        <li><a href="javascript:;">自动投标</a></li>
        <li><a href="javascript:;">积分红包</a></li>
        <li><a href="javascript:;">推荐管理</a></li>
    </ul>


    <div class="bind-card">
        <h2 class="hd-bind-card"><span>绑定银行卡</span></h2>

        <div class="card-list">
        <#--用户尚未绑定银行卡快捷支付-->
            <form action="" style="display: block" method="post" target="_blank">
                <div class="item-block">
                    <span class="name">真实姓名：${loginName}</span>
                </div>
                <div class="item-block">
                    <ol class="select-bank">
                        <p>选中银行:</p>
                        <li>
                            <input data-name="CMB" type="radio" name="bank" id="bank-zs" checked="checked">
                            <label for="bank-zs"><img src="/images/recharge/bank-zs.jpg" alt=""></label>
                        </li>
                        <li>
                            <input data-name="ICBC" type="radio" name="bank" id="bank-gs">
                            <label for="bank-gs"><img src="/images/recharge/bank-gs.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CMBC" type="radio" name="bank" id="bank-ms">
                            <label for="bank-ms"><img src="/images/recharge/bank-ms.jpg" alt=""> </label>
                        </li>
                        <li class="m-right-0">
                            <input data-name="CCB" type="radio" name="bank" id="bank-js">
                            <label for="bank-js"><img src="/images/recharge/bank-js.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="BOC" type="radio" name="bank" id="bank-zg">
                            <label for="bank-zg"><img src="/images/recharge/bank-zg.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="SPDB" type="radio" name="bank" id="bank-pf">
                            <label for="bank-pf"><img src="/images/recharge/bank-pf.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CIB" type="radio" name="bank" id="bank-xy">
                            <label for="bank-xy"><img src="/images/recharge/bank-xy.jpg" alt=""> </label>
                        </li>
                        <li class="m-right-0">
                            <input data-name="COMM" type="radio" name="bank" id="bank-jt">
                            <label for="bank-jt"><img src="/images/recharge/bank-jt.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CEB" type="radio" name="bank" id="bank-gd">
                            <label for="bank-gd"><img src="/images/recharge/bank-gd.jpg" alt=""> </label>
                        </li>
                        <li><input data-name="PSBC" type="radio" name="bank" id="bank-yz">
                            <label for="bank-yz"><img src="/images/recharge/bank-yz.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="HXB" type="radio" name="bank" id="bank-hx">
                            <label for="bank-hx"><img src="/images/recharge/bank-hx.jpg" alt=""> </label>
                        </li>
                        <li class="m-right-0">
                            <input data-name="BJBANK" type="radio" name="bank" id="bank-bj">
                            <label for="bank-bj"><img src="/images/recharge/bank-bj.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="CITIC" type="radio" name="bank" id="bank-zx">
                            <label for="bank-zx"><img src="/images/recharge/bank-zx.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="WZCB" type="radio" name="bank" id="bank-wz"><label for="bank-wz"><img
                                src="/images/recharge/bank-wz.jpg" alt=""> </label></li>
                        <li>
                            <input data-name="SHRCB" type="radio" name="bank" id="bank-s">
                            <label for="bank-s"><img src="/images/recharge/bank-sh.jpg" alt=""> </label>
                        </li>
                        <li class="m-right-0">
                            <input data-name="ABC" type="radio" name="bank-n" id="bank-ny">
                            <label for="bank-ny"><img src="../images/recharge/bank-ny.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="GDB" type="radio" name="bank" id="bank-gf">
                            <label for="bank-gf"><img src="/images/recharge/bank-gf.jpg" alt=""> </label>
                        </li>
                        <li>
                            <input data-name="BEA" type="radio" name="bank" id="bank-dy">
                            <label for="bank-dy"><img src="/images/recharge/bank-dy.jpg" alt=""> </label>
                        </li>
                    </ol>
                </div>
                <div class="item-block">
                    <span class="name">银行卡：</span>
                    <input name="cardNumber" class="input-bankcard" type="text" placeholder="输入卡号" value=""/>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </div>
                <div class="item-block">
                    <button class="btn-ok grey" type="submit" disabled="disabled">确认绑定</button>
                </div>
            </form>
        <#--用户尚未绑定银行卡快捷支付-->

        <#--未开通快捷支付-->
            <div class="card-box" style="display:none ;">
                <h4 class="hd-card">
                    <span class="logo-card"><img src="${requestContext.getContextPath()}/images/bindcard/logo-ICBC.jpg"
                                                 alt=""/></span>
                    <span class="user">皮东</span>
                </h4>

                <div class="card-num">6212**** **** ***4123</div>
                <div class="options">
                    <a class="cart-edit" href="">修改</a>
                    <a class="open-fast-pay" href="">开通快捷支付</a>
                </div>
            </div>
        <#--未开通快捷支付-->
        </div>
        <div class="tips">
            <h3>温馨提示</h3>

            <p>1、不支持提现至信用卡账户。</p>

            <p>2、由于银行卡保护机制均由联动优势提供，故您的银行卡将通过拓天平台绑定到联动优势平台上进行第三方托管。</p>

            <p>3、如果您的借记卡是中国工商银行，中国农业银行，中国建设银行，中国银行，光大银行，兴业银行，深圳发展银行七家之一，
                才可开通快捷支付。
            </p>

            <p>4、当您的账户余额为零的时候，系统才会支持您更换银行卡操作。
            </p>

            <p>5、如果您已经开通快捷支付，系统不再支持您更换银行卡。</p>
        </div>
    </div>
</div>



<div class="ecope-overlay" style=""></div>
<div class="ecope-dialog">
    <div class="dg_wrapper dialog-chongzhi">
        <div class="hd">
            <h3>绑卡成功与失败</h3>
        </div>
        <div class="bd">
            <p>请在新打开的连接页面查看后后选择：</p>

            <div class="ret">
                <p>绑卡成功：<a class="g-btn g-btn-medium-major tongji"
                           href="" data-category="确认成功"
                           data-label="recharge">确认成功</a></p>

                <p>绑卡失败：<a href="#" class="g-btn g-btn-medium-minor tongji js-close-btn" data-category="重新绑卡"
                           data-label="recharge">重新绑卡</a>&nbsp;&nbsp;<span class="help">查看&nbsp;<a
                        href="" class="tongji" target="_blank" data-category="查看帮助中心"
                        data-label="recharge">帮助中心</a></span></p>

                <p style="font-size:14px">遇到问题请拨打我们的客服热线：400-169-1188（工作日 9:00-22:00）</p>
            </div>
        </div>
        <a href="javascript:void(0)" class="js-close close tongji" data-category="关闭弹层" data-label="recharge"></a>
    </div>
</div>

<#include "footer.ftl">
<@global.javascript pageJavascript="${js.bind_card}">
</@global.javascript>
</body>
</html>