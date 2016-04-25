<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.my_account}" pageJavascript="${js.my_transfer_list}" activeNav="我的账户" activeLeftNav="我的投资" title="投资记录">
<div class="content-container invest-list-content">
    <h4 class="column-title">
        <a href="/investor/invest-list"><em class="tc">投资记录</em></a>
        <a href="/my-transfer-list"><em class="tc active">转让项目</em></a>
    </h4>

    <div class="item-block date-filter">
        <span class="sub-hd">起止时间:</span>
        <input type="text" id="date-picker" class="input-control" size="35"/>
        <span class="select-item" data-day="1">今天</span>
        <span class="select-item" data-day="7">最近一周</span>
        <span class="select-item current" data-day="30">一个月</span>
        <span class="select-item" data-day="180">六个月</span>
        <span class="select-item" data-day="">全部</span>
    </div>
    
    <div class="clear-blank"></div>
    <div class="invest-list">
        <table>
            <thead>
                <tr>
                    <th>转让项目名称</th>
                    <th>转让价格(元)</th>
                    <th>代收本金(元)</th>
                    <th>承接时间</th>
                    <th>下次回款(元)</th>
                    <th>详情</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><a href="#">ZR20160423-001</a></td>
                    <td>10,000,000.00</td>
                    <td>10,000,000.00</td>
                    <td>2016-04-23 10:37</td>
                    <td>2016-04-26/10,000,000.00</td>
                    <td>
                        <a href="#">回款记录</a>|<a href="#">合同</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="pagination" data-url="/investor/invest-list-data" data-page-size="10">
    </div>
</div>
</@global.main>