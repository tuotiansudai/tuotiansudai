<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="description" content="">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="yes" name="apple-touch-fullscreen">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta charset="UTF-8" name="viewport" content="width=device-width,initial-scale=1,target-density dpi=high-dpi,user-scalable=no">
    <link href="//cdn.bootcss.com/Swiper/3.3.1/css/swiper.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${css.index!}" charset="utf-8"/>
    <title>mobile api</title>

</head>
<body>
<div id="app"></div>
<script type="text/javascript">
    window.commonStaticServer='${commonStaticServer}';
</script>

<#if (js.jquerydll)??>
<script src="${js.jquerydll}" type="text/javascript" ></script>
</#if>

<#if (js.index)??>
<script src="${js.index!}"></script>
</#if>

</body>
</html>