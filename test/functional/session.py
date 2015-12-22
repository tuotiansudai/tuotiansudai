import redis
import test.functional.settings as settings

redis_client = redis.StrictRedis(host=settings.SESSION_REDIS_HOST, port=settings.SESSION_REDIS_PORT, db=settings.SESSION_REDIS_DB)

def get_session_value(session_id, session_key):
    redis_key = "spring:session:sessions:{0}".format(session_id)
    redis_sub_key = "sessionAttr:{0}".format(session_key)
    redis_value = redis_client.hget(redis_key, redis_sub_key)
    return redis_value

def get_login_captcha(session_id):
    session_value = get_session_value(session_id, 'LOGIN_CAPTCHA')
    return session_value[-5:]
