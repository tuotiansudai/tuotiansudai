from celery.utils.log import get_task_logger

from jobs.base import BaseTask
from jobs import current_app

logger = get_task_logger(__name__)


class InvestCallback(BaseTask):
    name = "currentInvestCallback"
    queue = "celery.invest.callback"

    def do(self, message):
        logger.info("this is the message:{}".format(message))
        return True

# register task and initialize it
current_app.tasks.register(InvestCallback())
ret = InvestCallback().delay()
