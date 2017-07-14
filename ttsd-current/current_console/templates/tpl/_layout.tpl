<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>{% block title %}日息宝-后台管理{% endblock %}</title>
    <meta content="TAX" name="keywords">
    <meta content="日息宝-后台管理" name="description">
    <link rel="stylesheet" href="/static/libs/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/libs/bootstrap/3.3.5/css/bootstrap-datetimepicker.min.css">
    <!--[if lt IE 9]>
      <script src="/static/libs/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="/static/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="/static/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="/static/libs/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script src="/static/libs/bootstrap/3.3.5/js/bootstrap-datetimepicker.min.js"></script>
    <script src="/static/libs/bootstrap/3.3.5/js/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
    <script src="/static/libs/1000hz-bootstrap-validator/0.11.9/validator.min.js"></script>
    <script src="/static/libs/validate/1.16.0/jquery.validate.min.js"></script>
    <script src="/static/libs/validate/1.16.0/messages_zh.min.js"></script>
    <script src="/static/libs/validate/1.16.0/additional-methods.min.js"></script>
    {% block style_content %}
    {% endblock %}
</head>
<body>

<div class="container-fluid">
    <div class="row">
        <!-- menu sidebar -->
        <div class="col-md-2">
            <div class="panel panel-default">
                <div class="panel-heading">首页</div>
                <ul class="list-group">
                    <li class="list-group-item"><a href="{% url 'console_index' %}">待办任务</a></li>
                </ul>
            </div>

        </div>
        <!-- menu sidebar end -->
        <div class="col-md-10">
            <!-- content area begin -->
            <div id="app">
                {% block content %}{% endblock %}
            </div>
        </div>
        <!-- content area end -->
    </div>
</div>

{% block script_content %}
{% endblock %}
</body>
</html>