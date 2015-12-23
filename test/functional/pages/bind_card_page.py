from test.functional.pages.base_page import BasePage

class BindCardPage(BasePage):
    url = "/bind-card"

    def input_card_no(self, card_number):
        self.find_element_by_name("cardNumber").send_keys(card_number)
        return self

    def click_submit(self):
        self.find_element_by_css(".bind-card-submit").click()
        return self

    def has_bind_card_form(self):
        try:
            self.find_element_by_css("ol.select-bank", 1)
            return True
        except:
            return False


    def get_bind_card_no(self):
        return self.find_element_by_css(".card-num").text


