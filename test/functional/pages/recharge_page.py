#coding=utf-8
from test.functional.pages.base_page import BasePage

class RechargePage(BasePage):
    url = "/user/recharge"

    #选择银行
    def get_rechage_way(self):
        self.find_element_by_xpath("//img[@src='/site/themes/default/images/umpaybanklogo/BOC.png']").click()
        return self

    #输入充值金额
    def get_actualMoney(self, money):
        self.find_element_by_id("recharge:actualMoney").send_keys(money)
        return self

    #提现按键
    def click_recharge_button(self):
        self.find_element_by_xpath("//a[@class='txczje02_btn']").click()
        return self

    #获取alert信息
    def get_alert_message(self):
        return self.find_element_by_xpath("//div[@class='mask_main']/h3[1]").text