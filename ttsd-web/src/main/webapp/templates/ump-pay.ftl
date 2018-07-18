<!DOCTYPE html>
<html lang="en">
<#if (pay.data)??>
    <#assign payData = pay.data>
<head>
    <#if payData.status>
        <script type="text/javascript">
            window.onload=function()  {
                document.getElementById('payForm').submit()
            }
        </script>
    </#if>
</head>
<body>

    <#if pay.status>
    <form id="payForm" action="${pay.url}" method="post">
        <#list pay.fields?keys as key>
            <input type="hidden" name="${key}" value="${pay.fields[key]}"/>
        </#list>
    </form>
    <#else>
    <p>${pay.message!}</p>
    </#if>
</body>
</#if>

</html>