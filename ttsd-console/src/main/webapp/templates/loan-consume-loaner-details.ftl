<h3><span>借款人基本信息</span></h3>
<hr class="top-line">
<div>
    <div class="form-group">
        <label class="col-sm-2 control-label">借款人姓名: </label>

        <div class="col-sm-4">
            <input name="userName" value="${loanerDto.userName}" disabled type="text" class="form-control" datatype="*" errormsg="借款人姓名不能为空">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款人性别: </label>

        <div class="col-sm-4">
            <select name="gender" class="selectpicker b-width" disabled>
                <option value="MALE" <#if loanerDto.sex='MALE'>selected="selected"</#if>>男</option>
                <option value="FEMALE" <#if loanerDto.sex='FEMALE'>selected="selected"</#if>>女</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款人年龄: </label>

        <div class="col-sm-4">
            <input name="age" value="${loanerDto.age}" type="text" disabled class="form-control" datatype="n1-3" errormsg="借款人年龄填写有误" maxlength="3">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款人身份证号: </label>

        <div class="col-sm-4">
            <input name="identityNumber" value="${loanerDto.identityNumber}" disabled type="text" class="form-control" datatype="idcard" errormsg="借款人身份证号填写有误">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">借款人婚姻情况: </label>

        <div class="col-sm-4">
            <select name="marriage" class="selectpicker b-width" disabled>
                <option value="UNMARRIED" <#if loanerDto.marriage='UNMARRIED'>selected="selected"</#if>>未婚</option>
                <option value="MARRIED" <#if loanerDto.marriage='MARRIED'>selected="selected"</#if>>已婚</option>
                <option value="DIVORCE" <#if loanerDto.marriage='DIVORCE'>selected="selected"</#if>>离异</option>
                <option value="NONE" <#if loanerDto.marriage='NONE'>selected="selected"</#if>>不明</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款人所在地区: </label>

        <div class="col-sm-4">
            <input name="region" value="${loanerDto.address}" type="text" class="form-control" datatype="*" errormsg="借款人所在地区不能为空">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">借款人家庭年收入: </label>

        <div class="col-sm-4">
            <input name="income" value="${(loanerDto.homeIncome * 10000)?c}" disabled type="text" class="form-control" datatype="*" errormsg="借款人年收入不能为空">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">借款人从业情况: </label>

        <div class="col-sm-4">
            <input name="employmentStatus" value="${loanerDto.workPosition}" disabled type="text" class="form-control" datatype="*" errormsg="借款人从业情况不能为空">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款人芝麻信用分: </label>

        <div class="col-sm-4">
            <input value="${loanerDto.sesameCredit!}" disabled type="text" class="form-control">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款人借款金额(元): </label>

        <div class="col-sm-4">
            <input value="${(loanerDto.amount * 10000)?c}" disabled type="text" class="form-control">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款人借款周期: </label>

        <div class="col-sm-4">
            <input value="${loanerDto.period}" disabled type="text" class="form-control">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款用途: </label>

        <div class="col-sm-4">
            <input name="purpose" value="${(loanerDto.loanUsage)!}" type="text" maxlength="10" class="form-control" datatype="*" errormsg="借款用途不能为空">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">资产信息: </label>

        <div class="col-sm-4">
            <input value="${(loanerDto.elsePledge)!}" disabled type="text" class="form-control">
        </div>
    </div>
</div>