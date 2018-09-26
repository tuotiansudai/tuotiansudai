<#list loan.pledgeVehicle as pledgeVehicle>
    <div class='vehicle-pledge'>
        <#if pledgeVehicle_index == 0>
            <h3><span class='vehicle-title'>车辆信息<#if (loan.pledgeVehicle?size > 1)>${pledgeVehicle_index+1}</#if></span><button type='button' class='jq-add-vehicle-pledge btn btn-info' style='margin-left: 10px;'>+</button></h3>
        <#else>
            <h3><span class='vehicle-title'>车辆信息${pledgeVehicle_index+1}</span> <button type='button' class='jq-add-vehicle-pledge btn btn-info' style='margin-left: 10px;'>+</button> <button type='button' class='jq-del-vehicle-pledge btn btn-info'>-</button></h3>
        </#if>

        <hr class="top-line">
        <div class="form-group"><label class="col-sm-2 control-label">抵押物所在地: </label>
            <div class="col-sm-4"><input name="pledgeLocation" value="${pledgeVehicle.pledgeLocation}" type="text" class="form-control" datatype="*"
                                         errormsg="抵押物所在地不能为空"></div>
        </div>
        <div class="form-group"><label class="col-sm-2 control-label">借款抵押车辆型号: </label>
            <div class="col-sm-4"><input name="model" value="${pledgeVehicle.model}" type="text" class="form-control" datatype="*"
                                         errormsg="抵押车辆型号不能为空"></div>
        </div>
        <div class="form-group"><label class="col-sm-2 control-label">借款抵押车辆品牌: </label>
            <div class="col-sm-4"><input name="brand" value="${pledgeVehicle.brand}" type="text" class="form-control" datatype="*"
                                         errormsg="抵押车辆品牌不能为空"></div>
        </div>
        <div class="form-group"><label class="col-sm-2 control-label">借款抵押物估值: </label>
            <div class="col-sm-4"><input name="estimateAmount" value="${pledgeVehicle.estimateAmount}" type="text" class="form-control"
                                         datatype="*" errormsg="借款抵押物估值不能为空"></div>
        </div>
        <div class="form-group"><label class="col-sm-2 control-label">抵押物借款金额: </label>
            <div class="col-sm-4"><input name="pledgeLoanAmount" value="${pledgeVehicle.pledgeLoanAmount}" type="text" class="form-control"
                                         datatype="*" errormsg="抵押物借款金额不能为空"></div>
        </div>
    </div>
</#list>