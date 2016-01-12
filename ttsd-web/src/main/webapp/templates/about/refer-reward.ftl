<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.about_us}" pageJavascript="" activeNav="关于我们" activeLeftNav="推荐奖励" title="推荐奖励">
<style type="text/css">
    @media screen and (max-width: 700px) {
        .main-frame .left-nav { display: none; }
    }
</style>

<div class="about-us-container company-summary">
    <h2 class="column-title"><em>推荐奖励</em></h2>
    <p>用户在平台采用<span class="give-number">2</span>级推荐机制，</p>
    <p>每级奖励金额为被推荐人投资本金年化的1%，</p>
    <p class="pc">奖励在标的放款时发放到推荐人账户。</p>
    <h3>推荐案例</h3>
    <p>
        <img src="${staticServer}/images/sign/aboutus/give-intro.png" alt="推荐奖励介绍">
        <img src="${staticServer}/images/sign/aboutus/give-intro-phone.png" alt="推荐奖励介绍" class="responsive-width">
    </p>
    <p class="tc">小李再推荐投资人，小张无奖励。 <br/>
        <i class="color-note">注：奖励发放方式请关注平台公告。</i>
    </p>
</div>
</@global.main>