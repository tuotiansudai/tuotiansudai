<h3><span>抵押物信息</span></h3>
<hr class="top-line">
<div class="form-group"><label class="col-sm-2 control-label">抵押物所在地: </label>
    <div class="col-sm-4"><input name="pledgeLocation" value="${loan.pledgeVehicle.pledgeLocation}" type="text" class="form-control" datatype="*"
                                 errormsg="抵押物所在地不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">借款抵押车辆型号: </label>
    <div class="col-sm-4"><input name="model" value="${loan.pledgeVehicle.model}" type="text" class="form-control" datatype="*"
                                 errormsg="抵押车辆型号不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">借款抵押车辆品牌: </label>
    <div class="col-sm-4"><input name="brand" value="${loan.pledgeVehicle.brand}" type="text" class="form-control" datatype="*"
                                 errormsg="抵押车辆品牌不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">借款抵押物估值: </label>
    <div class="col-sm-4"><input name="estimateAmount" value="${loan.pledgeVehicle.estimateAmount}" type="text" class="form-control"
                                 datatype="*" errormsg="借款抵押物估值不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">抵押物借款金额: </label>
    <div class="col-sm-4"><input name="pledgeLoanAmount" value="${loan.pledgeVehicle.pledgeLoanAmount}" type="text" class="form-control"
                                 datatype="*" errormsg="抵押物借款金额不能为空"></div>
</div>