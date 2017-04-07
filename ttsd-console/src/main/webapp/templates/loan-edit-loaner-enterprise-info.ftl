<h3><span>借款人基本信息</span></h3>
<hr class="top-line">
<div>
    <div class="form-group">
        <label class="col-sm-2 control-label">借款企业名称: </label>

        <div class="col-sm-3">
            <input name="companyName" value="${loan.loanerEnterpriseInfo.companyName}" type="text" class="form-control"
                                     datatype="*" errormsg="借款企业名称">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款企业营业地址: </label>

        <div class="col-sm-3">
            <input name="address" value="${loan.loanerEnterpriseInfo.address}" type="text" class="form-control" datatype="*"
                                      errormsg="借款企业营业地址不能为空">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">借款用途: </label>

        <div class="col-sm-3">
            <textarea name="purpose"
                      class="form-control"
                      datatype="*"
                      maxlength="100"
                      errormsg="借款用途不能为空">${loan.loanerEnterpriseInfo.purpose}</textarea>
        </div>
    </div>
</div>