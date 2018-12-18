BEGIN;
UPDATE help_center SET content='按月还息，到期还本；到期还本付息。' WHERE id='100033';
UPDATE help_center SET content='拓天速贷主要产品为房屋抵押借款、车辆抵押借款、车辆消费借款，50元起投；还款方式主要为：先付收益后还出借本金，按天计息，放款后生息；到期还本付息，按天计息，放款后生息。' WHERE id='100003';
UPDATE help_center SET content='平台所有标的募集期限均为7天，如到期未筹满，该项目视为流标，出借资金将返回到第三方支付账户联动优势账户中，届时您可以在账户余额中查看该笔资金。' WHERE id='100072';
COMMIT;