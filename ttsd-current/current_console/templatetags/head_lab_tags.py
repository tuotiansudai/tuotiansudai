# coding=utf-8
# -*-coding:utf-8 -*-

from django import template

from current_console.menu import menus

register = template.Library()


@register.inclusion_tag('console/tags/head_lab_tag.html')
def head_lab(request):
    head_lab_menus = []
    user_roles_set = set(request.session['roles'] if 'roles' in request.session else None)
    for menu in menus:
        for item in menu['sidebar']:
            if item['name'] != '' \
                    and item['role'] != '' \
                    and _has_role(set(item['role']),
                                  user_roles_set):
                head_lab_menus.append({'name': menu['name'], 'text': menu['header']['text'], 'link': item['link']})
                break

    return {'head_lab_menus': head_lab_menus}


def _has_role(menu_role, user_role):
    return True if len(menu_role & user_role) > 0 else False
