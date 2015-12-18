from test.functional.pages.base_page import BasePage


class WithdrawPage(BasePage):
    url = "/withdraw"

    def setMoney(self, money):
        self.find_element_by_class_name("amount-display").send_keys(money)
        return self

    def click_withdraw(self):
        self.find_element_by_class_name("withdraw-submit btn-normal").click()
        return self

    def withdraw(self, money):
        self.setMoney(money).click_withdraw()


