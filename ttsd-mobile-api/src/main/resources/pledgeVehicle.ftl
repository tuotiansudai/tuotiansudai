<#if loaner?? && loaner !="">
    <p style="margin-top: 0px; margin-bottom: 15px; padding: 0px; list-style: none; outline: 0px; color: rgb(76, 76, 76); font-family: &#39;Microsoft YaHei&#39;, Helvetica, sans-serif; font-size: 13px; line-height: 26px; white-space: normal; background: rgb(255, 255, 255);">
        <span style="margin: 0px; padding: 0px; list-style: none; outline: 0px; font-family: 宋体; font-size: 12px;">借款人：${loaner!}</span><br style="margin: 0px; padding: 0px;"/>
        <span style="margin: 0px; padding: 0px; list-style: none; outline: 0px; font-family: 宋体; font-size: 12px;"></span>
    </p>
</#if>

<#if loanAmount?? && loanAmount !="">
<p style="margin-top: 0px; margin-bottom: 15px; padding: 0px; list-style: none; outline: 0px; color: rgb(76, 76, 76); font-family: &#39;Microsoft YaHei&#39;, Helvetica, sans-serif; font-size: 13px; line-height: 26px; white-space: normal; background: rgb(255, 255, 255);">
    <span style="margin: 0px; padding: 0px; list-style: none; outline: 0px; font-family: 宋体; font-size: 12px;">抵押借款金额：${loanAmount!}</span><br style="margin: 0px; padding: 0px;"/>
    <span style="margin: 0px; padding: 0px; list-style: none; outline: 0px; font-family: 宋体; font-size: 12px;"></span>
</p>
</#if>

<#if estimateAmount?? && estimateAmount !="">
<p style="margin-top: 0px; margin-bottom: 15px; padding: 0px; list-style: none; outline: 0px; color: rgb(76, 76, 76); font-family: &#39;Microsoft YaHei&#39;, Helvetica, sans-serif; font-size: 13px; line-height: 26px; white-space: normal; background: rgb(255, 255, 255);">
    <span style="margin: 0px; padding: 0px; list-style: none; outline: 0px; font-family: 宋体; font-size: 12px;">借款抵押物估值：${estimateAmount!}</span><br style="margin: 0px; padding: 0px;"/>
    <span style="margin: 0px; padding: 0px; list-style: none; outline: 0px; font-family: 宋体; font-size: 12px;"></span>
</p>
</#if>
<#if model?? && model !="">
<p style="margin-top: 0px; margin-bottom: 15px; padding: 0px; list-style: none; outline: 0px; color: rgb(76, 76, 76); font-family: &#39;Microsoft YaHei&#39;, Helvetica, sans-serif; font-size: 13px; line-height: 26px; white-space: normal; background: rgb(255, 255, 255);">
    <span style="margin: 0px; padding: 0px; list-style: none; outline: 0px; font-family: 宋体; font-size: 12px;">借款抵押物车辆型号：${model!}</span><br style="margin: 0px; padding: 0px;"/>
    <span style="margin: 0px; padding: 0px; list-style: none; outline: 0px; font-family: 宋体; font-size: 12px;"></span>
</p>
</#if>
<#if declaration?? && declaration != "">
<p style="margin-top: 0px; margin-bottom: 15px; padding: 0px; list-style: none; outline: 0px; color: rgb(76, 76, 76); font-family: &#39;Microsoft YaHei&#39;, Helvetica, sans-serif; font-size: 13px; line-height: 26px; white-space: normal; background: rgb(255, 255, 255);">
    <span style="margin: 0px; padding: 0px; list-style: none; outline: 0px; font-family: 宋体; font-size: 12px;">声明：${declaration!}</span><br style="margin: 0px; padding: 0px;"/>
    <span style="margin: 0px; padding: 0px; list-style: none; outline: 0px; font-family: 宋体; font-size: 12px;"></span>
</p>
</#if>
