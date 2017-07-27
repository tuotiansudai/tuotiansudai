# -*- coding: utf-8 -*-
"""
Django settings for current project.

Generated by 'django-admin startproject' using Django 1.11.3.

For more information on this file, see
https://docs.djangoproject.com/en/1.11/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/1.11/ref/settings/
"""

import os

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
BASE_DIR = os.path.dirname(os.path.abspath(__file__))

# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.11/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = 't@7h4!s_wi!zf*86!$)%xtu$_#7(rmd-)4d&(u20vdmjvu&_(('

# ENVIRONMENT
PRODUCT = False

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = not PRODUCT

ALLOWED_HOSTS = ["*"]

ROOT_URLCONF = 'urls'

WSGI_APPLICATION = 'wsgi.application'

REDIS_URL = 'redis://192.168.33.10/2'

# Internationalization
# https://docs.djangoproject.com/en/1.11/topics/i18n/
LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'Asia/Shanghai'

USE_I18N = True

USE_L10N = True

USE_TZ = False

REST_ENABLED = os.environ.get('REST_ENABLED', True)

CONSOLE_ENABLED = os.environ.get('CONSOLE_ENABLED', False)

# reload setting for local
if REST_ENABLED:
    rest_settings = os.path.join(BASE_DIR, "current_rest/rest_settings.py")
    exec (compile(open(rest_settings).read(), rest_settings, 'exec'))

if CONSOLE_ENABLED:
    console_settings = os.path.join(BASE_DIR, "current_console/console_settings.py")
    exec (compile(open(console_settings).read(), console_settings, 'exec'))
