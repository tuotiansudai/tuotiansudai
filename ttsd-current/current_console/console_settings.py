# -*- coding: utf-8 -*-
import os

from settings import BASE_DIR

CONSOLE_PATH = 'console/'

# ===signIn module start===
SIGN_IN_HOST = 'http://127.0.0.1'
SIGN_IN_PORT = '5000'
LOGIN_URL = 'http://localhost:9080/login'
# ===signIn module end===

# rest service url
REST_SERVICE_HOST = '127.0.0.1'
REST_SERVICE_PORT = '8000'
REST_PATH = 'rest/'
REST_TIME_OUT = 300

LOGGING_DIR = '/var/log/current_console'

# reload setting for local
setting_local_file = '/workspace/deploy-config/ttsd-current/console_settings.py'
if not os.path.isfile(setting_local_file):
    setting_local_file = os.path.join(BASE_DIR, "current_console/console_settings_local.py")

if os.path.isfile(setting_local_file):
    exec (compile(open(setting_local_file).read(), setting_local_file, 'exec'))

# Application definition

INSTALLED_APPS = [
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.staticfiles',
    'current_console']

MIDDLEWARE = [
    'django.contrib.sessions.middleware.SessionMiddleware',
    'current_console.middleware.TTSDSessionManager',

]

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
                'current_console.context_processors.login_user_name',
            ],
        },
    },
]

# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.11/howto/static-files/
STATIC_URL = '/static/'

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
            'filename': LOGGING_DIR + '/current_console.log',
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
        }
    },
    'loggers': {
        'django': {
            'handlers': ['default'],
            'level': 'DEBUG',
            'propagate': False
        },
        'current_console': {
            'handlers': ['default'],
            'level': 'DEBUG',
            'propagate': False
        },
        'django.request': {
            'handlers': ['default', 'request_handler'],
            'level': 'DEBUG',
            'propagate': False,
        },
    }
}
