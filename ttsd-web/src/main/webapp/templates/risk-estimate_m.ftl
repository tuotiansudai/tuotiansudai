<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.risk_estimate}" pageJavascript="${m_js.risk_estimate}" title="投资偏好评估">
<div id="riskBox">
    <div class="m-header"><em class="icon-left" id="goBackIcon"><i></i></em>投资偏好评估</div>
    <div class="problem-list">

        <dl class="part-one">
            <dt>1.您的年龄是？</dt>
            <dd data-score="0"><span>18—30岁</span></dd>
            <dd data-score="1"><span>31—50岁</span></dd>
            <dd data-score="2"><span>51—60岁</span></dd>
            <dd data-score="3"><span>高于60岁</span></dd>
            <span class="small-decorate small-decorate-right"></span>
            <span class="small-decorate small-decorate-left"></span>
        </dl>

        <dl class="part-two">
            <dt>2.您的家庭年收入折合人民币为？</dt>
            <dd data-score="0"><span>8万元以下</span></dd>
            <dd data-score="1"><span>8—15万元</span></dd>
            <dd data-score="2"><span>15—30万元</span></dd>
            <dd data-score="3"><span>30—50万元</span></dd>
            <dd data-score="4"><span>50万元以上</span></dd>
            <span class="small-decorate small-decorate-right"></span>
            <span class="small-decorate small-decorate-left"></span>
        </dl>

        <dl class="part-three">
            <dt>3.在您每年的家庭收入中，可用于金融投资（储蓄存款除外）的比例为？</dt>
            <dd data-score="0"><span>小于10%</span></dd>
            <dd data-score="1"><span>10%至25%</span></dd>
            <dd data-score="2"><span>25%至50%</span></dd>
            <dd data-score="3"><span>大于50%</span></dd>
            <span class="small-decorate small-decorate-right"></span>
            <span class="small-decorate small-decorate-left"></span>
        </dl>

        <dl class="part-four">
            <dt>4.您有多少年投资股票、基金等风险投资品的经验？</dt>
            <dd data-score="0"><span>没有经验</span></dd>
            <dd data-score="1"><span>少于2年</span></dd>
            <dd data-score="2"><span>2至5年</span></dd>
            <dd data-score="3"><span>5至8年</span></dd>
            <dd data-score="4"><span>8年以上</span></dd>
            <span class="small-decorate small-decorate-right"></span>
            <span class="small-decorate small-decorate-left"></span>
        </dl>

        <dl class="part-five">
            <dt>5.以下哪项描述最符合您的投资态度？</dt>
            <dd class="long" data-score="0"><span>厌恶风险，不希望本金损失，希望获得稳定回报</span></dd>
            <dd class="long" data-score="1"><span>保守投资，不希望本金损失，愿意承担一定幅度的收益波动</span></dd>
            <dd class="long" data-score="2"><span>寻求资金的较高收益和成长性，愿意为此承担有限本金损失</span></dd>
            <dd class="long" data-score="3"><span>希望赚取高回报，愿意为此承担较大本金损失</span></dd>
            <span class="small-decorate small-decorate-right"></span>
            <span class="small-decorate small-decorate-left"></span>
        </dl>

        <dl class="part-six">
            <dt>6.您计划的投资期限是多久？</dt>
            <dd data-score="0"><span>1年以下</span></dd>
            <dd data-score="1"><span>1—3年</span></dd>
            <dd data-score="2"><span>3—5年</span></dd>
            <dd data-score="3"><span>5年以上</span></dd>
            <span class="small-decorate small-decorate-right"></span>
            <span class="small-decorate small-decorate-left"></span>
        </dl>

        <dl class="part-seven">
            <dt>7.您进行投资时所能承受的最大亏损比例是：</dt>
            <dd data-score="0"><span>10%以内</span></dd>
            <dd data-score="1"><span>10-30%</span></dd>
            <dd data-score="2"><span>30-50%</span></dd>
            <dd data-score="3"><span>50%以上</span></dd>
            <span class="small-decorate small-decorate-right"></span>
            <span class="small-decorate small-decorate-left"></span>
        </dl>

        <dl class="part-last">
            <dt>8.您期望的投资的年化收益率：</dt>
            <dd class="long" data-score="0"><span>高于同期定期存款</span></dd>
            <dd class="long" data-score="1"><span>10%以内，要求相对风险较低</span></dd>
            <dd class="long" data-score="2"><span>10-15%，可承受中等风险</span></dd>
            <dd class="long" data-score="3"><span>15%以上，可承担较高风险</span></dd>
            <span class="small-decorate small-decorate-right"></span>
            <span class="small-decorate small-decorate-left"></span>
        </dl>

    </div>
</div>

</@global.main>
