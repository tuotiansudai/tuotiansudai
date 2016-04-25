<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.about_us}" pageJavascript="${js.about_us}" activeNav="关于我们" activeLeftNav="推荐奖励" title="推荐奖励_拓天速贷" keywords="拓天速贷,推荐奖励,P2P理财,短期理财,短期投资,拓天速贷2级推荐机制" description="拓天速贷针对老用户推出2级推荐机制的推荐奖励,可以让您的财富快速升值.">
<style type="text/css">
    @media screen and (max-width: 700px) {
        .main-frame .left-nav { display: none; }
    }
</style>

<div class="about-us-container company-summary">
    <h2 class="column-title"><em>推荐奖励</em></h2>
    <p>用户在平台采用<span class="give-number">2</span>级推荐机制，</p>
    <p>每级奖励金额为被推荐人投资本金预期年化收益的1%，</p>
    <p class="pc">奖励在标的放款时发放到推荐人账户。</p>
    <h3>推荐案例</h3>

    <div class="refer-reward-box pc">
        <div class="pc-column"></div>
        <div class="section-one border-radius">
            发生二级以上的推荐，小张不获得奖励
        </div>
        <div class="section-two border-radius">
            小明每次投资，小张会得到投资本金预期年化收益的1%作为奖励。
            例如：小明投资10万元三个月（90天）标的，小张得到奖励金额为100000×（1%/365×90）=246.57元，放款时一次性发放。
        </div>
        <div class="section-three border-radius">
            小李每次投资，小张和小明分别得到投资本金预期年化收益的1%作为奖励。
            例如：小李投资10万元一个月（30天）标的，小张得到奖励金额为100000×（1%/365×30）=82.19元，放款时一次性发放。
        </div>
        <div class="section-four border-radius">
            小刘是小李推荐的，小刘每次投资，小明和小李会分别得到投资本金预期年化收益的1%作为奖励，小张无奖励。
            例如：小刘投资10万元半年期（180天）标的，他们分别得到奖励金额为100000×（1%/365×180）=493.15元，放款时一次性发放。
        </div>
    </div>
    <div class="refer-reward-box mobile">
        <img src="${staticServer}/images/sign/aboutus/give-intro-appnew.png" alt="推荐奖励介绍" class="responsive-width">
    </div>
    <p class="tc info">
        <span>小李再推荐投资人，小张无奖励。 </span>
        <i class="color-note">
            注：奖励发放方式请关注平台公告。</i>
    </p>
</div>
</@global.main>



