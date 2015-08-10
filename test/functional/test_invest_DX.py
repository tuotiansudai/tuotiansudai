#coding=utf-8
from test.functional.pages.index_page import IndexPage
from test.functional.pages.login_page import LoginPage
from test.functional.pages.p2p_loan_list_dxb_page import LoanDXB
from test.functional.pages.p2p_loan_dxb_20150729000151_page import InvestDXB
from test.functional.base import BaseSeleniumTestCase

class TestInvestDX(BaseSeleniumTestCase):
    def test_investDX(self):

        LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        username = IndexPage(self.selenium).get_username()
        self.assertIn("shenjiaojiao", username)

        investDX_page = IndexPage(self.selenium).get_lend()
        investDX_page.get_investDX()
        self.assertIn("/p2p_loan-list_dxb", investDX_page.get_current_page_url())

        p2ploanlist_page = LoanDXB(self.selenium).get_one_DXB()
        self.assertIn("/p2p_loan_dxb/20150729000151", p2ploanlist_page.get_current_page_url())

        dxb_page = InvestDXB(self.selenium).input_invest_money(1)
        dxb_page.placeholder(1).click_invest_button().accept()
        self.assertIn("http://pay.soopay.net/spay/pay/payservice.do", self.selenium.current_url)