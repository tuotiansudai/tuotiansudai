#coding=utf-8
from test.functional.pages.login_page import LoginPage
from test.functional.pages.index_page import IndexPage
from test.functional.pages.center_page import CentPage
from test.functional.pages.recharge_page import RechargePage
from test.functional.base import BaseSeleniumTestCase

class Testrecharge(BaseSeleniumTestCase):
    def test_recharge_way1(self):
        #登录
        LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("shenjiaojiao", username)

        #我的账户
        myaccount_page = IndexPage(self.selenium).get_my_account()
        self.assertIn("/user/center", myaccount_page.get_current_page_url())

        #充值页面
        cent_recharge_page = CentPage(self.selenium).get_recharge_button()
        self.assertIn("/user/recharge", cent_recharge_page.get_current_page_url())

        #银行
        recharge_page = RechargePage(self.selenium).get_rechage_way()

        #金额->提交
        recharge_page.get_actualMoney(1).click_recharge_button()

        alert_title_message = recharge_page.get_alert_message()
        self.assertIn(u"登录到联动优势支付平台充值", alert_title_message)

    def test_recharge_way2(self):
        #登录
        LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("shenjiaojiao", username)

        #我的账户
        myaccount_page = IndexPage(self.selenium).get_my_account()
        self.assertIn("/user/center", myaccount_page.get_current_page_url())

        #充值页面
        cent_recharge_page = CentPage(self.selenium).get_recharge_through_manageof_money()
        self.assertIn("/user/recharge", cent_recharge_page.get_current_page_url())

        #银行
        recharge_page_way2 = RechargePage(self.selenium).get_rechage_way()

        #金额->提交
        recharge_page_way2.get_actualMoney(1).click_recharge_button()

        alert_title_message_way2 = recharge_page_way2.get_alert_message()
        self.assertIn(u"登录到联动优势支付平台充值", alert_title_message_way2)