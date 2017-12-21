BEGIN;
update message set template = '哇，您终于来啦！初次见面，岂能无礼？6888元体验金及668元投资红包双手奉上，快去投资吧！',
template_txt = '哇，您终于来啦！初次见面，岂能无礼？6888元体验金及668元投资红包双手奉上，快去投资吧！' where id = 100018

update user_message set title = replace(title,'现金红包','投资红包'),content = replace(content,'现金红包','投资红包');

COMMIT;