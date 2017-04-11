<h3><span>借款人基本信息</span></h3>
<hr class="top-line">
<div>
    <div class="form-group">
        <label class="col-sm-2 control-label">借款人: </label>

        <div class="col-sm-3">
            <input name="juristicPerson" value="${loan.loanerEnterpriseDetails.juristicPerson}" type="text" class="form-control"
                                     datatype="*" errormsg="借款人不能为空">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">公司所在地: </label>

        <div class="col-sm-3">
            <input name="address" value="${loan.loanerEnterpriseDetails.address}" type="text" class="form-control" datatype="*"
                                      errormsg="公司所在地不能为空">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label">企业借款用途描述: </label>

        <div class="col-sm-3">
            <input name="purpose" value="${loan.loanerEnterpriseDetails.purpose}" type="text" class="form-control" maxlength="100" datatype="*"
                   errormsg="企业借款用途描述不能为空">
        </div>
    </div>
</div>