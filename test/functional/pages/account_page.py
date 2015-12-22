from test.functional.pages.base_page import BasePage
import logging

logger = logging.getLogger(__name__)

class AccountPage(BasePage):
    url = "/account"

    def get_balance(self):
        balance = self.find_element_by_id("balance").text
        return float(balance)


