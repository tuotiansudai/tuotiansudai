#coding=utf-8
from test.functional.pages.base_page import BasePage

class InvestDXB(BasePage):
    url = "/p2p_loan_dxb/20150729000151"

    #定向标金额
    def input_invest_money(self, money):
        self.find_element_by_id("j_idt45:j_idt55").send_keys(money)
        return self

    #定向标密码
    def placeholder(self, password):
        self.find_element_by_id("j_idt45:ut").send_keys(password)
        return self

    #投资按键
    def click_invest_button(self):
        self.find_element_by_xpath("//a[@class='sure-btn']").click()
        return self

    #alert 确认投资
    def accept(self):
        self.selenium.switch_to_alert().accept()
        return self