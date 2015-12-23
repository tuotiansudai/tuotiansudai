from test.functional.pages.base_page import BasePage


class WithdrawPage(BasePage):
    url = "/withdraw"

    def set_money(self, money):
        self.find_element_by_css(".amount-display").send_keys(money)
        return self

    def click_withdraw(self):
        self.find_element_by_css(".withdraw-submit").click()
        return self

    def withdraw(self, money):
        self.set_money(money).click_withdraw()
        return self
