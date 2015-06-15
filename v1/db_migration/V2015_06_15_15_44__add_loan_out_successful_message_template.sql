START TRANSACTION;
INSERT INTO `user_message_template` (`id`, `name`, `status`, `template`, `message_node`, `message_way`)
VALUES ('loan_out_successful_sms', '标的放款成功短信提醒', '必须', '尊敬的拓天速贷客户，您在平台的项目借款 “{loanName}” 已成功放款，您的投资金额为{money}元。【拓天速贷】', 'loan_out_successful_by_sms', 'sms');

INSERT INTO `user_message_template` (`id`, `name`, `status`, `template`, `message_node`, `message_way`)
VALUES ('loan_out_successful_email', '标的放款成功短信提醒', '必须', '尊敬的拓天速贷客户，您在平台的项目借款 “{loanName}” 已成功放款，您的投资金额为{money}元。【拓天速贷】', 'loan_out_successful_by_email', 'email');
COMMIT;