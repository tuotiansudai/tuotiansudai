#coding=utf-8
from test.functional.pages.index_page import IndexPage
from test.functional.pages.login_page import LoginPage
from test.functional.pages.p2p_loan_list_jxb_page import LoanJXB
from test.functional.pages.p2p_loan_jxb_20150422000054_page import InvestJXB
from test.functional.base import BaseSeleniumTestCase

class TestInvestJX(BaseSeleniumTestCase):
    def test_investJX(self):

        LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("shenjiaojiao", username)

        investJX_page = IndexPage(self.selenium).get_lend().get_investJX()
        self.assertIn("/p2p_loan-list_jxb", investJX_page.get_current_page_url())

        p2ploanlist_page = LoanJXB(self.selenium).get_one_JXB()
        self.assertIn("/p2p_loan_jxb/20150422000054", p2ploanlist_page.get_current_page_url())

        jxb_page = InvestJXB(self.selenium).input_invest_money(1)
        jxb_page.click_invest_button().accept()
        self.assertIn("http://pay.soopay.net/spay/pay/payservice.do", self.selenium.current_url)