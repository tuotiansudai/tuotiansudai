from test.functional.pages.base_page import BasePage
import logging

logger = logging.getLogger(__name__)

class AccountPage(BasePage):
    url = "/account"

    def get_balance(self):
        # detail_list = self.find_element_by_class_name("detail-list")
        # logger.info(detail_list)

        balance = self.find_element_by_xpath(self, "/html/body/div[3]/div/div[2]/div[2]/ul/li[1]/span")
        logger.info(balance)

        logger.info("logger=" + balance)
        return self


