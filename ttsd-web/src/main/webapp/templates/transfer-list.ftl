<#import "macro/global.ftl" as global>
<@global.main pageCss="${css.transfer_list}" pageJavascript="${js.transfer_list}" activeNav="我要投资" activeLeftNav="" title="标的详情">
<div class="transfer-list-content">
    <ul class="project-type">
        <li><a href="/loan-list">直投项目</a></li>
        <li class="active"><a href="/transfer-list">转让项目</a></li>
    </ul>
    <div class="filter-list clearfix">
        <ul class="wrapper-list">
            <li>
                <span>项目期限: </span>
                <a class="active" href="#">全部</a>
                <a href="#">0-1个月</a>
                <a href="#">1-3个月</a>
                <a href="#">3-6个月</a>
                <a href="#">6个月以上</a>
            </li>
            <li>
                <span>项目状态: </span>
                <a class="active" href="#">全部</a>
                <a href="#">转让中</a>
                <a href="#">已完成</a>
            </li>
            <li>
                <span>年化收益: </span>
                <a class="active" href="#">全部</a>
                <a href="#">10-12%</a>
                <a href="#">12-14%</a>
                <a href="#">14%以上</a>
            </li>
        </ul>
    </div>
    <div class="transfer-list-box">
        <ul>
            <li data-url="/loan/310365478633472" class="clearfix">
                <div class="transfer-info-frame fl">
                    <div class="transfer-top">
                        <span class="l-title fl">ZR20160315-001</span>
                    </div>
                    <div class="transfer-info-dl clearfix">
                        <dl>
                            <dt>转让价格</dt>
                            <dd><em>999999</em>
                            <i>.00</i>元
                            </dd>
                        </dl>
                        <dl>
                            <dt>代收本金</dt>
                            <dd><em>100000</em><i>.00</i>元</dd>
                        </dl>
                        <dl>
                            <dt>年化收益</dt>
                            <dd><em>20%</em></dd>
                        </dl>
                        <dl>
                            <dt>剩余期数</dt>
                            <dd><em>2</em></dd>
                        </dl>
                    </div>
                </div>
                <div class="transfer-right">
                    <div class="transfer-time">
                        <span>截止时间：2016-04-13 0点</span>
                    </div>
                    <div class="rest-amount">
                        <i class="btn-invest btn-normal">马上投资</i>
                    </div>
                </div>
            </li>
        </ul>
        <div class="pagination">
            <span class="total">共 <span class="subTotal">${count}</span> 条记录，当前第 <span class="index-page">${index}</span> 页</span>
            <span class="prev <#if hasPreviousPage>active</#if>"
                  data-url="/loan-list?status=${status!}&productType=${productType!}&rateStart=${rateStart!}&rateEnd=${rateEnd!}&index=${index - 1}">上一页</span>
            <span class="next <#if hasNextPage>active</#if>"
                  data-url="/loan-list?status=${status!}&productType=${productType!}&rateStart=${rateStart!}&rateEnd=${rateEnd!}&index=${index + 1}">下一页</span>
        </div>
    </div>
    <#include "coupon-alert.ftl" />
</div>
    <#include "red-envelope-float.ftl" />
</@global.main>