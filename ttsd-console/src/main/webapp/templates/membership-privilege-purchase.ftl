<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="membership-purchase.js" headLab="membership-manage" sideLab="membershipPurchase" title="会员购买">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/membership-manage/membership-privilege-purchase" class="form-inline query-build" method="get">
        <div class="form-group">
            <label for="mobile">手机号</label>
            <input type="text" id="mobile" name="mobile" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${mobile!}">
        </div>
        <div class="form-group">
            <label for="control-label">购买时间</label>
            <div class='input-group' id='datetimepickerStartTime'>
                <input type='text' class="form-control" name="startTime" value="${(startTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
            </div>
            <span>-</span>

            <div class='input-group' id='datetimepickerEndTime'>
                <input type='text' class="form-control" name="endTime" value="${(endTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span>
            </div>
        </div>

        <div class="form-group">
            <label for="project">购买期限</label>
            <select class="selectpicker" name="duration">
                <option value="" <#if !(priceType??)>selected</#if>>全部</option>
                <#list priceTypeList as priceTypeItem>
                    <option value="${priceTypeItem}" <#if priceType?? && priceTypeItem == priceType>selected</#if>>${priceTypeItem.duration}</option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="project">来源</label>
            <select class="selectpicker" name="source">
                <option value="" <#if !(source??)>selected</#if>>全部</option>
                <option value="WEB" <#if source?? && source == 'WEB'>selected</#if>>WEB</option>
                <option value="IOS" <#if source?? && source == 'IOS'>selected</#if>>IOS</option>
                <option value="ANDROID" <#if source?? && source == 'ANDROID'>selected</#if>>ANDROID</option>
            </select>
        </div>

        <button type="submit" class="btn btn-sm btn-primary">查询</button>
        <button type="reset" class="btn btn-sm btn-default">重置</button>
    </form>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>编号</th>
                <th>手机号</th>
                <th>姓名</th>
                <th>购买期限（天）</th>
                <th>购买金额（元）</th>
                <th>来源</th>
                <th>购买时间</th>
            </tr>
            </thead>
            <tbody>
                <#list data.data.records as item>
                <tr>
                    <td>${item.id?c}</td>
                    <td>${item.mobile}</td>
                    <td>${item.userName}</td>
                    <td>${item.privilegePriceType.duration}</td>
                    <td>${item.amount}</td>
                    <td>${item.source}</td>
                    <td>${item.createdTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="8">暂无数据</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if data.data.count &gt; 0>
            <div>
                <span class="bordern">总共${data.data.count}条,每页显示${data.data.pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if data.data.hasPreviousPage >
                        <a href="?mobile=${mobile!}&duration=${duration!}&source=${source!}&startTime=${startTime!}&endTime=${endTime!}&index=${data.data.index-1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                    </#if>
                </li>
                <li><a>${data.data.index}</a></li>
                <li>
                    <#if data.data.hasNextPage >
                        <a href="?mobile=${mobile!}&duration=${duration!}&source=${source!}&startTime=${startTime!}&endTime=${endTime!}&index=${data.data.index+1}" aria-label="Next">
                            <span aria-hidden="true">Next &raquo;</span>
                        </a>
                    </#if>
                </li>
            </ul>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->

</@global.main>