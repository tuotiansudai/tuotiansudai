<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.risk_estimate}" pageJavascript="${js.risk_estimate}" activeNav="我的账户" activeLeftNav="个人资料" title="投资偏好评估">
<div class="content-container invetsment-preferences" id="investmentBox">
    <h4 class="column-title"><em class="tc">投资偏好评估</em></h4>
    <div class="edit-item">
        <h3 class="title-name">用户投资偏好评估</h3>
        <div class="intro-item">
            以下问题根据您的财务状况、投资经验、投资风格、风险偏好和风险承受能力等对您进投资偏好评估，它可协助我们评估您的投资偏好和风险承受能力，有助于您控制投资的风险，同时也便于我行据此为您提供更准确的的投资服务。
        </div>
        <div class="problem-list">
            <dl>
                <dt>1.您的年龄是？</dt>
                <dd class="active">18—30岁</dd>
                <dd>31—50岁</dd>
                <dd>51—60岁</dd>
                <dd>高于60岁</dd>
            </dl>
            <dl>
                <dt>2.您的家庭年收入折合人民币为？</dt>
                <dd>8万元以下</dd>
                <dd>8—15万元</dd>
                <dd>15—30万元</dd>
                <dd>30—50万元</dd>
                <dd>50万元以上</dd>
            </dl>
            <dl>
                <dt>3.在您每年的家庭收入中，可用于金融投资（储蓄存款除外）的比例为？</dt>
                <dd>小于10%</dd>
                <dd>10%至25%</dd>
                <dd>25%至50%</dd>
                <dd>大于50%</dd>
            </dl>
            <dl>
                <dt>4.您有多少年投资股票、基金等风险投资品的经验？</dt>
                <dd>没有经验</dd>
                <dd>少于2年</dd>
                <dd>2至5年</dd>
                <dd>5至8年</dd>
                <dd>8年以上</dd>
            </dl>
            <dl>
                <dt>5.以下哪项描述最符合您的投资态度？</dt>
                <dd class="long">厌恶风险，不希望本金损失，希望获得稳定回报</dd>
                <dd class="long">保守投资，不希望本金损失，愿意承担一定幅度的收益波动</dd>
                <dd class="long">寻求资金的较高收益和成长性，愿意为此承担有限本金损失</dd>
                <dd class="long">希望赚取高回报，愿意为此承担较大本金损失</dd>
            </dl>
            <dl>
                <dt>6.您计划的投资期限是多久？ </dt>
                <dd>1年以下</dd>
                <dd>1—3年</dd>
                <dd>3—5年</dd>
                <dd>5年以上</dd>
            </dl>
            <dl>
                <dt>7.您进行投资时所能承受的最大亏损比例是：</dt>
                <dd>10%以内</dd>
                <dd>10-30%</dd>
                <dd>30-50%</dd>
                <dd>50%以上</dd>
            </dl>
            <dl>
                <dt>8.您期望的投资的年化收益率：</dt>
                <dd class="long">高于同期定期存款</dd>
                <dd class="long">10%以内，要求相对风险较低</dd>
                <dd class="long">10-15%，可承受中等风险</dd>
                <dd class="long">15%以上，可承担较高风险</dd>
            </dl>
        </div>
        <div class="invetsment-btn">
            <a href="#" class="btn-item" id="toResult">提交</a>
        </div>
    </div>

    <div class="result-item">
        <div class="title-text">您的投资偏好为</div>
        <div class="result-text">进取型</div>
        <div class="intro-text">
            您具有较高的风险承受能力，在投资的过程中可以非常好的认识好风险和收益之间的关系，能承担投资带来的风险，同时可以接受投资带来的结果。投资目标主要是取得超额的收益，为了实现投资目标愿意承担较大的投资风险。
        </div>
    </div>
</div>
</@global.main>


