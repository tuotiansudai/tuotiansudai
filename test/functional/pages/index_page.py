from test.functional.pages.base_page import BasePage


class IndexPage(BasePage):
    url = "/"

    def get_username(self):
        return self.find_element_by_css(".personal-info-link").text

