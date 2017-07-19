# coding=utf-8
# -*-coding:utf-8 -*-
import json

from current_console.menu import menus


def menu_settings(request):
    return {'menus': menus}
