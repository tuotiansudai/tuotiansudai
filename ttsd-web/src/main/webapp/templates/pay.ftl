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

    <#if payData.status>
    <form id="payForm" action="${payData.url}" method="post">
        <#list payData.fields?keys as key>
            <input type="hidden" name="${key}" value='${payData.fields[key]}'/>
        </#list>
    </form>
    <#else>
    <p>${payData.message!}</p>
    </#if>
</body>
</#if>

</html>