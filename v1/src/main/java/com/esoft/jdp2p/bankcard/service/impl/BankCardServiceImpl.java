package com.esoft.jdp2p.bankcard.service.impl;

import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.bankcard.service.BankCardService;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

@Service("bankCardService")
public class BankCardServiceImpl implements BankCardService{

    @Resource
    HibernateTemplate ht;

    @Override
    @Transactional
    public List<BankCard> getBoundBankCardsByUserId(String userId) {
        String hqlTemplate = "select bankCard from BankCard bankCard where bankCard.user=''{0}'' and bankCard.status=''passed''";
        List<BankCard> bankCards = ht.find(MessageFormat.format(hqlTemplate, userId));
        return bankCards;
    }

    @Override
    public boolean isBoundBankCard(String userId) {
        String hqlTemplate = "select count(bankCard) from BankCard bankCard where bankCard.user=''{0}'' and bankCard.status=''passed''";
        int count = DataAccessUtils.intResult(ht.find(MessageFormat.format(hqlTemplate, userId)));
        return count > 0;
    }
}
