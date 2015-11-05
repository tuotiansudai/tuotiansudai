<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
</head>
<body>
<div>
    <table border="1">
        <thead>
        <tr>
            <th>时间</th>
            <th>序号</th>
            <th>用户名</th>
            <th>费用类型</th>
            <th>操作类型</th>
            <th>金额</th>
            <th>余额</th>
            <th>冻结金额</th>
        </tr>
        </thead>
        <tbody>
        <#list userBillModels as userBillModel>
        <tr>
            <td>${(userBillModel.createdTime?string('yyyy-MM-dd'))!}</td>
            <td>${userBillModel.id?string('0')}</td>
            <td>${userBillModel.loginName!''}</td>
            <td>${userBillModel.operationType.getDescription()}</td>
            <td>${userBillModel.businessType.getDescription()}</td>
            <td>${userBillModel.amount/100}</td>
            <td>${userBillModel.balance/100}</td>
            <td>${userBillModel.freeze/100}</td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>