<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="banner-list.js" headLab="announce-manage" sideLab="bannerMan" title="banner管理">

<!-- content area begin -->
<div class="col-md-10">

    <div class="table-responsive">
      <button class="btn btn-default pull-left bannerAD" type="button" onclick="/banner-manage/create"> 添加banner</button>

        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th rowspan="2">名称</th>
                <th rowspan="2">简称</th>
                <th rowspan="2">图片</th>
                <th colspan="2">快捷支付限额(元)</th>
                <th>单笔</th>
                <th>单日</th>
                <th rowspan="2">操作</th>
            </tr>
            </thead>
            <tbody>
                <#if bankList??>
                    <#list bankList as bank>
                        <tr>
                            <td>${bank.name!}</td>
                            <td>${bank.shorterName!}</td>
                            <td><span class="webImg"><img id="webUrl" src="${bank.imageUrl!}" width="100" height="30"/></span></td>
                            <td>${bank.singleAmount!}</td>
                            <td>${bank.singleDayAmount!}</td>
                            <td>
                                <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                                    <a href="/bank-manage/bank/${bank.id?c}/edit" >编辑</a>
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