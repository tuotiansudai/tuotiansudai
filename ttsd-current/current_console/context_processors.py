# coding=utf-8
# -*-coding:utf-8 -*-
import json

from current_console.menu import menus


def login_user_name(request):
    return {'login_name': request.session.get('login_name'), 'roles': request.session.get('roles')}
