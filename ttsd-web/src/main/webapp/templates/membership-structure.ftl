<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.membership}" pageJavascript="${js.membership}" activeNav="成长体系" activeLeftNav="" title="成长体系" site="membership">

<div class="global-member-ship">
    <#if loginName??>
        <div class="user-info-block page-width structure">
            <div class="title">会员等级</div>
            <div class="info clearfix">
                <div class="avatar fl">
                    <img src="${staticServer}/images/sign/head.png"/>
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
                    <img src="${staticServer}/images/sign/head.png"/>
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
                    什么是会员?
                </div>
                <div class="answer">
                    答：是快乐的附件是劳动法谁离开的房间里撒开房间卡死啦的附件流口水的减肥了卡萨减肥了开始放假了快速的积分卡拉斯加对方离开，是浪费大家撒开了的房间时打发速度快了附件是老款的附件
                </div>
            </div>
            <div class="item">
                <div class="question">
                    什么是会员?
                </div>
                <div class="answer">
                    答：是快乐的附件是劳动法谁离开的房间里撒开房间卡死啦的附件流口水的减肥了卡萨减肥了开始放假了快速的积分卡拉斯加对方离开，是浪费大家撒开了的房间时打发速度快了附件是老款的附件
                </div>
            </div>
            <div class="item">
                <div class="question">
                    什么是会员?
                </div>
                <div class="answer">
                    答：是快乐的附件是劳动法谁离开的房间里撒开房间卡死啦的附件流口水的减肥了卡萨减肥了开始放假了快速的积分卡拉斯加对方离开，是浪费大家撒开了的房间时打发速度快了附件是老款的附件
                </div>
            </div>
            <div class="item">
                <div class="question">
                    什么是会员?
                </div>
                <div class="answer">
                    答：是快乐的附件是劳动法谁离开的房间里撒开房间卡死啦的附件流口水的减肥了卡萨减肥了开始放假了快速的积分卡拉斯加对方离开，是浪费大家撒开了的房间时打发速度快了附件是老款的附件
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
                        <input type="text" id="date-time-picker"/>
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