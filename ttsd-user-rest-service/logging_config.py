import logging
from logging.handlers import RotatingFileHandler
import os


BASE_DIR = '/var/log/signin/'
if not os.path.exists(BASE_DIR):
    os.mkdir(BASE_DIR, 0755)
logger = logging.getLogger(__name__)
hdlr = RotatingFileHandler(os.path.join(BASE_DIR, "ttsd-signin.log"), maxBytes=1024 * 1024 * 100, backupCount=20)
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
hdlr.setFormatter(formatter)
logger.addHandler(hdlr)
logger.setLevel(logging.DEBUG)
