# -*- coding:utf-8 -*-
import time
from test.functional.pages.base_page import BasePage


class LoanPage(BasePage):
    url = ""

    def __init__(self, selenium, currUrl, url_params=None):
        if not url_params:
            url_params = []
        self.url = currUrl
        self.selenium = selenium
        self.base_url = ""
        self.url_params = url_params
        BasePage.goto(self)

    def click_fake_invest(self):
        invest_amount = self.get_invest_amount()
        self.find_element_by_css(".btn-pay").click()
        return invest_amount

    def click_invest(self):
        self.find_element_by_css(".btn-pay").click()
        return self

    def click_loan_invests(self):
        self.find_element_by_id("loan-invests").click()
        return self

    def get_invest_amount(self):
        return self.find_element_by_css(".text-input-amount").get_attribute("value")

    def can_invest(self):
        invest_amount = float(self.get_invest_amount())
        if invest_amount > 0:
            return True
        else:
            return False


    def check_invest_is_success(self,login_name,invest_amount,invest_time):

        real_login_name = self.find_element_by_css(".loan-invest-loginName").text
        real_invest_amount = self.find_element_by_css(".loan-invest-amount").text
        real_invest_time = self.find_element_by_css(".loan-invest-time").text

        if real_login_name != login_name:
            return False
        if real_invest_amount != invest_amount:
            return False
        if real_invest_time < invest_time:
            return False

        return True


