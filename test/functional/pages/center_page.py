#coding=utf-8
from test.functional.pages.base_page import BasePage


class CentPage(BasePage):
    url = "/user/center"

    #充值的两种实现方式
    def get_recharge_button(self):
        self.find_element_by_link_text("充值").click()
        return self
    def get_recharge_through_manageof_money(self):
        self.find_element_by_id("CC1_2").click()
        self.find_element_by_xpath("//ul[@id='C1_2']/li[2]/a[1]").click()
        return self

    #提现的两种实现方式
    def get_withdrawal_button(self):
        self.find_element_by_link_text("提现").click()
        return self
    def get_withdrawal_through_manageof_money(self):
        self.find_element_by_id("CC1_2").click()
        self.find_element_by_xpath("//ul[@id='C1_2']/li[3]/a[1]").click()
        return self

    #实名认证(个人信息)
    def get_the_realname(self):
        self.find_element_by_id("CC1_5").click()
        self.find_element_by_xpath("//ul[@id='C1_5']/li[1]/a[1]").click()
        return self
    #安全信息
    def get_the_private_message(self):
        self.find_element_by_id("CC1_5").click()
        self.find_element_by_xpath("//ul[@id='C1_5']/li[2]/a[1]").click()
        return self

    #绑卡
    def bind_card(self):
        self.find_element_by_id("CC1_5").click()
        self.find_element_by_xpath("//ul[@id='C1_5']/li[3]/a[1]").click()
        return self