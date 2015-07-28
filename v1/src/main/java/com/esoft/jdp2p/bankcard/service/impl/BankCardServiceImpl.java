package com.esoft.jdp2p.bankcard.service.impl;

import com.esoft.archer.theme.controller.TplVars;
import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.bankcard.service.BankCardService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.text.MessageFormat;
import java.util.List;

@Service("bankCardService")
public class BankCardServiceImpl implements BankCardService{

    @Resource
    HibernateTemplate ht;

    @Resource
    private TplVars tplVars;

    @Override
    @Transactional
    public List<BankCard> getBoundBankCardsByUserId(String userId) {
        String hqlTemplate = "select bankCard from BankCard bankCard where bankCard.user=''{0}'' and bankCard.status=''passed'' order by bankCard.time desc";
        List<BankCard> bankCards = ht.find(MessageFormat.format(hqlTemplate, userId));
        List<BankCard> returnBankCards = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(bankCards)) {
            returnBankCards.add(bankCards.get(0));
        }
        return returnBankCards;
    }

    @Override
    public boolean isBoundBankCard(String userId) {
        String hqlTemplate = "select count(bankCard) from BankCard bankCard where bankCard.user=''{0}'' and bankCard.status=''passed''";
        int count = DataAccessUtils.intResult(ht.find(MessageFormat.format(hqlTemplate, userId)));
        return count > 0;
    }

    public boolean isOpenFastPayment(String userId) {
        String hqlTemplate = "select count(bankCard) from BankCard bankCard where bankCard.user=''{0}'' and bankCard.status=''passed'' and bankCard.isOpenFastPayment=true";
        int count = DataAccessUtils.intResult(ht.find(MessageFormat.format(hqlTemplate, userId)));
        return count > 0;
    }

    public boolean isCardNoBinding(String cardNo) {
        String hqlTemplate = "select count(bankCard) from BankCard bankCard where bankCard.cardNo=''{0}'' and bankCard.status=''passed''";
        int count = DataAccessUtils.intResult(ht.find(MessageFormat.format(hqlTemplate, cardNo)));
        return count > 0;
    }

    @Override
    public boolean isExistsBankPhoto(String bankNo){
        File classPath = new File(this.getClass().getResource("/").getPath());
        StringBuffer stringBufferPath = new StringBuffer(classPath.getParentFile().getParentFile().toString()+this.tplVars.getThemeImagePath());
        stringBufferPath.append("/umpaybanklogo/");
        stringBufferPath.append(bankNo);
        stringBufferPath.append(".png");
        File file = new File(stringBufferPath.toString());
        return file.exists();
    }

}
