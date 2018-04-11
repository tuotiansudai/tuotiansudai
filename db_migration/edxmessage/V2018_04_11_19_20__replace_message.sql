update message set
title = replace(title, '预期年化收益', '约定年化收益'),
app_title = replace(app_title, '预期年化收益', '约定年化收益'),
template = replace(template, '预期年化收益', '约定年化收益'),
template_txt = replace(template_txt, '预期年化收益', '约定年化收益');

update user_message set
title = replace(title, '预期年化收益', '约定年化收益'),
content = replace(content, '预期年化收益', '约定年化收益');
