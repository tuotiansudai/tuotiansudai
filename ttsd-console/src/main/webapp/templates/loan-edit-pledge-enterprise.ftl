<h3><span>抵押物信息</span></h3>
<hr class="top-line">
<div class="form-group"><label class="col-sm-2 control-label">担保方式: </label>
    <div class="col-sm-4"><input name="guarantee" value="${loan.pledgeEnterprise.guarantee}" type="text" class="form-control ui-autocomplete-input"
                                 datatype="*" autocomplete="off" errormsg="担保方式不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">借款抵押物估值: </label>
    <div class="col-sm-4"><input name="estimateAmount" value="${loan.pledgeEnterprise.estimateAmount}" type="text" class="form-control ui-autocomplete-input"
                                 datatype="*" autocomplete="off" errormsg="借款抵押物估值不能为空"></div>
</div>
<div class="form-group"><label class="col-sm-2 control-label">抵押物所在地: </label>
    <div class="col-sm-4"><input name="pledgeLocation" value="${loan.pledgeEnterprise.pledgeLocation}" type="text" class="form-control ui-autocomplete-input"
                                 datatype="*"
                                 autocomplete="off" errormsg="抵押物所在地不能为空"></div>
</div>