<!DOCTYPE html>
<html>
<#import "macro/global.ftl" as global>
<@global.head title="账户总览" pageCss="${css.global}"></@global.head>
<body>
<#include "header.ftl" />
<div class="main-frame account-overview">
    <aside class="menu-box fl">
        <ul class="menu-list">
            <li><a href="javascript:" class="active">账户总览</a></li>
            <li><a href="javascript:">投资记录</a></li>
            <li><a href="javascript:">债权转让</a></li>
            <li><a href="javascript:">资金管理</a></li>
            <li><a href="javascript:">个人资产</a></li>
            <li><a href="javascript:" >自动投标</a></li>
            <li><a href="javascript:">积分红包</a></li>
            <li><a href="javascript:">推荐管理</a></li>
        </ul>
    </aside>
    <div class="content-container fr auto-height">
        <div class="bRadiusBox spad bgWhite">
            <img src="/images/sign/profile.jpg" class="fl accountImg" >
            <div class="profileBox">
                <span><em>您好：dongshao</em> 吃完午饭小憩一会，为下午补充能量！</span>
                <ul class="proList">
                    <li class="fl"><a class="fa fa-cc-mastercard fa-fw"></a></li>
                    <li class="fl"><a class="fa fa-mobile fa-fw"></a></li>
                    <li class="fl"><a class="fa fa-envelope-o fa-fw"></a></li>
                    <li class="fr"><button class="btn-normal">充值</button> </li>
                    <li class="fr"><button class="btn-primary">体现</button></li>
                </ul>
            </div>

        </div>
        <div class="clear-blank"></div>
        <div class="AssetsBox bgWhite">
            <div class="AssetsReport bRadiusBox fl">
                <h3>资产总额：<span>500.00元</span></h3>
                <div id="ReportShow" style="width:100%; height:115px; "></div>
            </div>
            <div class="AssetsDetail bRadiusBox fr">
                <ul class="DetailList">
                    <li><b>我的余额：</b><span>0.00</span>元</li>
                    <li><b>累计收益：</b><span>0.00</span>元</li>
                    <li><b>待收本金：</b><span>0.00</span>元</li>
                    <li><b>待收利息：</b><span>0.00</span>元</li>
                    <li><b>已收利息：</b><span>0.00</span>元</li>
                    <li><b>冻结金额：</b><span>0.00</span>元</li>
                </ul>
            </div>
        </div>
    <div class="clear-blank"></div>
        <div class="LastMonth bRadiusBox bgWhite">

            <table class="table table-striped">
                <caption>最近7天还款总额：￥0.00元 <a href="#" class="fr">更多...</a> </caption>
                <thead>
                <tr>
                <th>项目名称</th>
                <th>年利率</th>
                <th>贷款周期</th>
                <th>项目周期</th>
                <th>预计还款</th>
                <th>还款日期</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="clear-blank"></div>
        <div class="tMonthPayment bRadiusBox bgWhite" id="tMonthBox">

            <ul class="PaymentSwitch">
                <li class="current"><a href="javascript:void(0);"> 本月已收回款</a></li>
                <li><a href="javascript:void(0);">本月待收回款</a></li>
            </ul>
            <table class="table table-striped">
                <caption>本月已收回款总额：￥6000.00元 <a href="#" class="fr">更多...</a> </caption>
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th>年利率</th>
                    <th>贷款周期</th>
                    <th>项目周期</th>
                    <th>预计还款</th>
                    <th>还款日期</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="6" class="tc">2015年5月应收本息 56780元</td>
                </tr>
                </tfoot>
            </table>
            <table class="table table-striped">
                <caption>本月待收回款总额：￥7000.00元 <a href="#" class="fr">更多...</a> </caption>
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th>年利率</th>
                    <th>贷款周期</th>
                    <th>项目周期</th>
                    <th>预计还款</th>
                    <th>还款日期</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td> 第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td>第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                <tr>
                    <td>生意周转个人个人个人....... </td>
                    <td>13.50 % </td>
                    <td>3 个月 </td>
                    <td>第1期/22期  </td>
                    <td>2498.76元 </td>
                    <td>10月09日 </td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="6" class="tc">2015年5月应收本息 56780元</td>
                </tr>
                </tfoot>
            </table>
        </div>
        <div class="clear-blank"></div>
        <div class="newProjects bRadiusBox bgWhite">
        <table class="table">
            <caption>最新投资项目 <a href="#" class="fr">更多...</a> </caption>
            <thead>
            <tr>
                <th>交易时间</th>
                <th>交易详情</th>
                <th>交易状态</th>
                <th>下次回款</th>
                <th>我的投资</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>2015-06-06 </td>
                <td><a href="#">个人经营借款6000元</a></td>
                <td>投资成功</td>
                <td>2015-09-06/2000元</td>
                <td>￥2498.76元 </td>
                <td><a href="#">合同</a></td>
            </tr>
            <tr>
                <td>2015-06-06 </td>
                <td><a href="#">个人经营借款6000元</a></td>
                <td>投资成功</td>
                <td>2015-09-06/2000元</td>
                <td>￥2498.76元 </td>
                <td><a href="#">合同</a></td>
            </tr>
            <tr>
                <td>2015-06-06 </td>
                <td><a href="#">个人经营借款6000元</a></td>
                <td>投资成功</td>
                <td>2015-09-06/2000元</td>
                <td>￥2498.76元 </td>
                <td><a href="#">合同</a></td>
            </tr>
            <tr>
                <td>2015-06-06 </td>
                <td><a href="#">个人经营借款6000元</a></td>
                <td>投资成功</td>
                <td>2015-09-06/2000元</td>
                <td>￥2498.76元 </td>
                <td><a href="#">合同</a></td>
            </tr>
            </tbody>
        </table>
        </div>
    </div>
</div>


<#include "footer.ftl">
<@global.javascript pageJavascript="${js.account_overview}">
</@global.javascript>

</body>
</html>