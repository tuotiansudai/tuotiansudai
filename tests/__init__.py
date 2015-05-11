import logging
import logging.config
import os

current_dir_path = os.path.dirname(os.path.realpath(__file__))
logging_file_path = os.path.join(current_dir_path, 'logging.conf')
logging.config.fileConfig(logging_file_path)