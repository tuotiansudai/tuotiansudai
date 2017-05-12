<#list loan.pledgeEnterprise as pledgeEnterprise>
    <div class='enterprise-pledge'>
        <#if pledgeEnterprise_index == 0>
            <h3><span class='enterprise-title'>抵押物信息<#if (loan.pledgeEnterprise?size > 1)>${pledgeEnterprise_index+1}</#if></span> <button type='button' class='jq-add-enterprise-pledge btn btn-info' style='margin-left: 10px;'>+</button></h3>
        <#else>
            <h3><span class='enterprise-title'>抵押物信息${pledgeEnterprise_index+1}</span> <button type='button' class='jq-add-enterprise-pledge btn btn-info' style='margin-left: 10px;'>+</button> <button type='button' class='jq-del-enterprise-pledge btn btn-info'>-</button></h3>
        </#if>
        <hr class="top-line">
        <div class="form-group"><label class="col-sm-2 control-label">担保方式: </label>
            <div class="col-sm-4"><input name="guarantee" value="${pledgeEnterprise.guarantee}" type="text" class="form-control"
                                         datatype="*" errormsg="担保方式不能为空"></div>
        </div>
        <div class="form-group"><label class="col-sm-2 control-label">借款抵押物估值: </label>
            <div class="col-sm-4"><input name="estimateAmount" value="${pledgeEnterprise.estimateAmount}" type="text" class="form-control"
                                         datatype="*"  errormsg="借款抵押物估值不能为空"></div>
        </div>
        <div class="form-group"><label class="col-sm-2 control-label">抵押物所在地: </label>
            <div class="col-sm-4"><input name="pledgeLocation" value="${pledgeEnterprise.pledgeLocation}" type="text" class="form-control"
                                         datatype="*"
                                         errormsg="抵押物所在地不能为空"></div>
        </div>
    </div>
</#list>