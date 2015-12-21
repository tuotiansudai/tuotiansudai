from test.functional.pages.base_page import BasePage


class WithdrawPage(BasePage):
    url = "/withdraw"

    def set_money(self, money):
        self.find_element_by_class_name("amount-display").send_keys(money)
        return self

    def click_withdraw(self):
        self.find_element_by_class_name("withdraw-submit").click()
        return self

    def withdraw(self, money):
        self.set_money(money).click_withdraw()
        return self

    def get_money(self):
        return self.find_element_by_xpath("/html/body/div[0]/div[1]/div[1]/div[0]/ur/li[5]/div[1]").text

