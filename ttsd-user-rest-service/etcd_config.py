import etcd


class EtcdConfig(object):
    def __init__(self, endpoint=None, host=None, port=2379, env=None):
        if endpoint:
            from urlparse import urlparse
            url = urlparse(endpoint)
            self.host = url.hostname
            self.port = url.port
        else:
            self.host = host
            self.port = port
        self.env = env
        self.client = etcd.Client(host=self.host, port=self.port)

    def _key(self, key):
        return '/{}/{}'.format(self.env, key) if key else key

    def get(self, key, default_value=None):
        try:
            return self.client.get(self._key(key))
        except etcd.EtcdKeyNotFound:
            return default_value

    def get_int(self, key, default_value=None):
        str_value = self.get(key)
        if str_value is None:
            return default_value
        else:
            return int(str_value)

    def get_bool(self, key):
        str_value = self.get(key)
        return str_value and 'true' == str_value.lower()
