from unittest import TestCase
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from test.functional import settings

# driver = webdriver.PhantomJS()
# driver.get("http://www.python.org")
# assert "Python" in driver.title
# elem = driver.find_element_by_name("q")
# elem.send_keys("pycon")
# elem.send_keys(Keys.RETURN)
# assert "No results found." not in driver.page_source
# driver.close()

class BaseSeleniumTestCase(TestCase):
    def get_web_driver(self):
        return webdriver.Firefox() if settings.ENV == "dev" else webdriver.PhantomJS()

    def setUp(self):
        self.selenium = self.get_web_driver()

    def tearDown(self):
        self.selenium.quit()

