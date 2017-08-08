# -*- coding: utf-8 -*-
from django.conf import settings
from redis import Redis


redis_client = Redis.from_url(settings.REDIS_URL)
