<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="booking-loan-list.js" headLab="announce-manage" sideLab="bookingLoanMan" title="预约投资管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/booking-loan-manage/list" class="form-inline query-build">
        <div class="form-group">
            <label>预约项目</label>
            <select class="selectpicker" name="productType">
                <option value="">全部</option>
                <#list productTypeList as productItem>
                    <option value="${productItem}"
                            <#if productType??&& productType==productItem>selected</#if>>${productItem.getName()}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>预约时间</label>

            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" name="bookingTimeStartTime"
                       value="${(bookingTimeStartTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" name="bookingTimeEndTime"
                       value="${(bookingTimeStartTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>
        <div class="form-group">
            <label for="control-label">投资人</label>
            <input type="text" class="form-control" name="mobile" value="${mobile!}"/>
        </div>
        <div class="form-group">
            <label>通知时间</label>

            <div class='input-group date' id='datetimepicker3'>
                <input type='text' class="form-control" name="noticeTimeStartTime"
                       value="${(noticeTimeStartTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker4'>
                <input type='text' class="form-control" name="noticeTimeEndTime"
                       value="${(noticeTimeEndTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>
        <div class="form-group">
            <label>预约渠道</label>
            <select class="selectpicker" name="source">
                <option value="">全部</option>
                <#list sourceList as sourceItem >
                    <#if sourceItem != 'MOBILE' && sourceItem != 'AUTO'>
                        <option value="${sourceItem}"
                                <#if source??&& source == sourceItem>selected</#if>>${sourceItem.name()}</option>

                    </#if>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>状态</label>
            <select class="selectpicker" name="status">
                <option value="">全部</option>
                <option value="false" <#if status?? && status== false>selected</#if>>队列中</option>
                <option value="true" <#if status?? && status== true>selected</#if>>已通知</option>
            </select>
        </div>


        <button class="btn btn-sm btn-primary query">查询</button>
        <a href="/booking-loan-manage/list" class="btn btn-sm btn-default">重置</a>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    投资人姓名
                </th>
                <th>
                    投资人手机号
                </th>
                <th>
                    预约渠道
                </th>
                <th>
                    预约时间
                </th>
                <th>
                    预约项目
                </th>
                <th>
                    预约投资金额(元)
                </th>
                <th>
                    通知时间
                </th>
                <th>
                    状态
                </th>
                <th>
                    操作
                </th>

            </tr>
            </thead>
            <tbody>
                <#if bookingLoan?? && bookingLoan.records??>
                    <#list bookingLoan.records as record>
                    <tr>
                        <td>${record.userName!}</td>
                        <td>${record.mobile!}</td>
                        <td>${record.source!}</td>
                        <td>${(record.bookingTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                        <td>${record.productType.getName()}</td>
                        <td>${record.amount!}</td>
                        <#if record.noticeTime??>
                            <td>${(record.noticeTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                        <#else >
                            <td>-/-/-</td>
                        </#if>
                        <#if record.status>
                            <td>已通知</td>
                            <td>--</td>
                        <#else >
                            <td>队列中</td>
                            <td><a href="/booking-loan-manage/${record.bookingLoanId}/notice">通知</a></td>
                        </#if>

                    </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <div>
            <span class="bordern">总共${bookingLoan.count}条,每页显示${bookingLoan.pageSize}条</span>
        </div>
        <#if bookingLoan.records?has_content>
            <ul class="pagination">
                <li>
                    <#if bookingLoan.hasPreviousPage>
                    <a href="?index=${index-1}&pageSize=${pageSize}<#if productType??>&productType=${productType}</#if><#if bookingTimeStartTime??>&bookingTimeStartTime=${bookingTimeStartTime?string('yyyy-MM-dd')}</#if><#if bookingTimeEndTime??>&bookingTimeEndTime=${bookingTimeEndTime?string('yyyy-MM-dd')}</#if><#if mobile??>&mobile=${mobile}</#if><#if noticeTimeStartTime??>&noticeTimeStartTime=${noticeTimeStartTime?string('yyyy-MM-dd')}</#if><#if noticeTimeEndTime??>&noticeTimeEndTime=${noticeTimeEndTime?string('yyyy-MM-dd')}</#if><#if source??>&source=${source}</#if><#if status??>&status=${status?c}</#if>"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${bookingLoan.index}</a></li>
                <li>
                    <#if bookingLoan.hasNextPage>
                    <a href="?index=${index+1}&pageSize=${pageSize}<#if productType??>&productType=${productType}</#if><#if bookingTimeStartTime??>&bookingTimeStartTime=${bookingTimeStartTime?string('yyyy-MM-dd')}</#if><#if bookingTimeEndTime??>&bookingTimeEndTime=${bookingTimeEndTime?string('yyyy-MM-dd')}</#if><#if mobile??>&mobile=${mobile}</#if><#if noticeTimeStartTime??>&noticeTimeStartTime=${noticeTimeStartTime?string('yyyy-MM-dd')}</#if><#if noticeTimeEndTime??>&noticeTimeEndTime=${noticeTimeEndTime?string('yyyy-MM-dd')}</#if><#if source??>&source=${source}</#if><#if status??>&status=${status?c}</#if>"
                       aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>
                </li>
            </ul>
            <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>