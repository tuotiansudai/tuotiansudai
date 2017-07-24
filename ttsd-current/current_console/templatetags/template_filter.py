# coding: utf-8
from django.template.defaulttags import register


@register.filter
def get_item(tuples, key):
    for tuple in tuples:
        if key == tuple[0]:
            return tuple[1]
    return ""
