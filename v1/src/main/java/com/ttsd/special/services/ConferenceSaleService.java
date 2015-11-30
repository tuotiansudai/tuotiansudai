package com.ttsd.special.services;

import com.esoft.archer.user.model.User;

public interface ConferenceSaleService {

    void processIfInActivityForBindCard(String orderId, User user);

    void processIfInActivityForInvest(String orderId, User user);
}
