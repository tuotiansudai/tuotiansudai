<!DOCTYPE html>
<html>
<body>
<form>
    <p>自动投标</p>
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
</body>
</html>