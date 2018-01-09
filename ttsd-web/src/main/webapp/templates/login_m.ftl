<#import "macro/global_m.ftl" as global>

<@global.main pageCss="" pageJavascript="" title="登录/注册">
    <div id="page_variables" redirect="${redirect!('/m/')}"></div>
    <#include 'login_m_entry.ftl'>
    <#include 'login_m_login_form.ftl'>
    <#include 'login_m_register_form.ftl'>
</@global.main>
