#coding=utf-8
from test.functional.pages.base_page import BasePage

class InvestPTB(BasePage):
    url = "/p2p_loan/20150427000062"

    #普通标金额
    def input_invest_money(self):
        self.find_element_by_id("j_idt45:j_idt55").send_keys(11)
        return self

    #投资按键
    def click_invest_button(self):
        self.find_element_by_xpath("//a[@class='sure-btn']").click()
        return self

    #alert确认投资
    def accept(self):
         self.selenium.switch_to_alert().accept()
         return self