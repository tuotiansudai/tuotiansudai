<h3><span>借款人基本信息</span></h3>
<hr class="top-line">
<div>
    <div class="form-group">
        <label class="col-sm-2 control-label">借款人姓名: </label>

        <div class="col-sm-4">
            <input name="userName" value="${loan.loanerDetails.userName}" type="text" class="form-control" datatype="*" errormsg="借款人姓名不能为空">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款人性别: </label>

        <div class="col-sm-4">
            <select name="gender" class="selectpicker b-width">
                <option value="MALE" <#if loan.loanerDetails.gender='MALE'>selected="selected"</#if>>男</option>
                <option value="FEMALE" <#if loan.loanerDetails.gender='FEMALE'>selected="selected"</#if>>女</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款人年龄: </label>

        <div class="col-sm-4">
            <input name="age" value="${loan.loanerDetails.age}" type="text" class="form-control" datatype="n1-3" errormsg="借款人年龄填写有误" maxlength="3">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款人身份证号: </label>

        <div class="col-sm-4">
            <input name="identityNumber" value="${loan.loanerDetails.identityNumber}" type="text" class="form-control" datatype="idcard" errormsg="借款人身份证号填写有误">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">借款人婚姻情况: </label>

        <div class="col-sm-4">
            <select name="marriage" class="selectpicker b-width">
                <option value="UNMARRIED" <#if loan.loanerDetails.marriage='UNMARRIED'>selected="selected"</#if>>未婚</option>
                <option value="MARRIED" <#if loan.loanerDetails.marriage='MARRIED'>selected="selected"</#if>>已婚</option>
                <option value="DIVORCE" <#if loan.loanerDetails.marriage='DIVORCE'>selected="selected"</#if>>离异</option>
                <option value="NONE" <#if loan.loanerDetails.marriage='NONE'>selected="selected"</#if>>不明</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款人所在地区: </label>

        <div class="col-sm-4">
            <input name="region" value="${loan.loanerDetails.region}" type="text" class="form-control" datatype="*" errormsg="借款人所在地区不能为空">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">收入水平: </label>

        <div class="col-sm-4">
            <input name="income" value="${loan.loanerDetails.income}" type="text" class="form-control" datatype="*" errormsg="收入水平不能为空">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">借款人从业情况: </label>

        <div class="col-sm-4">
            <input name="employmentStatus" value="${loan.loanerDetails.employmentStatus}" type="text" class="form-control" datatype="*" errormsg="借款人从业情况不能为空">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">借款用途: </label>

        <div class="col-sm-4">
            <input name="purpose" value="${(loan.loanerDetails.purpose)!}" type="text" maxlength="10" class="form-control" datatype="*" errormsg="借款用途不能为空">
        </div>
    </div>
</div>