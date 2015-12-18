# -*- coding: utf-8 -*-
from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.withdraw_page import WithdrawPage
from test.functional.pages.login_page import LoginPage


class TestWithdraw(BaseSeleniumTestCase):
    def test_should_navigate_to_umpay_page_after_submit(self):
        LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        WithdrawPage(self.selenium).withdraw(3)
        moneyEle=self.find_element_by_xpath("/html/body/div[0]/div[1]/div[1]/div[0]/ur/li[5]/div[1]")
        self.assertIn("3.00元", moneyEle)

