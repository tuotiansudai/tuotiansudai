from test.functional.pages.base_page import BasePage

class AccountPage(BasePage):
    url = "/account"

    def get_balance(self):
        balance = self.find_element_by_id("balance").text
        return float(balance)


