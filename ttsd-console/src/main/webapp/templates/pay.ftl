<!DOCTYPE html>
<html lang="en">
<#if pay??>

<head>
    <#if pay.status>
        <script type="text/javascript" src="/js/libs/jquery-1.11.3.min.js"></script>
        <script type="text/javascript">
            $(function () {
                $("#payForm").submit();
            });
        </script>
    </#if>
</head>
<body>
    <#if pay.status>
    <form id="payForm" action="${pay.url}" method="post">
        <#list payData.fields?keys as key>
            <input type="hidden" name="reqData" value="${pay.data}"/>
        </#list>
    </form>
    <#else>
    <p>${pay.message}</p>
    </#if>
</body>
</#if>

</html>