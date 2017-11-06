BEGIN;

update edxmessage.`message` set
  `web_url` = 'https://tuotiansudai.com/user-bill',
  `app_url` = 'FUND_DETAIL_LIST'
where `event_type` = 'PAYROLL_HAS_BEEN_TRANSFERRED';

COMMIT;