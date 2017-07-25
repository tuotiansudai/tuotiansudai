# -*- coding: utf-8 -*-
import os

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
from settings import BASE_DIR

REST_PATH = 'rest/'

# database config
DB_MYSQL_DATABASE = 'edxcurrent'
DB_MYSQL_HOST = '192.168.33.10'
DB_MYSQL_PORT = '3306'
DB_MYSQL_USER = 'root'
DB_MYSQL_PASSWORD = 'root'

LOGGING_DIR = '/var/log/current_rest'

PAY_WRAPPER_HOST = 'http://localhost:8080/current'

# reload setting for local
setting_local_file = '/workspace/deploy-config/ttsd-current/rest_settings.py'
if not os.path.isfile(setting_local_file):
    setting_local_file = os.path.join(BASE_DIR, "current_rest/console_settings_local.py")

if os.path.isfile(setting_local_file):
    exec (compile(open(setting_local_file).read(), setting_local_file, 'exec'))

# Application definition
INSTALLED_APPS = ['rest_framework', 'current_rest']

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

REST_FRAMEWORK = {
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
