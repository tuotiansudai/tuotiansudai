from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.withdraw_page import WithdrawPage
from test.functional.pages.login_page import LoginPage
from test.functional.pages.account_page import AccountPage
from time import sleep

class TestWithdraw(BaseSeleniumTestCase):
    def test_should_reduce_balance_after_withdraw(self):

        LoginPage(self.selenium).login('shenjiaojiao', '123abc')

        withdraw_money = 3

        old_balance = AccountPage(self.selenium).get_balance()

        WithdrawPage(self.selenium).withdraw(withdraw_money)

        sleep(4)

        new_balance = AccountPage(self.selenium).get_balance()

        self.assertEqual(old_balance, new_balance + withdraw_money)
