import test.functional.settings as settings
from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.login_page import LoginPage
from test.functional.pages.index_page import IndexPage

class TestLogin(BaseSeleniumTestCase):
    def test_should_navigate_to_index_page_after_login(self):

        url_after_login = LoginPage(self.selenium).login('shenjiaojiao', '123abc').get_current_page_url()
        self.assertEqual(settings.TEST_BASE_URL+'/', url_after_login)

        login_username = IndexPage(self.selenium).get_username()
        self.assertEqual("shenjiaojiao", login_username)


