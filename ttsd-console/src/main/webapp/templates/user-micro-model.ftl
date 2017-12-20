<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="user-micro-model.js" headLab="user-manage" sideLab="userMicroModel" title="用户微模型">

    <#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
    <#assign pagination = baseDto.data />
    <#assign userMicroList = pagination.records />

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="mobile">投资人手机号</label>
            <input type="text" class="form-control ui-autocomplete-input" id="mobile" name="mobile" datatype="*"
                   autocomplete="off" value="${mobile!}"/>
        </div>

        <div class="form-group">
            <label for="project">角色</label>
            <select class="selectpicker" name="userRole">
                <option value="">全部</option>
                <#list roleList as roleItem>
                    <option value="${roleItem}"
                            <#if (roleSelected?has_content && roleSelected.name() == roleItem.name()) >selected</#if>>${roleItem.getDescription()}
                    </option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="number">注册时间</label>

            <div class='input-group date' id='registerTimeStart'>
                <input type='text' class="form-control" name="registerTimeStart"
                       value="${(registerTimeStart?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
            </div>
            -
            <div class='input-group date' id='registerTimeEnd'>
                <input type='text' class="form-control" name="registerTimeEnd"
                       value="${(registerTimeEnd?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
            </div>
        </div>
        <div class="form-group">
            <label for="project">是否实名</label>
            <select class="selectpicker" name="hasCertify">
                <option value="" selected>全部</option>
                <option value="true" <#if hasCertify?has_content && hasCertify == 'true'>selected</#if>>是</option>
                <option value="false" <#if hasCertify?has_content && hasCertify == 'false'>selected</#if>>否</option>
            </select>
        </div>
        <div class="form-group">
            <label for="project">是否投资</label>
            <select class="selectpicker" name="invested">
                <option value="" selected>全部</option>
                <option value="true" <#if invested?has_content && invested == 'true'>selected</#if>>是</option>
                <option value="false" <#if invested?has_content && invested == 'false'>selected</#if>>否</option>
            </select>
        </div>

        <div class="form-group">
            <label for="project">累计投资金额：</label>
            <input type="text" class="form-control total-invest-amount-start" name="totalInvestAmountStart" value="${(totalInvestAmountStart?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control total-invest-amount-end" name="totalInvestAmountEnd" value="${(totalInvestAmountEnd?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <div class="form-group">
            <label for="project">累计提现金额：</label>
            <input type="text" class="form-control total-withdraw-amount-start" name="totalWithdrawAmountStart" value="${(totalWithdrawAmountStart?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control total-withdraw-amount-end" name="totalWithdrawAmountEnd" value="${(totalWithdrawAmountEnd?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <div class="form-group">
            <label for="project">账户余额：</label>
            <input type="text" class="form-control user-balance-start" name="userBalanceStart" value="${(userBalanceStart?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control user-balance-end" name="userBalanceEnd" value="${(userBalanceEnd?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <div class="form-group">
            <label for="project">投资次数：</label>
            <input type="text" class="form-control invest-count-start" name="investCountStart" value="${(investCountStart?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control invest-count-end" name="investCountEnd" value="${(investCountEnd?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <div class="form-group">
            <label for="project">投资标的数：</label>
            <input type="text" class="form-control loan-count-start" name="loanCountStart" value="${(loanCountStart?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control loan-count-end" name="loanCountEnd" value="${(loanCountEnd?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <div class="form-group">
            <label for="project">转化周期：</label>
            <input type="text" class="form-control transform-period-start" name="transformPeriodStart" value="${(transformPeriodStart?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control transform-period-end" name="transformPeriodEnd" value="${(transformPeriodEnd?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <div class="form-group">
            <label for="project">首二笔间隔：</label>
            <input type="text" class="form-control invest-1st-2nd-timing-start" name="invest1st2ndTimingStart" value="${(invest1st2ndTimingStart?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control invest-1st-2nd-timing-end" name="invest1st2ndTimingEnd" value="${(invest1st2ndTimingEnd?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <div class="form-group">
            <label for="project">首三笔间隔：</label>
            <input type="text" class="form-control invest-1st-3nd-timing-start" name="invest1st3ndTimingStart" value="${(invest1st3ndTimingStart?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control invest-1st-3nd-timing-end" name="invest1st3ndTimingEnd" value="${(invest1st3ndTimingEnd?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <div class="form-group">
            <label for="number">最后一次投资时间：</label>
            <div class='input-group date' id='lastInvestTimeStart'>
                <input type='text' class="form-control" name="lastInvestTimeStart"
                       value="${(lastInvestTimeStart?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
            </div>
            -
            <div class='input-group date' id='lastInvestTimeEnd'>
                <input type='text' class="form-control" name="lastInvestTimeEnd"
                       value="${(lastInvestTimeEnd?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
            </div>
        </div>

        <div class="form-group">
            <label for="project">待收回款：</label>
            <input type="text" class="form-control repaying-amount-start" name="repayingAmountStart" value="${(repayingAmountStart?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">~
            <input type="text" class="form-control repaying-amount-end" name="repayingAmountEnd" value="${(repayingAmountEnd?c)!}" onblur="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
        </div>

        <div class="form-group">
            <label for="number">最后一次登录/访问时间：</label>
            <div class='input-group date' id='lastLoginTimeStart'>
                <input type='text' class="form-control" name="lastLoginTimeStart"
                       value="${(lastLoginTimeStart?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
            </div>
            -
            <div class='input-group date' id='lastLoginTimeEnd'>
                <input type='text' class="form-control" name="lastLoginTimeEnd"
                       value="${(lastLoginTimeEnd?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
            </div>
        </div>

        <div class="form-group">
            <label for="project">最后一次登录/访问来源：</label>
            <select class="selectpicker" name="lastLoginSource">
                <option value="">全部</option>
                <#list sourceList as sourceItem>
                    <#if sourceItem.name() != 'AUTO'>
                        <option value="${sourceItem}"
                                <#if (lastLoginSource?has_content && lastLoginSource.name() == sourceItem.name()) >selected</#if>>${sourceItem.name()}
                        </option>
                    </#if>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label for="number">最后一笔回款时间：</label>
            <div class='input-group date' id='lastRepayTimeStart'>
                <input type='text' class="form-control" name="lastRepayTimeStart"
                       value="${(lastRepayTimeStart?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
            </div>
            -
            <div class='input-group date' id='lastRepayTimeEnd'>
                <input type='text' class="form-control" name="lastRepayTimeEnd"
                       value="${(lastRepayTimeEnd?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
            </div>
        </div>

        <div class="form-group">
            <label for="number">最后一笔提现时间：</label>
            <div class='input-group date' id='lastWithdrawTimeStart'>
                <input type='text' class="form-control" name="lastWithdrawTimeStart"
                       value="${(lastWithdrawTimeStart?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
            </div>
            -
            <div class='input-group date' id='lastWithdrawTimeEnd'>
                <input type='text' class="form-control" name="lastWithdrawTimeEnd"
                       value="${(lastWithdrawTimeEnd?string('yyyy-MM-dd HH:mm:ss'))!}"/>
                <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
            </div>
        </div>

        <button type="submit" class="btn btn-sm btn-primary">查询</button>
        <button type="reset" class="btn btn-sm btn-default">重置</button>
    </form>


    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>姓名</th>
                <th>手机号</th>
                <th>注册渠道</th>
                <th>注册时间</th>
                <th>注册后未投资天数（天）</th>
                <th>是否投资</th>
                <th>累计投资金额（元）</th>
                <th>投资笔数</th>
                <th>笔均投资金额（元）</th>
                <th>投资标的数</th>
                <th>标的平均投资金额（元）</th>
                <th>转化周期（天）</th>
                <th>首二间隔（天）</th>
                <th>首三间隔（天）</th>
                <th>最后一次投资时间</th>
                <th>最后一笔回款时间</th>
                <th>最后一笔提现时间</th>
                <th>待收回款（元）</th>
                <th>账户余额（元）</th>
                <th>累计提现金额（元）</th>
                <th>最后一次登录时间</th>
                <th>最后一次登录时间距今（天）</th>
                <th>最后一次登录来源</th>
            </tr>
            </thead>
            <tbody>
                <#list userMicroList as userItem>
                <tr>
                    <td>${userItem.userName!}</td>
                    <td>${userItem.mobile!}</td>
                    <td>${userItem.channel!}</td>
                    <td>${(userItem.registerTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${userItem.noInvestPeriod!}</td>
                    <td><#if userItem.invested == true>是<#else>否</#if></td>
                    <td>${((userItem.totalInvestAmount/100)?string["0.##"])!}</td>
                    <td>${userItem.investCount}</td>
                    <td>${((userItem.averageInvestAmount/100)?string["0.##"])!}</td>
                    <td>${userItem.loanCount}</td>
                    <td>${((userItem.averageLoanInvestAmount/100)?string["0.##"])!}</td>
                    <td>${userItem.transformPeriod!}</td>
                    <td>${userItem.invest1st2ndTiming!}</td>
                    <td>${userItem.invest1st3rdTiming!}</td>
                    <td>${(userItem.lastInvestTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${(userItem.lastRepayTime?string('yyyy-MM-dd'))!}</td>
                    <td>${(userItem.lastWithdrawTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${((userItem.totalRepayingAmount/100)?string["0.##"])!}</td>
                    <td>${((userItem.userBalance/100)?string["0.##"])!}</td>
                    <td>${((userItem.totalWithdrawAmount/100)?string["0.##"])!}</td>
                    <td>${(userItem.lastLoginTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${userItem.lastLoginToNow!}</td>
                    <td>${userItem.lastLoginSource!}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="19">Empty</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if userMicroList?has_content>
            <div>
                <span class="bordern">总共${pagination.count}条,每页显示${pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if pagination.hasPreviousPage >
                    <a href="?mobile=${mobile!}&registerTimeStart=${(registerTimeStart?string('yyyy-MM-dd HH:mm:ss'))!}&registerTimeEnd=${(registerTimeEnd?string('yyyy-MM-dd HH:mm:ss'))!}
&hasCertify=${hasCertify!}&invested=${invested!}&totalInvestAmountStart=${(totalInvestAmountStart?c)!}&totalInvestAmountEnd=${(totalInvestAmountEnd?c)!}
&investCountStart=${(investCountStart?c)!}&investCountEnd=${(investCountEnd?c)!}&loanCountStart=${(loanCountStart?c)!}&loanCountEnd=${(loanCountEnd?c)!}&transformPeriodStart=${(transformPeriodStart?c)!}
&transformPeriodEnd=${(transformPeriodEnd?c)!}&invest1st2ndTimingStart=${(invest1st2ndTimingStart?c)!}&invest1st2ndTimingEnd=${(invest1st2ndTimingEnd?c)!}
&invest1st3ndTimingStart=${(invest1st3ndTimingStart?c)!}&invest1st3ndTimingEnd=${(invest1st3ndTimingEnd?c)!}&lastInvestTimeStart=${(lastInvestTimeStart?string('yyyy-MM-dd HH:mm:ss'))!}
&lastInvestTimeEnd=${(lastInvestTimeEnd?string('yyyy-MM-dd HH:mm:ss'))!}&repayingAmountStart=${(repayingAmountStart?c)!}&repayingAmountEnd=${(repayingAmountEnd?c)!}
&lastLoginTimeStart=${(lastLoginTimeStart?string('yyyy-MM-dd HH:mm:ss'))!}&lastLoginTimeEnd=${(lastLoginTimeEnd?string('yyyy-MM-dd HH:mm:ss'))!}
&lastLoginSource=${lastLoginSource!}&pageSize=${pageSize}&index=${index-1}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${pagination.index}</a></li>
                <li>
                    <#if pagination.hasNextPage >
                    <a href="?mobile=${mobile!}&registerTimeStart=${(registerTimeStart?string('yyyy-MM-dd HH:mm:ss'))!}&registerTimeEnd=${(registerTimeEnd?string('yyyy-MM-dd HH:mm:ss'))!}
&hasCertify=${hasCertify!}&invested=${invested!}&totalInvestAmountStart=${(totalInvestAmountStart?c)!}&totalInvestAmountEnd=${(totalInvestAmountEnd?c)!}
&investCountStart=${(investCountStart?c)!}&investCountEnd=${(investCountEnd?c)!}&loanCountStart=${(loanCountStart?c)!}&loanCountEnd=${(loanCountEnd?c)!}&transformPeriodStart=${(transformPeriodStart?c)!}
&transformPeriodEnd=${(transformPeriodEnd?c)!}&invest1st2ndTimingStart=${(invest1st2ndTimingStart?c)!}&invest1st2ndTimingEnd=${(invest1st2ndTimingEnd?c)!}
&invest1st3ndTimingStart=${(invest1st3ndTimingStart?c)!}&invest1st3ndTimingEnd=${(invest1st3ndTimingEnd?c)!}&lastInvestTimeStart=${(lastInvestTimeStart?string('yyyy-MM-dd HH:mm:ss'))!}
&lastInvestTimeEnd=${(lastInvestTimeEnd?string('yyyy-MM-dd HH:mm:ss'))!}&repayingAmountStart=${(repayingAmountStart?c)!}&repayingAmountEnd=${(repayingAmountEnd?c)!}
&lastLoginTimeStart=${(lastLoginTimeStart?string('yyyy-MM-dd HH:mm:ss'))!}&lastLoginTimeEnd=${(lastLoginTimeEnd?string('yyyy-MM-dd HH:mm:ss'))!}
&lastLoginSource=${lastLoginSource!}&pageSize=${pageSize}&index=${index+1}"
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