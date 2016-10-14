package coupon.util;


import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotAccountNotInvestedUserCollector implements UserCollector {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<String> collect(long couponId) {
        List<String> investorLoginNames = investMapper.findInvestorLoginNames();
        List<String> userLoginNames = userMapper.findAllLoginNames();
        userLoginNames.removeAll(investorLoginNames);
        return userLoginNames;
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        return investMapper.sumSuccessInvestAmountByLoginName(null, loginName) == 0 && userMapper.findByLoginName(loginName) != null;
    }

}
