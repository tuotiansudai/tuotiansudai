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

<header class="navbar" id="top" role="banner">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="" class="navbar-brand">
                <img src="/static/images/logo.png" alt="" />
            </a>
        </div>
        <div class="collapse navbar-collapse">
            <p class="navbar-text navbar-right"><a id="logout-link" href="/logout">注销</a>【沙漏考拉】</p>
            <form id="logout-form" action="/logout" method="post">
            </form>
        </div>
    </div>
    <nav id="bs-navbar" class="collapse navbar-collapse top-menu">
            <div class="container-fluid">
                <ul class="nav navbar-nav">
                    {% for menu in menus %}
                            <li {% if menu.name == 'current-manage' %} class="active" {% endif %}>
                                <a href="{{menu.header.link}}">{{menu.header.text}}</a>
                            </li>
                    {% endfor %}

                </ul>
            </div>
     </nav>
</header>
<!-- main begin -->
<div class="main">
    <div class="container-fluid">
        <div class="row">
            <!-- menu sidebar -->
            <div class="col-md-2">
                <ul class="nav sidenav">
                    {% for menu in menus %}
                        {% if menu.name == 'current-manage' %}
                            {% for item in menu.sidebar%}
                                <li class="{{item.class}}" >
                                    {% if item.link != '' %}
                                        <a href="{{item.link}}">{{item.text}}</a>
                                    {% else %}
                                        {{item.text}}
                                    {% endif%}
                                </li>
                            {% endfor %}
                        {% endif %}
                    {% endfor%}
                </ul>
            </div>
            <!-- menu sidebar end -->

            <!-- content area begin -->
            {% block content %}{% endblock %}
            <!-- content area end -->
        </div>
    </div>
</div>
<!-- main end -->



{% block script_content %}
{% endblock %}
</body>
</html>