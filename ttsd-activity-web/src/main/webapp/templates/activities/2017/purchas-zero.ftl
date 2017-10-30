<#import "../../macro/global.ftl" as global>

<@global.main pageCss="${css.purchas_zero_2017}" pageJavascript="${js.purchas_zero_2017}" activeNav="" activeLeftNav="" title="好货0元购_活动中心_拓天速贷" keywords="拓天速贷,0元购,商品奖励,专享项目" description="拓天速贷好货0元购活动,活动期间用户点选心仪好货,通过活动页面'立即白拿'按钮进入投资页面,投资带有'0元购'标签的专享项目达到该商品对应投资额,即可0元白拿商品.">

<div class="banner-slide" id="bannerSlide">

</div>
<div id="purchas_zero" class="shopping-zero-frame">
    <div class="activity-process clearfix page-width">
        <dl class="title">
            <dt></dt>
            <dd>活动期间，用户可点选心仪好货，并通过活动页面“立即白拿”按钮进入投资页面，投资带有“0元购”标签的专享项目达到该商品对应的投资额，即可0元获赠该商品。<strong> 注：用户参与活动时需严格按照此兑换流程进行操作。</strong></dd>
        </dl>
        <div class="process-content clearfix">
            <dl>
                <dt class="process-img" id="goodsBetter">

                </dt>
                <dd>选择好货</dd>
            </dl>
            <span class="arrow-right"></span>
            <dl>
                <dt class="process-img" id="goodsInvest">

                </dt>
                <dd class="long-font">投资一定金额</dd>
            </dl>
            <span class="arrow-right"></span>
            <dl>
                <dt class="process-img" id="goodsGive">

                </dt>
               <dd>白拿商品</dd>
            </dl>
            <span class="arrow-right"></span>
            <dl>
                <dt class="process-img" id="goodsInterest">

                </dt>
                <dd class="long-font">到期还本付息</dd>
            </dl>
        </div>
    </div>

    <i class="date" data-starttime="${activityStartTime!}" data-endtime="${activityEndTime!}"></i>

    <div class="goods-select-wrap">
        <div class="goods-select">
            <div class="goods-title"></div>
            <div class="goods-list">
                <div class="page-width">
                    <ul class="clearfix">
                        <li>
                            <#if Deerma_humidifier < 1 >
                                <div class="sold-wrap">
                                    <span class="sold"></span>
                                </div>
                            </#if>
                            <a href="/activity/zero-shopping/article?zeroShoppingPrize=Deerma_humidifier<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>">
                                <div class="img fl" id="humidifier"></div>
                                <div class="fr">
                                    <div class="goods-name">德尔玛（Deerma）加湿器5L大容量</div>
                                    <div class="goods-features">温润你的生活</div>
                                    <div class="goods-price"><del>市场价：119元</del></div>
                                    <div class="goods-income">额外收益：约42元</div>
                                    <a href="/activity/zero-shopping/article?zeroShoppingPrize=Deerma_humidifier<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>" class="invest-btn">投资1600元可得</a>
                                </div>
                            </a>
                        </li>
                        <li>
                            <#if Trolley_case < 1 >
                                <div class="sold-wrap">
                                    <span class="sold"></span>
                                </div>
                            </#if>
                            <a href="/activity/zero-shopping/article?zeroShoppingPrize=Trolley_case<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>">
                                <div class="img fl" id="barBox"></div>
                                <div class="fr">
                                    <div class="goods-name">90分商旅两用拉杆箱</div>
                                    <div class="goods-features">刚柔并济 悠享自在旅程</div>
                                    <div class="goods-price"><del>市场价：349元</del></div>
                                    <div class="goods-income">额外收益：约127元</div>
                                    <a href="/activity/zero-shopping/article?zeroShoppingPrize=Trolley_case<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>" class="invest-btn">投资4800元可得</a>
                                </div>
                            </a>
                        </li>
                        <li>
                            <#if Philips_Shaver < 1>
                                <div class="sold-wrap">
                                    <span class="sold"></span>
                                </div>
                            </#if>
                            <a href="/activity/zero-shopping/article?zeroShoppingPrize=Philips_Shaver<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>">
                                <div class="img fl" id="razor"></div>
                                <div class="fr">
                                    <div class="goods-name">飞利浦（PHILIPS）电动剃须刀</div>
                                    <div class="goods-features">多效理容 更自信有型</div>
                                    <div class="goods-price"><del>市场价：649元</del></div>
                                    <div class="goods-income">额外收益：约 <em></em> 238元</div>
                                    <a href="/activity/zero-shopping/article?zeroShoppingPrize=Philips_Shaver<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>" class="invest-btn">投资9000元可得</a>
                                </div>
                            </a>
                        </li>
                        <li>
                            <#if SK_II < 1>
                                <div class="sold-wrap">
                                    <span class="sold"></span>
                                </div>
                            </#if>
                            <a href="/activity/zero-shopping/article?zeroShoppingPrize=SK_II<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>">
                                <div class="img fl" id="cosmetics"></div>
                                <div class="fr">
                                    <div class="goods-name">SK-II"神仙水"晶透修护礼
                                        盒</div>
                                    <div class="goods-features">神仙美肌 剔透改变</div>
                                    <div class="goods-price"><del>市场价：1370 元</del></div>
                                    <div class="goods-income">额外收益：约502元</div>
                                    <a href="/activity/zero-shopping/article?zeroShoppingPrize=SK_II<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>" class="invest-btn">投资19000元可得</a>
                                </div>
                            </a>
                        </li>
                        <li>
                            <#if XiaoMi_5X < 1 >
                                <div class="sold-wrap">
                                    <span class="sold"></span>
                                </div>
                            </#if>
                            <a href="/activity/zero-shopping/article?zeroShoppingPrize=XiaoMi_5X<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>">
                                <div class="img fl" id="phone"></div>
                                <div class="fr">
                                    <div class="goods-name">小米5X 4GB+64GB</div>
                                    <div class="goods-features">变焦双摄  拍人更美</div>
                                    <div class="goods-price"><del>市场价：1499元</del></div>
                                    <div class="goods-income">额外收益：约555元</div>
                                    <a href="/activity/zero-shopping/article?zeroShoppingPrize=XiaoMi_5X<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>" class="invest-btn">投资21000元可得</a>
                                </div>
                            </a>
                        </li>
                        <li>
                            <#if XiaPu_Television < 1>
                                <div class="sold-wrap">
                                    <span class="sold"></span>
                                </div>
                            </#if>
                            <a href="/activity/zero-shopping/article?zeroShoppingPrize=XiaPu_Television<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>">
                                <div class="img fl" id="video"></div>
                                <div class="fr">
                                    <div class="goods-name">夏普45英寸智能液晶电视</div>
                                    <div class="goods-features">定义全新的视界</div>
                                    <div class="goods-price"><del>市场价：2099元</del></div>
                                    <div class="goods-income">额外收益：约766元</div>
                                    <a href="/activity/zero-shopping/article?zeroShoppingPrize=XiaPu_Television<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>" class="invest-btn">投资29000元可得</a>
                                </div>
                            </a>
                        </li>
                        <li>
                            <#if Philips_Purifier < 1>
                                <div class="sold-wrap">
                                    <span class="sold"></span>
                                </div>
                            </#if>
                            <a href="/activity/zero-shopping/article?zeroShoppingPrize=Philips_Purifier<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>">
                                <div class="img fl" id="purifier"></div>
                                <div class="fr">
                                    <div class="goods-name">飞利浦空气净化器</div>
                                    <div class="goods-features">健康新家 持久享受</div>
                                    <div class="goods-price"><del>市场价：2799元</del></div>
                                    <div class="goods-income">额外收益：约1030元</div>
                                    <a href="/activity/zero-shopping/article?zeroShoppingPrize=Philips_Purifier<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>" class="invest-btn">投资39000元可得</a>
                                </div>
                            </a>
                        </li>
                        <li>
                            <#if Sony_Camera < 1 >
                                <div class="sold-wrap">
                                    <span class="sold"></span>
                                </div>
                            </#if>
                            <a href="/activity/zero-shopping/article?zeroShoppingPrize=Sony_Camera<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>">
                                <div class="img fl" id="camara"></div>
                                <div class="fr">
                                    <div class="goods-name">索尼DSC-RX100 M3 黑卡
                                        相机</div>
                                    <div class="goods-features">2010万有效像素</div>
                                    <div class="goods-price"><del>市场价：3899元</del></div>
                                    <div class="goods-income">额外收益：约1426元</div>
                                    <a href="/activity/zero-shopping/article?zeroShoppingPrize=Sony_Camera<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>" class="invest-btn">投资54000元可得</a>
                                </div>
                            </a>
                        </li>
                        <li>
                            <#if Apple_MacBook < 1 >
                                <div class="sold-wrap">
                                    <span class="sold"></span>
                                </div>
                            </#if>
                            <a href="/activity/zero-shopping/article?zeroShoppingPrize=Apple_MacBook<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>">
                                <div class="img fl" id="macAir">
                                    <div class="limitation">限量</div>
                                </div>
                                <div class="fr">
                                    <div class="goods-name">MacBook Air 13.3英寸 8GB内存/128GB闪存</div>
                                    <div class="goods-features">纤巧轻薄 性能强劲</div>
                                    <div class="goods-price"><del>市场价：6588元</del></div>
                                    <div class="goods-income">额外收益：约2377元</div>
                                    <a href="/activity/zero-shopping/article?zeroShoppingPrize=Apple_MacBook<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>" class="invest-btn">投资90000元可得</a>
                                </div>
                            </a>
                        </li>
                        <li>
                            <#if Iphone_X < 1 >
                                <div class="sold-wrap">
                                    <span class="sold"></span>
                                </div>
                            </#if>
                            <a href="/activity/zero-shopping/article?zeroShoppingPrize=Iphone_X<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>">
                                <div class="img fl" id="iphonex">
                                    <div class="limitation">限量</div>
                                </div>
                                <div class="fr">
                                    <div class="goods-name">iPhone X 256GB</div>
                                    <div class="goods-features">全面屏 全面绽放</div>
                                    <div class="goods-price"><del>市场价：9688元</del></div>
                                    <div class="goods-income">额外收益：约3565元</div>
                                    <a href="/activity/zero-shopping/article?zeroShoppingPrize=Iphone_X<#if isAppSource>&source=app&token=${token!}&appVersion=${appVersion!}</#if>" class="invest-btn">投资135000元可得</a>
                                </div>
                            </a>
                        </li>
                    </ul>
                </div>

            </div>
        </div>
    </div>
    <div class="kindly-tips page-width">
        <div class="tips-title"></div>
        <div class="tips-content">
            <dl>
                <dd>1、用户在点选心仪商品后，需通过活动页面的“立即白拿”按钮进行投资，方可成功兑换奖励；</dd>
                <dd>2、用户在每次点选心仪商品后，需按该商品指定的投资额进行投资，如超出指定商品所需投资额，则超出部分的投资额不予兑换更多奖励。如欲兑换多项商品，每次兑换时均需在活动页面点选该商品，并通过活动页面中“立即白拿”按钮进行投资；</dd>
                <dd>3、如用户活动期间累计投资额未达到所选商品所需的投资额，则无法兑换奖励；</dd>
                <dd>4、每名用户在活动期间兑换同种商品最多仅限兑换3次；</dd>
                <dd>5、奖品将以用户活动期间在本活动页面所选择兑换的奖品为准，奖品在活动结束后不可进行更换和折现；</dd>
                <dd>6、实物奖品颜色、型号如无特殊需求则以平台实际采购情况为准，实物可能与图片略有差异，介意的用户请慎重兑换；</dd>
                <dd>7、“0元购”专享项目不与平台其他优惠活动同享；</dd>
                <dd>8、“0元购”专享项目不可进行债权转让；</dd>
                <dd>9、所有“0元购”商品将于活动结束后7个工作日内统一联系发放，请获奖用户保持联系方式畅通，若在7个工作日内无法联系，将视为自动放弃奖励；</dd>
                <dd>10、活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</dd>
            </dl>
        </div>
    </div>
    <#include "../../module/login-tip.ftl" />
</div>
</@global.main>