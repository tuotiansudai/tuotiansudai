#coding=utf-8
from test.functional.pages.base_page import BasePage

class WithdrawPage(BasePage):
    url = "/user/withdraw"

    #提现金额
    def input_money(self):
        self.find_element_by_id("form:money").send_keys(1)
        return self

    #提现按键
    def click_button(self):
        self.find_element_by_xpath("//span[@id='form:withdrawBtn']/a[1]").click()
        return self