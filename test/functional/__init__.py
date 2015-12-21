import logging

logger = logging.getLogger(__name__)
hdlr = logging.FileHandler('function_test.log')
formatter = logging.Formatter('%(asctime)s %(levelname)s %(message)s')
hdlr.setFormatter(formatter)
logger.addHandler(hdlr)
logger.setLevel(logging.DEBUG)
