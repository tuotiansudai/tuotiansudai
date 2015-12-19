import time
from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.index_page import IndexPage
from test.functional.pages.login_page import LoginPage
from test.functional.pages.loan_page import LoanPage


class TestInvest(BaseSeleniumTestCase):
    def test_should_navigate_to_index_page_after_login(self):
        LoginPage(self.selenium).login('shenjiaojiao', '123abc')
        username = IndexPage(self.selenium).get_username()

        self.assertIn("shenjiaojiao", username)

        IndexPage(self.selenium).click_prepare_invest()

        ump_url = LoanPage(self.selenium).click_invest().get_current_page_url()

        self.assertEqual("http://pay.soopay.net/spay/pay/payservice.do",ump_url)











