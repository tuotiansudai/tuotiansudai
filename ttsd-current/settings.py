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
DEBUG = True

ALLOWED_HOSTS = ["*"]

REST_ENABLED = True
REST_PATH = 'rest/'

CONSOLE_ENABLED = True
CONSOLE_PATH = 'console/'

# database config

DB_MYSQL_DATABASE = 'edxcurrent'
DB_MYSQL_HOST = '192.168.33.10'
DB_MYSQL_PORT = '3306'
DB_MYSQL_USER = 'root'
DB_MYSQL_PASSWORD = 'root'

# rest service url
REST_SERVICE_HOST = '127.0.0.1'
REST_SERVICE_PORT = '8000'
REST_TIME_OUT = 500

# ===signIn module start===
SIGN_IN_HOST = 'http://127.0.0.1'
SIGN_IN_PORT = '5000'
REDIRECT_URL = 'http://localhost:9080/login'
# ===signIn module end===
LOGGING_DIR = '/var/log/current_rest'

PAY_WRAPPER_HOST = 'http://localhost:8080/current'

# reload setting for local
setting_local_file = '/workspace/deploy-config/ttsd-current/settings_local.py'
if not os.path.isfile(setting_local_file):
    setting_local_file = os.path.join(BASE_DIR, "settings_local.py")
if os.path.isfile(setting_local_file):
    exec (compile(open(setting_local_file).read(), setting_local_file, 'exec'))

# Application definition

INSTALLED_APPS = [
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
]

if not PRODUCT:
    INSTALLED_APPS += ['django_extensions']

if REST_ENABLED:
    INSTALLED_APPS += ['rest_framework', 'current_rest']

if CONSOLE_ENABLED:
    INSTALLED_APPS += [
        'current_console']

MIDDLEWARE = [
    # 'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    # 'django.middleware.common.CommonMiddleware',
    # 'django.middleware.csrf.CsrfViewMiddleware',
    # 'django.contrib.auth.middleware.AuthenticationMiddleware',
    # 'django.contrib.messages.middleware.MessageMiddleware',
    # 'django.middleware.clickjacking.XFrameOptionsMiddleware',
    # 'current_console.middleware.TTSDSessionManager',
]

ROOT_URLCONF = 'urls'

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [],
        'APP_DIRS': True,
        'OPTIONS': {
            'context_processors': [
                'django.template.context_processors.debug',
                'django.template.context_processors.request',
                'django.contrib.auth.context_processors.auth',
                'django.contrib.messages.context_processors.messages',
            ],
        },
    },
]

WSGI_APPLICATION = 'wsgi.application'

# Database
# https://docs.djangoproject.com/en/1.11/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': DB_MYSQL_DATABASE,
        'HOST': DB_MYSQL_HOST,
        'PORT': DB_MYSQL_PORT,
        'USER': DB_MYSQL_USER,
        'PASSWORD': DB_MYSQL_PASSWORD,
        'OPTIONS': {
            'sql_mode': 'TRADITIONAL',
            'charset': 'utf8',
            'init_command': 'SET '
                            'character_set_connection=utf8,'
                            'collation_connection=utf8_bin',
        }
    }
}

# Password validation
# https://docs.djangoproject.com/en/1.11/ref/settings/#auth-password-validators

# AUTH_PASSWORD_VALIDATORS = [
#     {
#         'NAME': 'django.contrib.auth.password_validation.UserAttributeSimilarityValidator',
#     },
#     {
#         'NAME': 'django.contrib.auth.password_validation.MinimumLengthValidator',
#     },
#     {
#         'NAME': 'django.contrib.auth.password_validation.CommonPasswordValidator',
#     },
#     {
#         'NAME': 'django.contrib.auth.password_validation.NumericPasswordValidator',
#     },
# ]

# Internationalization
# https://docs.djangoproject.com/en/1.11/topics/i18n/

LANGUAGE_CODE = 'en-us'

TIME_ZONE = 'Asia/Shanghai'

USE_I18N = True

USE_L10N = True

USE_TZ = True

# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.11/howto/static-files/

STATIC_URL = '/static/'

REST_FRAMEWORK = {
    # 'DEFAULT_RENDERER_CLASSES': ['rest_framework.renderers.JSONRenderer'],
    'DEFAULT_PERMISSION_CLASSES': ['rest_framework.permissions.AllowAny'],
    'DEFAULT_AUTHENTICATION_CLASSES': ['current_rest.authentication.NoAuthentication'],
    'EXCEPTION_HANDLER': 'current_rest.exceptions.api_exception_handler',
    'PAGE_SIZE': 10
}

LOGGING = {
    'version': 1,
    'disable_existing_loggers': False,
    'formatters': {
        'standard': {
            'format': '%(asctime)s [%(name)s] [%(module)s:%(funcName)s:%(lineno)d] [%(levelname)s]- %(message)s'
        }
    },
    'filters': {
    },
    'handlers': {
        'default': {
            'level': 'DEBUG',
            'class': 'logging.handlers.TimedRotatingFileHandler',
            'filename': LOGGING_DIR + '/current_rest.log',
            'when': 'midnight',
            'backupCount': 5,
            'formatter': 'standard',
        },
        'request_handler': {
            'level': 'DEBUG',
            'class': 'logging.handlers.TimedRotatingFileHandler',
            'filename': LOGGING_DIR + '/request.log',
            'when': 'midnight',
            'backupCount': 5,
            'formatter': 'standard',
        },
        'db_handler': {
            'level': 'DEBUG',
            'class': 'logging.handlers.TimedRotatingFileHandler',
            'filename': LOGGING_DIR + '/db.log',
            'when': 'midnight',
            'backupCount': 5,
            'formatter': 'standard',
        }
    },
    'loggers': {
        'django': {
            'handlers': ['default'],
            'level': 'DEBUG',
            'propagate': False
        },
        'django.request': {
            'handlers': ['default', 'request_handler'],
            'level': 'DEBUG',
            'propagate': False,
        },
        'django.db.backends': {
            'handlers': ['default', 'db_handler'],
            'level': 'DEBUG',
        },
        'current_rest': {
            'handlers': ['default'],
            'level': 'DEBUG',
            'propagate': False
        },
    }
}
