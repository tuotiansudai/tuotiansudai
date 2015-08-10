#coding=utf-8
from test.functional.pages.base_page import BasePage

class InvestXSB(BasePage):
    url = "/p2p_loan_xsb/20150722000142"

    #新手标金额
    def input_invest_money(self, money):
        self.find_element_by_id("j_idt45:j_idt55").send_keys(money)
        return self

    #投资按键
    def click_invest_button(self):
        self.find_element_by_xpath("//a[@class='sure-btn']").click()
        return self

    #alert确认投资
    def accept(self):
        self.selenium.switch_to_alert().accept()
        return self