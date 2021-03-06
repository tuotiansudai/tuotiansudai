<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="invest-list.js" headLab="finance-manage" sideLab="userInvest" title="用户投资管理">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" method="get" class="form-inline query-build">
        <div class="form-group">
            <label>项目编号</label>
            <input type="text" class="form-control" name="loanId" placeholder=""
                   value="${(loanId?string.computer)!}">
        </div>
        <div class="form-group">
            <label>日期</label>

            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" name="startTime"
                       value="${(startTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" name="endTime"
                       value="${(endTime?string('yyyy-MM-dd'))!}"/>
					                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>
        <div class="form-group">
            <label>投资人</label>
            <input type="text" id="tags" name="mobile" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${mobile!}"/>
        </div>
        <div class="form-group">
            <label>投资状态</label>
            <select class="selectpicker" name="investStatus">
                <option value="" <#if !(investStatus??)>selected</#if>>全部</option>
                <#list investStatusList as status>
                    <option value="${status}" <#if investStatus?? && status==investStatus>selected</#if>>
                    ${status.description}
                    </option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>渠道</label>
            <select class="selectpicker" name="channel">
                <option value="">全部</option>
                <#list channelList as channelName>
                    <option value="${channelName}"
                            <#if (channel?has_content && channel == channelName) >selected</#if>
                            >${channelName}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>来源</label>
            <select class="selectpicker" name="source">
                <option value="">全部</option>
                <#list sourceList as sourceItem>
                    <option value="${sourceItem.name()}"
                            <#if (source?has_content && source == sourceItem.name()) >selected</#if>
                            >${sourceItem.name()}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>投资人角色</label>
            <select class="selectpicker" name="role">
                <option value="">全部</option>
                <#list roleList as roleItem>
                    <option value="${roleItem.name()}"
                            <#if (role?has_content && role == roleItem.name()) >selected</#if>
                            >${roleItem.getDescription()}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>使用优惠类型</label>
            <select class="selectpicker" name="usedPreferenceType">
                <option value="">全部</option>
                <#list preferenceTypes as preferenceType>
                    <option value="${preferenceType.name()}"
                            <#if (selectedPreferenceType?has_content && preferenceType == selectedPreferenceType.name()) >selected</#if>
                            >${preferenceType.getDescription()}</option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label>是否允许转让</label>
            <select class="selectpicker" name="transferType">
                <option value="" <#if !transferType?? >selected</#if>>全部</option>
                <option value="no" <#if transferType?? && transferType=='no' >selected</#if>>否</option>
                <option value="normalTransfer" <#if transferType?? && transferType=='normalTransfer' >selected</#if>>正常转让</option>
                <option value="overdueTransfer" <#if transferType?? && transferType=='overdueTransfer' >selected</#if>>逾期转让</option>
            </select>
        </div>

        <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
        <button type="reset" class="btn btn-sm btn-default btnSearch">重置</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th colspan="18">合计投资金额：${data.sumAmount/100} 元 (本统计不包含债权转让投资)</th>
            </tr>
            <tr>
                <th>项目编号</th>
                <th>项目名称</th>
                <th>期数</th>
                <th>投资人</th>
                <th>投资人姓名</th>
                <th>投资人手机号</th>
                <th>推荐人</th>
                <th>推荐人姓名</th>
                <th>推荐人手机号</th>
                <th>渠道</th>
                <th>来源</th>
                <th>投资时间</th>
                <th>自动投标</th>
                <th>投资金额(元)</th>
                <th>使用优惠(使用优惠信息／实际返款)</th>
                <th>阶梯加息优惠(阶梯加息利率/实际返款)</th>
                <th>投资状态</th>
                <th>允许转让</th>
                <th>回款记录</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as invest>
                <tr>
                    <td>${invest.loanId?string.computer}</td>
                    <td>${invest.loanName}</td>
                    <td>${invest.loanPeriods?string('0')}</td>
                    <td>${invest.investorLoginName!}</td>
                    <td>${invest.investorUserName!}
                        <#if invest.investorStaff>
                            <span class="glyphicon glyphicon glyphicon-user" aria-hidden="true"></span>
                        </#if>
                    </td>
                    <td>${invest.investorMobile!}</td>
                    <td>${invest.referrerLoginName!}
                        <#if invest.referrerStaff>
                            <span class="glyphicon glyphicon glyphicon-user" aria-hidden="true"></span>
                        </#if>
                    </td>
                    <td>${invest.referrerUserName!}</td>
                    <td>${invest.referrerMobile!}</td>
                    <td>${invest.channel!}</td>
                    <td>${invest.source}</td>
                    <td>${invest.investTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>${invest.autoInvest?then('是','否')}</td>
                    <td>${invest.investAmount}</td>
                    <td>${invest.couponDetail!'-'} / ${invest.couponActualInterest!'-'}</td>
                    <td>${invest.extraDetail!'-'} / ${invest.extraActualInterest!'-'}</td>
                    <td>${invest.investStatus}</td>
                    <td><#if invest.overdueTransfer>
                            逾期转让
                        <#elseif !invest.overdueTransfer && invest.transferStatus != 'NONTRANSFERABLE'>
                            正常转让
                        <#else>
                            否
                        </#if>
                        <@security.authorize access="hasAnyAuthority('ADMIN','OPERATOR_ADMIN')">
                            <#if invest.transferStatus == 'NONTRANSFERABLE'>
                                <button type="button" class="btn btn-sm btn-primary updateTransferStatus"
                                        data-investid="${invest.investId?string.computer}" data-warmprompt="是否确认允许该笔投资进行正常转让?">允许正常转让</button>
                            <#elseif !invest.overdueTransfer && invest.transferStatus == 'TRANSFERABLE' && invest.lastPeriodRepayStatus?? && invest.lastPeriodRepayStatus == 'OVERDUE'>
                                <button type="button" class="btn btn-sm btn-primary updateTransferStatus"
                                        data-investid="${invest.investId?string.computer}" data-warmprompt="是否确认允许该笔投资进行逾期转让?">允许逾期转让</button>
                            </#if>
                        </@security.authorize>
                    </td>
                    <td><a href="/finance-manage/invest-repay/${invest.investId?string.computer}">回款记录</a></td>
                </tr>
                <#else>
                <tr>
                    <td colspan="18">Empty</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <div>
            <span class="bordern">总共${data.count}条, 每页显示${data.pageSize}条</span>
        </div>
        <#if data.records?has_content>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                    <a href="?index=${data.index - 1}&<#if loanId??>loanId=${loanId?string.computer}&</#if><#if mobile??>mobile=${mobile}&</#if><#if startTime??>startTime=${startTime?string('yyyy-MM-dd')}&</#if><#if endTime??>endTime=${endTime?string('yyyy-MM-dd')}&</#if><#if investStatus??>investStatus=${investStatus}&</#if><#if channel??>channel=${channel}&</#if><#if source??>source=${source}&</#if><#if role??>role=${role}&</#if><#if transferType??>transferType=${transferType}&</#if><#if selectedPreferenceType??>usedPreferenceType=${selectedPreferenceType.name()}</#if>"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage>
                    <a href="?index=${data.index + 1}&<#if loanId??>loanId=${loanId?string.computer}&</#if><#if mobile??>mobile=${mobile}&</#if><#if startTime??>startTime=${startTime?string('yyyy-MM-dd')}&</#if><#if endTime??>endTime=${endTime?string('yyyy-MM-dd')}&</#if><#if investStatus??>investStatus=${investStatus}&</#if><#if channel??>channel=${channel}&</#if><#if source??>source=${source}&</#if><#if role??>role=${role}&</#if><#if transferType??>transferType=${transferType}&</#if><#if selectedPreferenceType??>usedPreferenceType=${selectedPreferenceType.name()}</#if>"
                       aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>
                </li>
            </ul>
            <@security.authorize access="hasAnyAuthority('DATA')">
                <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
            </@security.authorize>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>