<#import "macro/global_m.ftl" as global>

<@global.main pageCss="${m_css.sign_enter_point}" pageJavascript="${m_js.sign_login}" title="登录/注册">
    <div id="page_variables" redirect="${redirect!('/m/')}"></div>
    <div class="entry_container"><#include 'login_m_entry.ftl'></div>
    <div class="login_container"><#include 'login_m_login_form.ftl'></div>
    <div class="register_container"><#include 'login_m_register_form.ftl'></div>
</@global.main>
