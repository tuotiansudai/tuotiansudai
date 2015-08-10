#coding=utf-8
from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.index_page import IndexPage
from test.functional.pages.login_page import LoginPage
from test.functional.pages.p2p_loan_list_page import LoanlistPage
from test.functional.pages.p2p_loan_20150427000062_page import InvestPTB

class TestLogin(BaseSeleniumTestCase):
    def test_should_navigate_to_index_page_after_login(self):
        LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("shenjiaojiao", username)

        #我要出借
        index_page = IndexPage(self.selenium).get_lend()
        self.assertIn("/p2p_loan", index_page.get_current_page_url())

        #选择标的
        p2ploanlist_page = LoanlistPage(self.selenium).get_one_PTB()
        self.assertIn("/p2p_loan/20150427000062", p2ploanlist_page.get_current_page_url())

        #标的投资
        ptb_page = InvestPTB(self.selenium).input_invest_money()
        ptb_page.click_invest_button().accept()
        self.assertIn("http://pay.soopay.net/spay/pay/payservice.do", self.selenium.current_url)