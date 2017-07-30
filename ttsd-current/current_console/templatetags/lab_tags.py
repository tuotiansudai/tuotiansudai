# coding=utf-8
# -*-coding:utf-8 -*-

from django import template

from current_console.menu import menus

register = template.Library()


@register.inclusion_tag('console/tags/head_lab_tag.html')
def head_lab(request):
    head_lab_menus = []
    user_roles_set = set(request.session.get('roles', []))
    for menu in menus:
        for item in menu['sidebar']:
            if item['name'] and item['role'] and _has_role(set(item['role']),
                                                           user_roles_set):
                head_lab_menus.append({'name': menu['name'], 'text': menu['header']['text'], 'link': item['link']})
                break

    return {'head_lab_menus': head_lab_menus}


@register.inclusion_tag('console/tags/side_lab_tag.html')
def side_lab(request):
    side_lab_menus = []
    user_roles_set = set(request.session.get('roles', []))
    for menu in menus:
        if menu['name'] != 'current-manage':
            continue
        for item in menu['sidebar']:
            if item['role'] and _has_role(set(item['role']), user_roles_set):
                side_lab_menus.append({'name': item['name'], 'text': item['text'], 'link': item['link'],
                                       'class': item.get('class', '')})

    return {'side_lab_menus': side_lab_menus}


def _has_role(menu_role, user_role):
    return len(menu_role & user_role) > 0
