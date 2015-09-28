<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv=″X-UA-Compatible″ content=″IE=edge,chrome=1″/>
    <title>抽奖页面</title>
    <link rel="stylesheet" href="/special/investlottery/css/active.css"/>
    <script src="/special/investlottery/js/jquery-1.11.3.min.js"></script>
    <script src="/special/investlottery/js/jquery-scrollable.js"></script>
    <script src="/special/investlottery/js/index.js"></script>
</head>
<body>
<div class="main">
    <div class="top"></div>
    <div class="con">
        <div class="bg-con">
            <div class="active-box">
                <div class="table-list">
                    <div class="table-box">
                        <table id="table-lottery">
                            <tbody>
                            <#list investLotterys as item>
                            <tr>
                                <td>${item.user.username}</td>
                                <td>
                                    <#if item.prizeType == 'G'>
                                    ${(item.amount/100)?string('0.00')} 元现金
                                    <#else>
                                    ${item.prizeType.desc}
                                    </#if>
                                </td>
                                <td>${item.awardTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
                <h2 class="active-hd"></h2>
                <p> 1、现金类奖品中奖后24小时之内联系客服领取；</p>
                <p> 2、大型家电类奖励，公司将保价投递，但奖励售后服务由奖励厂家负责。</p>
                <p> 3、实物类奖品中奖后，由客服确认用户投递地址，已物流及快递时间为准，实物投递仅限于中国大陆地区，不含港澳台地区，新疆、青海、西藏不包邮费，用户如需奖励，自行承担物流及快递费用。</p>
                <p> 4、用户抽奖次数只限当日有效。</p>
                <p> 5、新手投资：新手标抽奖用户只要投资新手标，即可获取一次抽奖机会，投资资金无限制。</p>
                <p> 普通投资：投资抽奖用户投资除新手标以外的任何标的物均可抽奖用户单笔投资500元可获取一次，用户单笔投资1500元以上可获取三次机会（单日最高3次）。</p>
            </div>
            <div class="item-box">
                <div class="tab">
                    <input type="hidden" id="currentLoginName" value="${currentLoginName!}"/>
                    <span class="news current">新手投资抽奖x <i id="num">${noviceRemainCount}</i></span>
                    <span class="news">普通投资抽奖x <i id="num_1">${normalRemainCount}</i></span>
                </div>
                <div class="con-box">
                    <div class="box" style="display: block;">
                        <h3>新手投资者抽奖区 赶紧试试手气吧！</h3>

                        <div class="draw-box">
                            <ul class="list">
                                <li class="draw-1">现金</li>
                                <li class="draw-2">100元</br>礼品</li>
                                <li class="draw-3">1000元</br>礼品</li>
                                <li class="draw-4">代金券</br>一张</li>
                                <li class="draw-5">礼品卷</br>一张</li>
                                <li class="draw-6 current">300元</br>礼品</li>
                                <li class="draw-7 ">现金</li>
                                <li class="draw-8">100元</br>礼品</li>
                                <li class="draw-9">礼品卷</br>一张</li>
                                <li class="draw-10">300元</br>礼品</li>
                                <li class="draw-11">代金券</br>一张</li>
                                <li class="draw-12">500元</br>礼品</li>
                            </ul>
                            <button type="button" class="btn-draw" id="novice">抽奖</button>
                        </div>
                    </div>
                    <div class="box">
                        <h3>普通投资者抽奖区 赶紧试试手气吧！</h3>

                        <div class="draw-box">
                            <ul class="list">
                                <li class="draw-1">现金</li>
                                <li class="draw-2">100元</br>礼品</li>
                                <li class="draw-3">1000元</br>礼品</li>
                                <li class="draw-4">代金券</br>一张</li>
                                <li class="draw-5">礼品卷</br>一张</li>
                                <li class="draw-6 current">300元</br>礼品</li>
                                <li class="draw-7 ">现金</li>
                                <li class="draw-8">100元</br>礼品</li>
                                <li class="draw-9">礼品卷</br>一张</li>
                                <li class="draw-10">300元</br>礼品</li>
                                <li class="draw-11">代金券</br>一张</li>
                                <li class="draw-12">500元</br>礼品</li>
                            </ul>
                            <button type="button" class="btn-draw" id="normal">抽奖</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="bottom">
        <a href="/" class="btn-back"></a>
    </div>
</div>


<div class="layer-box">
    <div class="box bg-1">
        <span class="close"></span>
        <div class="title">恭喜您抽中 <span></span>礼品</div>
        <div class="txtv">¥<span></span></div>
    </div>
    <div class="box bg-2">
        <span class="close"></span>
        <div class="title">恭喜您获得 <span></span>现金</div>
        <div class="txtv">¥<span></span></div>
    </div>
</div>

</body>
</html>