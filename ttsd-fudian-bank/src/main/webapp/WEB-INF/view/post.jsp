<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>正在提交数据</title>
</head>
<body>
<form action="http://116.62.202.105:56502${path}" id="postForm" style="display: none;" method="post" >
    <input type="text" name="reqData" value='${message}'>
</form>
</body>
<Strong>正在提交数据......</Strong>
<script type="text/javascript">
    window.onload = function(){
        document.getElementById("postForm").submit();
    }
</script>
</html>