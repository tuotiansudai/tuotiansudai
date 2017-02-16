begin;
update message set title='恭喜您已成功购买{0}天增值特权',template='尊敬的用户，恭喜您已成功购买增值特权，有效期至{0}日，【马上投资】享受增值特权吧！',
  template_txt = '尊敬的用户，恭喜您已成功购买增值特权，有效期至{0}日，【马上投资】享受增值特权吧！',event_type = 'MEMBERSHIP_PRIVILEGE_BUY_SUCCESS' where id = 100031;

  update message set title='您购买的增值特权已过期',template='尊敬的用户，您购买的增值特权已过期，增值特权可享受服务费7折优惠，请及时续费。',
  template_txt = '尊敬的用户，您购买的增值特权已过期，增值特权可享受服务费7折优惠，请及时续费。',event_type = 'MEMBERSHIP_PRIVILEGE_EXPIRED' where id = 100026;
COMMIT ;