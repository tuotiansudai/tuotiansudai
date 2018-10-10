<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.risk_estimate}" pageJavascript="${js.risk_estimate}" activeNav="我的账户" activeLeftNav="个人资料" title="出借偏好评估">
<div class="content-container invetsment-preferences" id="investmentBox">
    <h4 class="column-title"><em class="tc">出借偏好评估</em></h4>
    <div class="edit-item">
        <h3 class="title-name">用户出借偏好评估</h3>
        <div class="intro-item">
            以下问题根据您的财务状况、出借经验、出借风格、风险偏好和风险承受能力等对您进出借偏好评估，它可协助我们评估您的出借偏好和风险承受能力，有助于您控制出借的风险，同时也便于我行据此为您提供更准确的的出借服务。
        </div>
        <div class="problem-list">
            <dl>
                <dt><span>1.您的年龄是？<strong class="tip-text">这个选项还没有选择哦</strong></span></dt>
                <dd data-score="0"><span>18—30岁</span></dd>
                <dd data-score="1"><span>31—50岁</span></dd>
                <dd data-score="2"><span>51—60岁</span></dd>
                <dd data-score="3"><span>高于60岁</span></dd>
            </dl>
            <dl>
                <dt><span>2.您的家庭年收入折合人民币为？<strong class="tip-text">这个选项还没有选择哦</strong></span></dt>
                <dd data-score="0"><span>8万元以下</span></dd>
                <dd data-score="1"><span>8—15万元</span></dd>
                <dd data-score="2"><span>15—30万元</span></dd>
                <dd data-score="3"><span>30—50万元</span></dd>
                <dd data-score="4"><span>50万元以上</span></dd>
            </dl>
            <dl>
                <dt><span>3.在您每年的家庭收入中，可用于金融出借（储蓄存款除外）的比例为？<strong class="tip-text">这个选项还没有选择哦</strong></span></dt>
                <dd data-score="0"><span>小于10%</span></dd>
                <dd data-score="1"><span>10%至25%</span></dd>
                <dd data-score="2"><span>25%至50%</span></dd>
                <dd data-score="3"><span>大于50%</span></dd>
            </dl>
            <dl>
                <dt><span>4.您有多少年出借股票、基金等风险出借品的经验？<strong class="tip-text">这个选项还没有选择哦</strong></span></dt>
                <dd data-score="0"><span>没有经验</span></dd>
                <dd data-score="1"><span>少于2年</span></dd>
                <dd data-score="2"><span>2至5年</span></dd>
                <dd data-score="3"><span>5至8年</span></dd>
                <dd data-score="4"><span>8年以上</span></dd>
            </dl>
            <dl>
                <dt><span>5.以下哪项描述最符合您的出借态度？<strong class="tip-text">这个选项还没有选择哦</strong></span></dt>
                <dd class="long" data-score="0"><span>厌恶风险，不希望本金损失，希望获得稳定回报</span></dd>
                <dd class="long" data-score="1"><span>保守出借，不希望本金损失，愿意承担一定幅度的收益波动</span></dd>
                <dd class="long" data-score="2"><span>寻求资金的较高收益和成长性，愿意为此承担有限本金损失</span></dd>
                <dd class="long" data-score="3"><span>希望赚取高回报，愿意为此承担较大本金损失</span></dd>
            </dl>
            <dl>
                <dt><span>6.您计划的出借期限是多久？ <strong class="tip-text">这个选项还没有选择哦</strong></span></dt>
                <dd data-score="0"><span>1年以下</span></dd>
                <dd data-score="1"><span>1—3年</span></dd>
                <dd data-score="2"><span>3—5年</span></dd>
                <dd data-score="3"><span>5年以上</span></dd>
            </dl>
            <dl>
                <dt><span>7.您进行出借时所能承受的最大亏损比例是：<strong class="tip-text">这个选项还没有选择哦</strong></span></dt>
                <dd data-score="0"><span>10%以内</span></dd>
                <dd data-score="1"><span>10-30%</span></dd>
                <dd data-score="2"><span>30-50%</span></dd>
                <dd data-score="3"><span>50%以上</span></dd>
            </dl>
            <dl>
                <dt><span>8.您期望的出借的年化收益率：<strong class="tip-text">这个选项还没有选择哦</strong></span></dt>
                <dd class="long" data-score="0"><span>高于同期定期存款</span></dd>
                <dd class="long" data-score="1"><span>10%以内，要求相对风险较低</span></dd>
                <dd class="long" data-score="2"><span>10-15%，可承受中等风险</span></dd>
                <dd class="long" data-score="3"><span>15%以上，可承担较高风险</span></dd>
            </dl>
        </div>
        <div class="invetsment-btn">
            <a href="#" class="btn-item" id="toResult">提交</a>
        </div>
    </div>
</div>
</@global.main>


