<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.create_transfer}" pageJavascript="${js.create_transfer}" activeNav="我的账户" activeLeftNav="债权转让" title="债权转让">
<div class="content-container create-transfer-content">
    <h4 class="column-title"><em class="tc">债权转让</em></h4>
    <ul class="filters-list">
        <li class="active">可转让债权</li>
        <li>转让中债权</li>
        <li>转让记录</li>
    </ul>
    <div class="list-container">
        <div class="record-list active">
            <table class="table-striped transfer-list">
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th class="tr">代收本金(元)</th>
                    <th>年化收益</th>
                    <th>剩余期数</th>
                    <th>到期时间</th>
                    <th>下次回款(元)</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <a href="/loan" class="project-name">个人经营借款</a>
                    </td>
                    <td class="tr">10,000,000.00</td>
                    <td>12%</td>
                    <td>1/3</td>
                    <td>2017-01-22</td>
                    <td>
                        2016-03-22/10,000,000.00
                    </td>
                    <td>
                        <a href="####">申请转让</a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="/loan" class="project-name">个人经营借款</a>
                    </td>
                    <td class="tr">10,000,000.00</td>
                    <td>12%</td>
                    <td>1/3</td>
                    <td>2017-01-22</td>
                    <td>
                        2016-03-22/10,000,000.00
                    </td>
                    <td>
                        <a href="####">申请转让</a>
                    </td>
                </tr>
                <td colspan="7" class="no-data">暂时没有可转让债权记录</td>
                </tbody>
            </table>
            <div class="pagination" data-url="/investor/invest-list-data" data-page-size="10">
            </div>
        </div>
        <div class="record-list">
            <table class="table-striped transfer-list">
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th class="tr">代收本金(元)</th>
                    <th>年化收益</th>
                    <th>剩余期数</th>
                    <th>到期时间</th>
                    <th>下次回款(元)</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <a href="/loan" class="project-name">个人经营借款</a>
                    </td>
                    <td class="tr">10,000,000.00</td>
                    <td>12%</td>
                    <td>1/3</td>
                    <td>2017-01-22</td>
                    <td>
                        2016-03-22/10,000,000.00
                    </td>
                    <td>
                        <a href="####">申请转让</a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="/loan" class="project-name">个人经营借款</a>
                    </td>
                    <td class="tr">10,000,000.00</td>
                    <td>12%</td>
                    <td>1/3</td>
                    <td>2017-01-22</td>
                    <td>
                        2016-03-22/10,000,000.00
                    </td>
                    <td>
                        <a href="####">申请转让</a>
                    </td>
                </tr>
                <td colspan="7" class="no-data">暂时没有可转让债权记录</td>
                </tbody>
            </table>
            <div class="pagination" data-url="/investor/invest-list-data" data-page-size="10">
            </div>
        </div>
        <div class="record-list">
            <table class="table-striped transfer-list">
                <thead>
                <tr>
                    <th>项目名称</th>
                    <th class="tr">代收本金(元)</th>
                    <th>年化收益</th>
                    <th>剩余期数</th>
                    <th>到期时间</th>
                    <th>下次回款(元)</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <a href="/loan" class="project-name">个人经营借款</a>
                    </td>
                    <td class="tr">10,000,000.00</td>
                    <td>12%</td>
                    <td>1/3</td>
                    <td>2017-01-22</td>
                    <td>
                        2016-03-22/10,000,000.00
                    </td>
                    <td>
                        <a href="####">申请转让</a>
                    </td>
                </tr>
                <tr>
                    <td>
                        <a href="/loan" class="project-name">个人经营借款</a>
                    </td>
                    <td class="tr">10,000,000.00</td>
                    <td>12%</td>
                    <td>1/3</td>
                    <td>2017-01-22</td>
                    <td>
                        2016-03-22/10,000,000.00
                    </td>
                    <td>
                        <a href="####">申请转让</a>
                    </td>
                </tr>
                <td colspan="7" class="no-data">暂时没有可转让债权记录</td>
                </tbody>
            </table>
            <div class="pagination" data-url="/investor/invest-list-data" data-page-size="10">
            </div>
        </div>
    </div>
</div>
</@global.main>