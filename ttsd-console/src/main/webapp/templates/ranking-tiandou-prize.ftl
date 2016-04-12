<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="tiandouPrize" title="天豆奖品管理">

<!-- content area begin -->
<div style="margin: 20px 30px 20px 300px">
    总抽奖次数：${drawCount!0}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    总抽奖人数：${drawUserCount!0}
</div>
<div class="table-responsive">
    <table class="table table-bordered table-hover" style="width: 400px">
        <thead>
        <tr>
            <th>奖品</th>
            <th>中奖人数</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>MacBook Air</td>
            <td>${macBookWinnerCount!0}</td>
            <td><a href="/activity-manage/prize-winner?prize=MacBook">查看详情</a></td>
        </tr>
        <tr>
            <td>iPhone6s Plus</td>
            <td>${iphoneWinnerCount!0}</td>
            <td><a href="/activity-manage/prize-winner?prize=Iphone6s">查看详情</a></td>
        </tr>
        <tr>
            <td>300元京东购物卡</td>
            <td>${jingDongWinnerCount!0}</td>
            <td><a href="/activity-manage/prize-winner?prize=JingDong300">查看详情</a></td>
        </tr>
        <tr>
            <td>20元现金</td>
            <td>${cashWinnerCount!0}</td>
            <td>-</td>
        </tr>
        <tr>
            <td>0.5%加息券</td>
            <td>${couponWinnerCount!0}</td>
            <td>-</td>
        </tr>
        </tbody>
    </table>
</div>

</@global.main>