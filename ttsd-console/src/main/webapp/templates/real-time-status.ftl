<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="real-time-status.js" headLab="finance-manage" sideLab="realTimeStatus" title="联动优势余额查询">

<div class="col-md-10">
    <form action="" method="get" class="form-inline query-build">
        <div class="form-group">
            <label class="control-label">账户类型: </label>&nbsp;&nbsp;
            <input type="radio" name="role" value="INVESTOR"
                   <#if role?? && role == 'INVESTOR'>checked="checked"</#if>
            />联动优势 &nbsp;&nbsp;
            <input type="radio" name="role" value="BANK_LOANER"
                   <#if role?? && role=='BANK_LOANER'>checked="checked"</#if>
            />富滇银行-借款人 &nbsp;&nbsp;
            <input type="radio" name="role" value="BANK_INVESTOR"
                   <#if !role?? || role=='BANK_INVESTOR'>checked="checked"</#if>
            />富滇银行-出借人 &nbsp;&nbsp;

        </div>
        <br/>
        <div class="form-group">
            <label for="type">查询类型</label>
            <select class="selectpicker" id="type" name="type">
                <option value="user" <#if !(type??) || type=='user'>selected</#if>>用户账户</option>
                <option value="loan" <#if type?? && type=='loan'>selected</#if>>标的账户</option>
                <option value="trade" <#if type?? && type=='trade'>selected</#if>>交易状态</option>
            </select>
        </div>

        <div class="login-name form-group" <#if type?? && type != 'user'>style="display: none"</#if>>
            <label for="loginNameOrMobile">手机号或用户ID</label>
            <input type="text" id="loginNameOrMobile" name="loginNameOrMobile" class="form-control" value="${loginNameOrMobile!}"/>
        </div>

        <div class="loan form-group" <#if !(type??) || type != 'loan'>style="display: none"</#if>>
            <label for="loanId">标的号</label>
            <input type="text" id="loanId" name="loanId" class="form-control"
                    <#if type?? && type == 'loan'>value="${loanId?c}"</#if>/>
        </div>

        <div class="transfer form-group" <#if !(type??) || type != 'trade'>style="display: none"</#if>>
            <label for="queryTradeType">交易类型</label>
            <select class="selectpicker" id="queryTradeType" name="queryTradeType">
                <option value="RECHARGE" <#if !(queryTradeType??) || queryTradeType=='RECHARGE'>selected</#if>>充值</option>
                <option value="WITHDRAW" <#if queryTradeType?? && queryTradeType=='WITHDRAW'>selected</#if>>提现</option>
                <option value="LOAN_INVEST" <#if queryTradeType?? && queryTradeType=='LOAN_INVEST'>selected</#if>>投标</option>
                <option value="LOAN_REPAY" <#if queryTradeType?? && queryTradeType=='LOAN_REPAY'>selected</#if>>借款人还款</option>
                <option value="LOAN_CALLBACK" <#if queryTradeType?? && queryTradeType=='LOAN_CALLBACK'>selected</#if>>投资人回款</option>
                <option value="LOAN_CREDIT_INVEST" <#if queryTradeType?? && queryTradeType=='LOAN_CREDIT_INVEST'>selected</#if>>债权认购</option>
                <option value="LOAN_FULL" <#if queryTradeType?? && queryTradeType=='LOAN_FULL'>selected</#if>>满标放款</option>
            </select>
        </div>

        <div class="transfer form-group" <#if !(type??) || type != 'trade'>style="display: none"</#if>>
            <label for="orderNo">订单号</label>
            <input type="text" id="orderNo" name="orderNo" class="form-control" value="${orderNo!}"/>
        </div>

        <div class="transfer form-group" <#if !(type??) || type != 'trade'>style="display: none"</#if>>
            <label for="orderDate">交易时间</label>
            <div class='input-group date' id="orderDate">
                <input type='text' class="form-control" name="orderDate" value="${(orderDate?string('yyyy-MM-dd'))!}" readonly/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>

        <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
    </form>

    <#if type?? && type=='user' && data??>
        <div class="panel panel-default">
            <div class="panel-body form-horizontal">
                <#if data.status>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">存管用户名</label>
                        <div class="col-sm-3"><p class="form-control-static">${data.bankUserName}</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">存管账户名</label>
                        <div class="col-sm-3"><p class="form-control-static">${data.bankAccountName}</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">授权</label>
                        <div class="col-sm-3"><p class="form-control-static">投资授权: ${data.loanInvestAuthorization?string('是', '否')} 还款授权: ${data.loanRepayAuthorization?string('是', '否')}</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">可用余额</label>
                        <div class="col-sm-3"><p class="form-control-static">${(data.balance/100)?string["0.##"]}元</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">可提现余额</label>
                        <div class="col-sm-3"><p class="form-control-static">${(data.withdrawBalance/100)?string["0.##"]}元</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">冻结金额</label>
                        <div class="col-sm-3"><p class="form-control-static">${(data.freezeBalance/100)?string["0.##"]}元</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">身份证号</label>
                        <div class="col-sm-3"><p class="form-control-static">${data.identityCode}</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">账户状态</label>
                        <div class="col-sm-3"><p class="form-control-static">${data.userStatus}</p></div>
                    </div>
                <#else>
                    ${data.message}
                </#if>
            </div>
        </div>
    </#if>

    <#if type?? && type=='loan' && data??>
        <div class="panel panel-default">
            <div class="panel-body form-horizontal">
                <#if data.status>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">存管标的ID</label>
                        <div class="col-sm-3"><p class="form-control-static">${data.loanTxNo}</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">存管标的账户号</label>
                        <div class="col-sm-3"><p class="form-control-static">${data.loanAccNo}</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">筹款金额</label>
                        <div class="col-sm-3"><p class="form-control-static">${(data.amount/100)?string["0.##"]}元</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">账户余额</label>
                        <div class="col-sm-3"><p class="form-control-static">${(data.balance/100)?string["0.##"]}元</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">状态</label>
                        <div class="col-sm-3"><p class="form-control-static">${data.loanStatus}</p></div>
                    </div>
                <#else>
                    ${data.message}
                </#if>
            </div>
        </div>
    </#if>

    <#if type?? && type=='trade' && data??>
        <div class="panel panel-default">
            <div class="panel-body form-horizontal">
                <#if data.status>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">银行订单号</label>
                        <div class="col-sm-3"><p class="form-control-static">${data.bankOrderNo}</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">订单日期</label>
                        <div class="col-sm-3"><p class="form-control-static">${data.bankOrderDate}</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">订单状态</label>
                        <div class="col-sm-3"><p class="form-control-static">${data.queryStatus}</p></div>
                    </div>
                <#else>
                    ${data.message}
                </#if>
            </div>
        </div>
    </#if>
</div>

</@global.main>