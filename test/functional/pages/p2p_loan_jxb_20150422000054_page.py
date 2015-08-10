#coding=utf-8
from test.functional.pages.base_page import BasePage

class InvestJXB(BasePage):
    url = "/p2p_loan_jxb/20150422000054"

    #加息标金额
    def input_invest_money(self, money):
        self.find_element_by_id("j_idt49:j_idt59").send_keys(money)
        return self

    #投资按键
    def click_invest_button(self):
        self.find_element_by_xpath("//a[@class='sure-btn']").click()
        return self

    #alert确认投资
    def accept(self):
        self.selenium.switch_to_alert().accept()
        return self