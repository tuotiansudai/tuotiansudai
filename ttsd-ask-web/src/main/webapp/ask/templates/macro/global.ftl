<#macro main pageCss pageJavascript="" staticServer="${staticServer}" jsPath="js/" cssPath="js/" >
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <meta name="keywords" content="拓天速贷,互联网金融平台,P2P理财,拓天借贷,网络理财">

    <meta name="description" content="拓天速贷是基于互联网的金融信息服务平台,由拓天伟业(北京)资产管理有限公司旗下的拓天伟业(北京)金融信息服务有限公司运营.">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="_csrf" content="4c4754d8-9846-4aae-8aff-a0f40c267d7b"/>
    <meta name="_csrf_header" content="X-CSRF-TOKEN"/>
    <title>拓天速贷-互联网金融信息服务平台</title>
    <link href="http://localhost:8080/images/favicon.ico" rel="shortcut icon" type="image/x-icon"/>
    <#if pageCss?? && pageCss != "">
        <link rel="stylesheet" type="text/css" href="${staticServer}${cssPath}${pageCss}" charset="utf-8"/>
    </#if>
    <script>
        var _czc = _czc || [];
        _czc.push(["_trackEvent()", "1257936541"]);
    </script>
</head>
<body>
<div class="header-container">
    <div class="header-download">
        <div id="closeDownloadBox" class="icon-close img-close-tip"></div>
        <div class="img-logo-tip"></div>
        <span>APP客户端重磅来袭<br/>更便捷更安全</span>
        <a href="#" class="btn-normal fr" id="btnExperience">立即体验</a>
    </div>
    <div class="header page-width">
        <span class="fl service-time">客服电话：400-169-1188<time>（服务时间：9:00－20:00）</time></span>
        <ul class="fr">
            <li class="login-pop-app" id="iphone-app-pop">
                <a href="javascript:" onclick="cnzzPush.trackClick('13顶部导航','手机APP')">手机APP</a>
                <div id="iphone-app-img" class="img-app-pc-top"></div>
            </li>

            <li>
                <a href="/login" onclick="cnzzPush.trackClick('14顶部导航','登录')">登录</a>
            </li>
            <li>
                <a href="/register/user" onclick="cnzzPush.trackClick('15顶部导航','注册')">注册</a>
            </li>
        </ul>

    </div>
</div>
<div class="nav-container">
    <div class="nav">
        <a href="/" class="logo"></a> <i class="fa fa-navicon show-main-menu fr" id="showMainMenu"></i>
        <ul id="TopMainMenuList">
            <li><a class="active" href="/" onclick="cnzzPush.trackClick('16顶部导航','首页')">首页</a></li>
            <li><a href="/loan-list" onclick="cnzzPush.trackClick('17顶部导航','我要投资')">我要投资</a></li>
            <li><a href="/account" onclick="cnzzPush.trackClick('18顶部导航','我的账户')">我的账户</a></li>
            <li><a href="#">问答</a></li>
            <li><a href="/about/guide" onclick="cnzzPush.trackClick('19顶部导航','新手指引')">新手指引</a></li>
            <li><a href="/about/company" onclick="cnzzPush.trackClick('20顶部导航','关于我们')">关于我们</a></li>
        </ul>
    </div>
</div>
<div class="banner-box page-width"></div>
<div class="main-frame full-screen clearfix">

    <div class="borderBox tc mobile-menu">
        <a href="/askQuestion" class="btn-main want-question">我要提问</a>
        <a href="/qaAnswer" class="btn-main my-question">我的提问</a>
        <a href="/qaAnswer" class="btn-main my-answer">我的回答</a>
    </div>
    <div class="download-mobile">
        <a href="#"> <img src="${staticServer}/images/sign/downloadApp.jpg"></a>
    </div>
    <div class="question-container answer-container">
        <#nested>

        <div class="aside-frame fr">
            <div class="profile-box">
                <i class="profile"></i>
                <ul class="welcome-info">
                    <li class="username">CG007008,您好</li>
                    <li>提问有新回答 <br/>
                        <a href="questionDetail"> 点击查看</a>
                    </li>
                </ul>
                <div class="button-layer">
                    <a href="qaAnswer" class="btn">我的提问<em>(4)</em></a>
                    <a href="qaAnswer" class="btn">我的回答<em>(8)</em></a>
                </div>
                <div class="vertical-line"></div>
            </div>

            <div class="hot-questions margin-top-10 clearfix">
                <div class="qa-title">热门问题分类</div>
                <ul class="qa-list clearfix">
                    <li><a href="category" class="active">证劵</a></li>
                    <li><a href="category">银行</a></li>
                    <li><a href="category">期货</a></li>
                    <li><a href="category">P2P</a></li>
                    <li><a href="category">信托</a></li>
                    <li><a href="category">贷款</a></li>
                    <li><a href="category">基金</a></li>
                    <li><a href="category">众筹</a></li>
                    <li><a href="category">理财</a></li>
                    <li><a href="category">信用卡</a></li>
                    <li><a href="category">外汇</a></li>
                    <li><a href="category">股票</a></li>
                    <li><a href="category">其他</a></li>
                </ul>
            </div>

            <img src="${staticServer}/images/welfare.jpg" alt="新人送福利" class="margin-top-10">

        </div>
    </div>

</div>
<div class="footer-container">
    <div class="footer page-width">
        <ul>
            <li class="f-title">我要投资</li>
            <li><a href="/loan-list" onclick="cnzzPush.trackClick('47底部导航','理财产品')">投资产品</a></li>
            <li><a href="/account" onclick="cnzzPush.trackClick('48底部导航','我的帐户')">我的帐户</a></li>
            <li><a href="/about/notice" onclick="cnzzPush.trackClick('49底部导航','拓天公告')">拓天公告</a></li>
        </ul>
        <ul>
            <li class="f-title">关于我们</li>
            <li><a href="/about/company" onclick="cnzzPush.trackClick('50底部导航','公司介绍')">公司介绍</a></li>
            <li><a href="/about/assurance" onclick="cnzzPush.trackClick('51底部导航','安全保障')">安全保障</a></li>
            <li><a href="/about/team" onclick="cnzzPush.trackClick('52底部导航','团队介绍')">团队介绍</a></li>
            <li><a href="/about/refer-reward" onclick="cnzzPush.trackClick('53底部导航','推荐奖励')">推荐奖励</a></li>
            <li><a href="/about/operational">运营数据</a></li>
        </ul>
        <ul>
            <li class="f-title">帮助中心</li>
            <li><a href="/about/guide" onclick="cnzzPush.trackClick('54底部导航','新手指引')">新手指引</a></li>
            <li><a href="/about/service-fee" onclick="cnzzPush.trackClick('55底部导航','服务费用')">服务费用</a></li>
            <li><a href="/about/qa" onclick="cnzzPush.trackClick('56底部导航','常见问题')">常见问题</a></li>
        </ul>
        <ul>
            <li class="f-title">联系我们</li>
            <li>客服电话：400-169-1188（服务时间：9:00－20:00）</li>
            <li>客服邮箱：kefu@tuotiansudai.com</li>
            <li>商务合作：010-57167320 <a href="mailto:Amanda.liang@tuotiansudai.com" target="_blank"> Amanda.liang@tuotiansudai.com</a></li>
            <li>地址：北京市丰台区洋桥12号天路蓝图大厦8层</li>
        </ul>
        <ul class="last">
            <li class="tc fl">
                <i class="img-weixin-img"></i> <br/>
                扫我关注拓天速贷微信
            </li>
            <li class="tc fl">
                <i class="img-app-download"></i> <br/>
                扫我下载APP
            </li>
        </ul>
    </div>
    <div class="link-list page-width clearfix">
        <h3>友情链接</h3>
        <ul id="linkList">
        </ul>
        <div class="get-more" id="getMore">
            更多<i class="fa fa-chevron-down" aria-hidden="true"></i><i class="fa fa-chevron-up" aria-hidden="true"></i>
        </div>
    </div>
    <div class="copyright page-width">
        <div class="fl">
            <a rel="nofollow" href="http://www.itrust.org.cn/yz/pjwx.asp?wm=1335512526" onclick="cnzzPush.trackClick('57底部导航','信用企业')" target="_blank">
                <div class="img-gray img-itrust_logo"></div>
            </a>
            <a rel="nofollow" href="https://search.szfw.org/cert/l/CX20150616010363010507" onclick="cnzzPush.trackClick('58底部导航','诚信网站')" target="_blank">
                <div class="img-gray img-chengxin_logo"></div>
            </a>
            <a rel="nofollow" href="https://ss.knet.cn/verifyseal.dll?sn=e15071011011759427pzmf000000&pa=500267" onclick="cnzzPush.trackClick('59底部导航','可信网站')" target="_blank">
                <div class="img-gray img-knetSealLogo"></div>
            </a>
            <a rel="nofollow" href="https://seal.digicert.com/seals/popup/?tag=7MzpfEHK&url=tuotiansudai.com&lang=en" onclick="cnzzPush.trackClick('60底部导航','digicert')" target="_blank">
                <div class="img-gray img-digicert"></div>
            </a>
        </div>
        <div class="fr tr">
                <span>京ICP备15035567号-1 <br/>
                拓天伟业(北京)金融信息服务有限公司 版权所有</span>
        </div>
    </div>
    <div class="footer-responsive">
        <ul>
            <li><a href="/about/refer-reward">推荐奖励</a></li>
            <li><a href="/about/guide">新手指引</a></li>
            <li><a href="/about/assurance">安全保障</a></li>
        </ul>
        <p>客服电话:400-169-1188(服务时间:9:00-20:00)</p>
        <p>拓天伟业(北京)金融信息服务有限公司 版权所有</p>
    </div>
</div>

    <#if pageJavascript?? && pageJavascript?length gt 0>
    <script src="${staticServer}${jsPath}${pageJavascript}" type="text/javascript"></script>
    </#if>


</body>
</html>
</#macro>