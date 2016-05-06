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
            <#if linkExchangeList??>
                <#list linkExchangeList as linkExchange>
                    <li><a href="${linkExchange.linkUrl}" target="_blank" title="${linkExchange.title}">${linkExchange.title}</a></li>
                </#list>
            </#if>
        </ul>
        <div class="get-more" id="getMore">
            更多<i class="fa fa-chevron-down" aria-hidden="true"></i><i class="fa fa-chevron-up" aria-hidden="true"></i>
        </div>
    </div>
    <div class="copyright page-width">
        <div class="fl">
            <a href="http://www.itrust.org.cn/yz/pjwx.asp?wm=1335512526" onclick="cnzzPush.trackClick('57底部导航','信用企业')" target="_blank">
                <div class="img-gray img-itrust_logo"></div>
            </a>
            <a href="https://search.szfw.org/cert/l/CX20150616010363010507" onclick="cnzzPush.trackClick('58底部导航','诚信网站')" target="_blank">
                <div class="img-gray img-chengxin_logo"></div>
            </a>
            <a href="https://ss.knet.cn/verifyseal.dll?sn=e15071011011759427pzmf000000&pa=500267" onclick="cnzzPush.trackClick('59底部导航','可信网站')" target="_blank">
                <div class="img-gray img-knetSealLogo"></div>
            </a>
            <a href="https://seal.digicert.com/seals/popup/?tag=7MzpfEHK&url=tuotiansudai.com&lang=en" onclick="cnzzPush.trackClick('60底部导航','digicert')" target="_blank">
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
