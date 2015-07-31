#coding=utf-8
from test.functional.pages.base_page import BasePage

class changePassword(BasePage):
    url="/user/changePassword"

    #旧密码
    def get_the_old_passwd(self, oldPass):
        self.find_element_by_id("j_idt43:oldPass").send_keys(oldPass)
        return self

    #新密码
    def get_the_new_passwd(self, newPassed):
        self.find_element_by_id("j_idt43:pass").send_keys(newPassed)
        return self

    #重复新密码
    def get_the_repasswd(self, rePasswd):
        self.find_element_by_id("j_idt43:repass").send_keys(rePasswd)
        return self

    #提交
    def changePasswordButton(self):
        self.find_element_by_link_text("提交").click()

    #alert修改成功
    def get_alert_message(self):
        return self.find_element_by_xpath("//div[@class='aui_content']/div[1]").text