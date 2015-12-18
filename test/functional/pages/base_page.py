import logging
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as expected
from test.functional import settings
from test.functional.settings import WAIT_TIME

logger = logging.getLogger(__name__)


class BasePage(object):
    url = ""

    def __init__(self, selenium, url_params=None):
        if not url_params:
            url_params = []
        self.selenium = selenium
        self.base_url = settings.TEST_BASE_URL
        self.url_params = url_params
        self.goto()

    def goto(self):
        logger.debug("Goto page: [{}]".format(self.get_page_url()))
        return self._selenium_get_url(self.get_page_url())

    def refresh(self):
        self.selenium.refresh()

    def navigate_back(self):
        self.selenium.back()

    def _selenium_get_url(self, url):
        try:
            self.selenium.get('about:blank')
            self.selenium.get(str(url))
        except Exception as ex:
            logger.error("Can not open the url:[{}]".format(url))
            raise ex
        return self

    def get_page_url(self):
        if not self.url:
            raise RuntimeError("no url been set")
        return self._get_url(self.url)

    def _get_url(self, url):
        format_url = url.format(*self.url_params)
        return "{0}{1}".format(self.base_url, format_url)

    def get_current_page_url(self):
        return self.selenium.current_url

    def find_element_by_css(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.visibility_of_element_located((By.CSS_SELECTOR, selector)))

    def find_elements_by_css(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.presence_of_all_elements_located((By.CSS_SELECTOR, selector)))

    def find_element_by_class_name(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.visibility_of_element_located((By.CLASS_NAME, selector)))

    def find_elements_by_class_name(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.presence_of_all_elements_located((By.CLASS_NAME, selector)))

    def find_elements_by_link_text(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.presence_of_all_elements_located((By.LINK_TEXT, selector)))

    def find_element_by_link_text(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.visibility_of_element_located((By.LINK_TEXT, selector)))

    def find_element_by_id(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.visibility_of_element_located((By.ID, selector)))

    def find_element_by_xpath(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.visibility_of_element_located((By.XPATH, selector)))

    def find_element_by_name(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.visibility_of_element_located((By.NAME, selector)))

    def find_elements_by_xpath(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.presence_of_all_elements_located((By.XPATH, selector)))

    def invisible_element_by_id(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.invisibility_of_element_located((By.ID, selector)))

    def invisible_element_by_xpath(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.invisibility_of_element_located((By.XPATH, selector)))

    def invisible_element_by_css(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.invisibility_of_element_located((By.CSS_SELECTOR, selector)))

    def invisible_element_by_link_text(self, selector, wait_time=WAIT_TIME):
        return WebDriverWait(self.selenium, wait_time).until(
            expected.invisibility_of_element_located((By.LINK_TEXT, selector)))

    def find_sub_element_by_css(self, selector, elem):
        return elem.find_element(By.CSS_SELECTOR, selector)

    def find_sub_elements_by_css(self, selector, elem):
        return elem.find_elements(By.CSS_SELECTOR, selector)

    def find_sub_elements_by_xpath(self, selector, elem):
        return elem.find_elements(By.XPATH, selector)

    def find_sub_element_by_xpath(self, selector, elem):
        return elem.find_element(By.XPATH, selector)

    def find_sub_element_by_tag(self, selector, elem):
        return elem.find_element(By.TAG_NAME, selector)

    def get_cookie_by_name(self, name):
        cookie = self.selenium.get_cookie(name)
        return cookie['value']

    def get_session_id(self):
        return self.get_cookie_by_name("SESSION")
