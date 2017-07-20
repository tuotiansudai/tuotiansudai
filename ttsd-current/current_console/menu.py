# coding=utf-8
# -*-coding:utf-8 -*-
menus = [
    {
        "name": "sys-manage",
        "header": {"text": "系统首页", "link": "/"},
        "sidebar":
            [
                {"name": "", "class": "sub-title-1", "text": "我的任务", "link": "",
                 "role": "'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE', 'ASK_ADMIN'"},
                {"name": "myTasks", "text": "我的任务", "link": "/",
                 "role": "'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE', 'ASK_ADMIN'"}
            ]
    },
    {
        "name": "project-manage",
        "header": {"text": "项目管理", "link": "/project-manage/loan-list"},
    },
    {
        "name": "user-manage",
        "header": {"text": "用户管理", "link": "/user-manage/users"},

    },
    {
        "name": "finance-manage",
        "header": {"text": "财务管理", "link": "/finance-manage/invests"},

    },
    {
        "name": "content-manage",
        "header": {"text": "内容管理", "link": "/message-manage/manual-message"},

    },
    {
        "name": "security",
        "header": {"text": "安全管理", "link": "/security-log/login-log"},

    },
    {
        "name": "activity-manage",
        "header": {"text": "活动管理", "link": "/activity-manage/red-envelope"},
    },
    {
        "name": "membership-manage",
        "header": {"text": "会员管理", "link": "/membership-manage/membership-list"},

    },
    {
        "name": "statistic",
        "header": {"text": "平台数据", "link": "/statistic"},
    },
    {
        "name": "point-manage",
        "header": {"text": "积分管理", "link": "/point-manage/user-point-list"},
    },
    {
        "name": "ask-manage",
        "header": {"text": "问答管理", "link": "/ask-manage/questions"},
    },
    {
        "name": "customer-center",
        "header": {"text": "客服中心", "link": "/user-manage/users-search"},

    },
    {
        "name": "experience-manage",
        "header": {"text": "体验金管理", "link": "/experience-manage/balance"},
    },
    {
        "name": "current-manage",
        "header": {"text": "日息宝管理", "link": "/console/create-loan"},
        "sidebar": [
            {"name": "", "class": "sub-title-1", "text": "资产管理", "link": "",
             "role": "'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE','DATA'"},
            {"name": "currentHome", "text": "添加资产", "link": "/console/create-loan",
             "role": "'ADMIN','OPERATOR','OPERATOR_ADMIN','CUSTOMER_SERVICE','DATA'"}
        ]
    }
]
