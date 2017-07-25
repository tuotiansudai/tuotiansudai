# coding=utf-8
# -*-coding:utf-8 -*-
import json

from current_console.menu import menus


def login_user_name(request):
    return {'login_name': request.session['login_name'] if 'login_name' in request.session else None,
            'roles': request.session['roles'] if 'roles' in request.session else None}
