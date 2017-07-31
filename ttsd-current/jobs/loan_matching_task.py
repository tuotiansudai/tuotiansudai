#!/usr/bin/env python
# -*- coding:utf-8 -*-
from celery import current_app


@current_app.task
def add(x, y):
    return x + y
