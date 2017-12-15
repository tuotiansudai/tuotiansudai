<div class="pledge-box house">

<#if loan.loanerDetail??>
    <div class="section">
        <div class="title">借款人基本信息</div>
        <dl>
            <#list ['性别', '年龄', '婚姻状况', '就业情况', '收入水平', '借款用途', '逾期率'] as key>
                <#if (loan.loanerDetail[key])?? && loan.loanerDetail[key] != '' && loan.loanerDetail[key] != '不明' >
                    <dd><span>${key}：</span><span>${loan.loanerDetail[key]}</span></dd>
                </#if>
            </#list>
        </dl>
    </div>
</#if>

<#if "HOUSE" == loan.pledgeType>
    <#list loan.pledgeHouseDetailList as pledgeHouseDetail>
    <div class="section">
        <div class="title">抵押档案<#if (loan.pledgeHouseDetailList?size > 1)>${pledgeHouseDetail_index+1}</#if></div>
        <#if pledgeHouseDetail??>
            <dl>
            <#list ['抵押物所在地', '房屋面积', '抵押物估值', '抵押物借款金额'] as key>
                <#if pledgeHouseDetail[key]?? && pledgeHouseDetail[key] != ''>
                    <dd><span>${key}：</span><span>${pledgeHouseDetail[key]}</span></dd>
                </#if>
            </#list>
            </dl>
        </#if>
    </div>
    </#list>
</#if>

<#if "VEHICLE" == loan.pledgeType>
    <#list loan.pledgeVehicleDetailList as pledgeVehicleDetail>
    <div class="section">
        <div class="title">抵押档案<#if (loan.pledgeVehicleDetailList?size > 1)>${pledgeVehicleDetail_index+1}</#if></div>
        <#if pledgeVehicleDetail??>
            <dl>
            <#list ['车辆品牌', '车辆型号', '抵押物估值', '抵押物借款金额'] as key>
                <#if pledgeVehicleDetail[key]?? && pledgeVehicleDetail[key] != ''>
                    <dd><span>${key}：</span><span>${pledgeVehicleDetail[key]}</span></dd>
                </#if>
            </#list>
            </dl>
        </#if>
    </div>
    </#list>
</#if>

<#if ["ENTERPRISE_CREDIT","ENTERPRISE_PLEDGE"]?seq_contains(loan.pledgeType)>
    <div class="section">
        <div class="title">借款人基本信息</div>
        <#if loan.loanerEnterpriseDetailsInfo??>
            <dl>
            <#list ['借款人', '公司所在地', '企业借款用途描述'] as key>
                <#if loan.loanerEnterpriseDetailsInfo[key]?? && loan.loanerEnterpriseDetailsInfo[key] != ''>
                    <dd><span>${key}：</span><span>${loan.loanerEnterpriseDetailsInfo[key]}</span></dd>
                </#if>
            </#list>
            </dl>
        </#if>
    </div>
</#if>

<#if "ENTERPRISE_PLEDGE" == loan.pledgeType>
    <#list loan.pledgeEnterpriseDetailList as pledgeEnterpriseDetail>
    <div class="section">
        <div class="title">抵押物信息<#if (loan.pledgeEnterpriseDetailList?size > 1)>${pledgeEnterpriseDetail_index+1}</#if></div>
        <#if pledgeEnterpriseDetail??>
            <dl>
            <#list ['担保方式', '抵押物估值', '抵押物所在地'] as key>
                <#if pledgeEnterpriseDetail[key]?? && pledgeEnterpriseDetail[key] != ''>
                    <dd><span>${key}：</span><span>${pledgeEnterpriseDetail[key]}</span></dd>
                </#if>
            </#list>
            </dl>
        </#if>
    </div>
    </#list>
</#if>

<#if loan.pledgeType == "ENTERPRISE_FACTORING">
    <div class="section">
        <div class="title">借款企业基本信息</div>
        <#if loan.enterpriseInfo??>
            <dl>
            <#list ['企业名称', '经营地址', '借款用途'] as key>
                <#if loan.enterpriseInfo[key]?? && loan.enterpriseInfo[key] != ''>
                    <dd><span>${key}：</span><span>${loan.enterpriseInfo[key]}</span></dd>
                </#if>
            </#list>
            </dl>
        </#if>
    </div>

    <div class="section">
        <div class="title">保理公司基本信息</div>
        <#if loan.enterpriseInfo??>
            <dl>
            <#list ['公司名称','公司简介'] as key>
                <#if loan.enterpriseInfo[key]?? && loan.enterpriseInfo[key] != ''>
                    <dd><span>${key}：</span><span>${loan.enterpriseInfo[key]}</span></dd>
                </#if>
            </#list>
            </dl>
        </#if>
    </div>
</#if>

<#if loan.pledgeType == "ENTERPRISE_BILL">
    <div class="section">
        <div class="title">借款企业基本信息</div>
        <#if loan.enterpriseInfo??>
            <dl>
            <#list ['企业名称', '经营地址', '借款用途'] as key>
                <#if loan.enterpriseInfo[key]?? && loan.enterpriseInfo[key] != ''>
                    <dd><span>${key}：</span><span>${loan.enterpriseInfo[key]}</span></dd>
                </#if>
            </#list>
            </dl>
        </#if>
    </div>
</#if>

    <div class="section">
        <div class="title">风控审核</div>
        <dl>
        <#if loan.pledgeType == 'ENTERPRISE_CREDIT' || loan.pledgeType == 'ENTERPRISE_PLEDGE' >
            <dd><span>法人核证</span><span>法人征信</span></dd>
            <dd><span>股东持股</span><span>验资报告</span></dd>
            <dd><span>公司对公账单</span><span>银行流水查证</span></dd>
            <dd><span>账务报表审计</span><span>税务缴纳</span></dd>
        <#elseif loan.pledgeType == 'ENTERPRISE_FACTORING'>
            <dd><span>实地认证</span><span>应收账质押合同</span></dd>
            <dd><span>企业征信报告</span><span>验资报告</span></dd>
            <dd><span>不动产明细</span><span>企业授信余额</span></dd>
            <dd><span>银行流水查证</span><span>税务缴纳</span></dd>
        <#elseif loan.pledgeType == 'ENTERPRISE_BILL'>
            <dd><span>实地认证</span><span>商业汇票</span></dd>
            <dd><span>企业征信报告</span><span>验资报告</span></dd>
            <dd><span>不动产明细</span><span>企业授信余额</span></dd>
            <dd><span>银行流水查证</span><span>税务缴纳</span></dd>
        <#else>
            <dd><span>身份认证</span><span>手机认证</span></dd>
            <dd><span>婚姻状况认证</span><span>房产认证</span></dd>
            <dd><span>住址信息认证</span><span>收入证明</span></dd>
        </#if>
        </dl>
    </div>
    <div class="look-apply-material">
        <a href="#" class="btn-look">查看申请资料</a>
    </div>
<#list loan.loanTitles as loanTitleRelation >
    <#list loan.loanTitleDto as loanTitle>
        <#if loanTitle.id == loanTitleRelation.titleId>
        <dl class="material-box">
            <dt>${loanTitle.title}</dt>
            <dd>
            <#list loanTitleRelation.applicationMaterialUrls?split(",") as title>
                <img src="${commonStaticServer}${title}" alt="${loanTitle.title}"/>
            </#list>
            </dd>
        </dl>
        </#if>
    </#list>
</#list>

    <div class="notice-toggle">
        <p>声明：${loan.declaration!}</p>

        <div class="btn-detail-toggle">收起详情</div>
    </div>

</div>

