# -*- coding: utf-8 -*-
from redis import Redis

import settings

redis_client = Redis.from_url(settings.REDIS_URL)
