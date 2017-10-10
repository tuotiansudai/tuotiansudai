<#list loan.pledgeHouse as pledgeHouse>
    <div class='house-pledge'>
        <#if pledgeHouse_index == 0>
            <h3><span class='house-title'>抵押物信息<#if (loan.pledgeHouse?size > 1)>${pledgeHouse_index+1}</#if></span> <button type='button' class='jq-add-house-pledge btn btn-info' style='margin-left: 10px;'>+</button></h3>
        <#else>
            <h3><span class='house-title'>抵押物信息${pledgeHouse_index+1}</span> <button type='button' class='jq-add-house-pledge btn btn-info' style='margin-left: 10px;'>+</button> <button type='button' class='jq-del-house-pledge btn btn-info'>-</button></h3>
        </#if>

        <hr class="top-line">
        <div class="form-group"><label class="col-sm-2 control-label">抵押物所在地: </label>
            <div class="col-sm-4"><input name="pledgeLocation" value="${pledgeHouse.pledgeLocation}" type="text" class="form-control"
                                         datatype="*"
                                         errormsg="抵押物所在地不能为空"></div>
        </div>
        <div class="form-group"><label class="col-sm-2 control-label">不动产登记证明:编号: </label>
            <div class="col-sm-4"><input name="estateRegisterId" value="${pledgeHouse.estateRegisterId}" type="text"
                                         class="form-control"
                                         datatype="*" errormsg="不动产登记证明不能为空"></div>
        </div>

        <#if (pledgeHouse.propertyCardId)??>
            <div class="form-group"><label class="col-sm-2 control-label">房产证编号: </label>
                <div class="col-sm-4"><input name="propertyCardId" value="${pledgeHouse.propertyCardId}" type="text"
                                             class="form-control"
                                             datatype="*" errormsg="房产证编号不能为空"></div>

            </div>
        <#else>
            <div class="form-group"><label class="col-sm-2 control-label">房权证编号: </label>
                <div class="col-sm-4"><input name="propertyCardId" value="${pledgeHouse.propertyRightCertificateId}" type="text"
                                             class="form-control"
                                             datatype="*" errormsg="房权证编号不能为空"></div>

            </div>
        </#if>
        <div class="form-group"><label class="col-sm-2 control-label">借款抵押房产面积: </label>
            <div class="col-sm-4"><input name="square" value="${pledgeHouse.square}" type="text" class="form-control"
                                         datatype="*"
                                         errormsg="借款抵押房产面积不能为空"></div>
        </div>
        <div class="form-group"><label class="col-sm-2 control-label">公证书: </label>
            <div class="col-sm-4"><input name="authenticAct" value="${pledgeHouse.authenticAct}" type="text" class="form-control"
                                         datatype="*" errormsg="公证书不能为空"></div>
        </div>
        <div class="form-group"><label class="col-sm-2 control-label">借款抵押物估值: </label>
            <div class="col-sm-4"><input name="estimateAmount" value="${pledgeHouse.estimateAmount}" type="text"
                                         class="form-control"
                                         datatype="*" errormsg="借款抵押物估值不能为空"></div>
        </div>
        <div class="form-group"><label class="col-sm-2 control-label">抵押物借款金额: </label>
            <div class="col-sm-4"><input name="pledgeLoanAmount" value="${pledgeHouse.pledgeLoanAmount}" type="text" class="form-control"
                                         datatype="*" errormsg="抵押物借款金额不能为空"></div>
        </div>
    </div>
</#list>