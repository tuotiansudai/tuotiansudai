# -*- coding: utf-8 -*-
from redis import Redis

from current_rest import settings

redis_client = Redis.from_url(settings.REDIS_URL)
