<h3><span>抵押物信息</span></h3>
<hr class="top-line">
<div class="form-group"><label class="col-sm-2 control-label">抵押物所在地: </label>
    <div class="col-sm-4"><input name="pledgeLocation" value="${loan.pledgeHouse.pledgeLocation}" type="text" class="form-control ui-autocomplete-input"
                                 datatype="*"
                                 autocomplete="off" errormsg="抵押物所在地不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">不动产登记证明:编号: </label>
    <div class="col-sm-4"><input name="estateRegisterId" value="${loan.pledgeHouse.estateRegisterId}" type="text"
                                 class="form-control ui-autocomplete-input"
                                 datatype="*" autocomplete="off" errormsg="不动产登记证明不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">房本编号: </label>
    <div class="col-sm-4"><input name="propertyCardId" value="${loan.pledgeHouse.propertyCardId}" type="text"
                                 class="form-control ui-autocomplete-input"
                                 datatype="*" autocomplete="off" errormsg="房本编号不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">借款抵押房产面积: </label>
    <div class="col-sm-4"><input name="square" value="${loan.pledgeHouse.square}" type="text" class="form-control ui-autocomplete-input"
                                 datatype="*"
                                 autocomplete="off" errormsg="借款抵押房产面积不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">公证书: </label>
    <div class="col-sm-4"><input name="authenticAct" value="${loan.pledgeHouse.authenticAct}" type="text" class="form-control ui-autocomplete-input"
                                 datatype="*" autocomplete="off" errormsg="公证书不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">借款抵押物估值: </label>
    <div class="col-sm-4"><input name="estimateAmount" value="${loan.pledgeHouse.estimateAmount}" type="text"
                                 class="form-control ui-autocomplete-input"
                                 datatype="*" autocomplete="off" errormsg="借款抵押物估值不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">抵押物借款金额: </label>
    <div class="col-sm-4"><input name="pledgeLoanAmount" value="${loan.pledgeHouse.pledgeLoanAmount}" type="text" class="form-control ui-autocomplete-input"
                                 datatype="*" autocomplete="off" errormsg="抵押物借款金额不能为空"></div>
</div>