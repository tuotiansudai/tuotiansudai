<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="bank-list.js" headLab="finance-manage" sideLab="bankMange" title="银行卡管理">

<!-- content area begin -->
<div class="col-md-10">

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
                <tr>
                    <th rowspan="2">名称</th>
                    <th rowspan="2">简称</th>
                    <th rowspan="2">图片</th>
                    <th colspan="2">快捷支付限额（元）</th>
                    <th rowspan="2">操作</th>
                </tr>
                <tr>
                    <th>单笔</th>
                    <th>单日</th>
                </tr>
            </thead>
            <tbody>
                <#if bankList??>
                    <#list bankList as bank>
                        <tr>
                            <td>${bank.name!}</td>
                            <td>${bank.bankCode!}</td>
                            <td><span class="webImg"><img id="webUrl" src="${bank.imageUrl!}"/></span></td>
                            <td>${bank.singleAmount/100}</td>
                            <td>${bank.singleDayAmount/100}</td>
                            <td>
                                <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                                    <a href="/finance-manage/bank/${bank.id?c}/edit" >编辑</a>
                                </@security.authorize>
                            </td>
                        </tr>
                    </#list>
                </#if>
            </tbody>
        </table>
    </div>
</div>
<!-- content area end -->
</@global.main>