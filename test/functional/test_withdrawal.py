#coding=utf-8
from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.index_page import IndexPage
from test.functional.pages.login_page import LoginPage
from test.functional.pages.center_page import CentPage
from test.functional.pages.withdraw_page import WithdrawPage

class TestWithdrawal(BaseSeleniumTestCase):

    def test_withdrawal_way1(self):
        #登录
        LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("shenjiaojiao", username)

        #我的账户
        myaccount_page = IndexPage(self.selenium).get_my_account()
        self.assertIn("/user/center", myaccount_page.get_current_page_url())

        #点击提现
        withdrawal_page = CentPage(self.selenium).get_withdrawal_button()
        self.assertIn("/user/withdraw", withdrawal_page.get_current_page_url())

        #金额
        withdraw_page = WithdrawPage(self.selenium).input_money()
        #提交
        withdraw_page.click_button()
        self.assertIn("http://pay.soopay.net/spay/pay/payservice.do", self.selenium.current_url)

    def test_withdrawal_way2(self):
        #登录
        LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("shenjiaojiao", username)
        #我的账户
        myaccount_page_way2 = IndexPage(self.selenium).get_my_account()
        self.assertIn("/user/center", myaccount_page_way2.get_current_page_url())
        #资金管理->账户提现
        withdrawal_page_way2 = CentPage(self.selenium).get_withdrawal_through_manageof_money()
        self.assertIn("/user/withdraw", withdrawal_page_way2.get_current_page_url())

        #金额
        withdraw_page2 = WithdrawPage(self.selenium).input_money()
        #提交
        withdraw_page2.click_button()
        self.assertIn("http://pay.soopay.net/spay/pay/payservice.do", self.selenium.current_url)
