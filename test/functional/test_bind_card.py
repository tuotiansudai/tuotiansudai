import time
import test.functional.settings as settings
from test.functional.base import BaseSeleniumTestCase
from test.functional.pages.login_page import LoginPage
from test.functional.pages.bind_card_page import BindCardPage

class TestBindCard(BaseSeleniumTestCase):

    def assert_open_umpay_page(self):
        current_window_handle = self.selenium.current_window_handle
        new_window_handle = self.selenium.window_handles[1]
        self.selenium.switch_to_window(new_window_handle)
        new_window_url = self.selenium.current_url
        self.assertIn(settings.UMPAY_BASE_URL, new_window_url)
        self.selenium.switch_to_window(current_window_handle)


    def test_should_bind_card(self):
        LoginPage(self.selenium).login('shenjiaojiao','123abc')

        bind_card_page = BindCardPage(self.selenium)

        self.assertEqual(True, bind_card_page.has_bind_card_form())

        fake_card = int(time.time()*1000000)
        bind_card_page.input_card_no(fake_card).click_submit()

        self.assert_open_umpay_page()


    def test_should_bind_card_fake_ump(self):
        LoginPage(self.selenium).login('shenjiaojiao','123abc')

        bind_card_page = BindCardPage(self.selenium)

        self.assertEqual(True, bind_card_page.has_bind_card_form())

        fake_card = int(time.time()*1000000)
        bind_card_page.input_card_no(fake_card).click_submit()

        time.sleep(2)
        bind_card_no = BindCardPage(self.selenium).get_bind_card_no()

        self.assertEqual(False, bind_card_page.has_bind_card_form())

        self.assertEqual(fake_card, bind_card_no)


