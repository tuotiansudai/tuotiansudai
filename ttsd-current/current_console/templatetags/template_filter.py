# coding: utf-8
from django.template.defaulttags import register


@register.filter
def get_item(tuples, key):
    for item in tuples:
        if key == item[0]:
            return item[1]
    return ""


@register.filter
def get_dict_value(dictionary, key):
    return dictionary.get(key, '')


@register.filter
def convert_cent_to_yuan(cent):
    return '{0:.2f}'.format(int(cent)/100.0)
