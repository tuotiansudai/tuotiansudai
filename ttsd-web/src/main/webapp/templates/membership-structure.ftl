<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.membership}" pageJavascript="${js.membership}" activeNav="成长体系" activeLeftNav="" title="会员体系_会员中心_拓天速贷" keywords="拓天会员,拓天会员等级,拓天会员体系,拓天速贷" description="拓天速贷会员成长体系,给处于不同成长阶段的投资用户提供差异化的专享特权,会员等级越高,享有的特权越多." site="membership">

<div class="global-member-ship" id="memberStructure">
    <#if loginName??>
        <div class="user-info-block page-width structure">
            <div class="title">会员等级</div>
            <div class="info clearfix">
                <div class="avatar fl">
                    <span class="icon-avatar"></span>
                    <i class="vip-no-bg vip-${membershipLevel!}"></i>
                </div>
                <div class="text">
                    我当前等级<i class="vip-no-bg vip-${membershipLevel!}"></i>
                    成长值：${membershipPoint!}
                </div>
            </div>
            <div class="progress">
                <div class="progress-bar" style="margin-top: 45px;">
                    <div class="vip-bg vip-0"></div>
                    <div class="vip-bg vip-1"></div>
                    <div class="vip-bg vip-2"></div>
                    <div class="vip-bg vip-3"></div>
                    <div class="vip-bg vip-4"></div>
                    <div class="vip-bg vip-5"></div>
                    <div class="popup-number vip-0">0</div>
                    <div class="popup-number vip-1">5000</div>
                    <div class="popup-number vip-2">50,000</div>
                    <div class="popup-number vip-3">300,000</div>
                    <div class="popup-number vip-4">1,500,000</div>
                    <div class="popup-number vip-5">5,000,000</div>
                </div>
            </div>
        </div>
    <#else>
        <div class="user-info-block page-width no-login">
            <div class="info clearfix">
                <div class="avatar fl">
                    <span class="icon-avatar"></span>
                </div>
                <div class="text">
                    亲，成为会员可享受多种特权哦~ <br/>
                    了解更多请 <a href="/login" class="btn-normal">登录</a>
                </div>
            </div>
            <div class="progress">
                <div class="progress-bar" style="margin-top: 45px;">
                    <div class="vip-bg vip-0"></div>
                    <div class="vip-bg vip-1"></div>
                    <div class="vip-bg vip-2"></div>
                    <div class="vip-bg vip-3"></div>
                    <div class="vip-bg vip-4"></div>
                    <div class="vip-bg vip-5"></div>
                    <div class="popup-number vip-0">0</div>
                    <div class="popup-number vip-1">5000</div>
                    <div class="popup-number vip-2">50,000</div>
                    <div class="popup-number vip-3">300,000</div>
                    <div class="popup-number vip-4">1,500,000</div>
                    <div class="popup-number vip-5">5,000,000</div>
                </div>
            </div>
            <div class="register">
                新用户请 <a href="/register">注册</a>
            </div>
        </div>
    </#if>

    <div class="instructions" id="instructions">
        <div class="main-title">
            <div class="inner">
                <h2>会员说明</h2>
            </div>
        </div>
        <div class="inner-block">
            <div class="item active">
                <div class="question">
                    拓天速贷会员介绍
                </div>
                <div class="answer">
                    <div>拓天会员是为给处于不同成长阶段的出借用户提供差异化的专享特权而设置的。</div>
                    <div>会员目前总计分为六个等级，依次为：V0、V1、V2、V3、V4、V5。会员等级越高，享有的特权越多。</div>
                </div>
            </div>
            <div class="item">
                <div class="question">
                    如何获取成长值？
                </div>
                <div class="answer">
                    <div>1，在平台出借，出借金额1：1转化为成长值，不足1元的舍去不计；</div>
                    <div>2，目前出借债权转让项目无法获得成长值。</div>
                </div>
            </div>

        </div>
    </div>


    <div class="structure-detail">
        <div class="main-title">
            <div class="inner">
                <h2>成长值明细</h2>
            </div>
        </div>
        <div class="inner-block">
            <div class="filter-bar">
                <div class="clearfix">
                    <div class="fl buttons" id="filter-btns">
                        <span data-type="all" class="active">全部</span>
                        <span data-type="180">六个月</span>
                        <span data-type="30">一个月</span>
                        <span data-type="7">本周</span>
                        <span data-type="1">今天</span>
                    </div>
                    <div class="fr">
                        <label>日期</label>
                        <input type="text" id="date-time-picker" readonly/>
                    </div>
                </div>
            </div>
            <div class="table">
                <table>
                    <thead>
                    <tr>
                        <th>日期</th>
                        <th>详情</th>
                        <th>成长值</th>
                    </tr>
                    </thead>
                    <tbody id="tbody">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script type="text/template" id="tpl">
    <% if (data && data.length) { %>
    <% _.each(data, function(value) { %>
    <tr>
        <td><%= value.createdTime %></td>
        <td><%= value.description %></td>
        <td>+<%= value.experience %></td>
    </tr>
    <% }); %>
    <% } else { %>
    <tr>
        <td></td>
        <td>暂无数据</td>
        <td></td>
    </tr>
    <% } %>
</script>


</@global.main>