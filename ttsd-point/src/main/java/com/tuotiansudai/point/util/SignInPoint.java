package com.tuotiansudai.point.util;

import com.google.common.collect.ImmutableMap;
import com.tuotiansudai.point.dto.SignInPointDto;
import org.joda.time.DateTime;

import java.util.Date;

public class SignInPoint {
    public static final Integer firstSignIn = 1;
    public static final Integer secondSignIn = 2;
    public static final Integer thirdSignIn = 3;
    public static final Integer fourthSignIn = 4;
    public static final Integer fifthSignIn = 5;

    public static ImmutableMap<Integer, Integer> signInPointMap = ImmutableMap.<Integer, Integer>builder()
            .put(firstSignIn, 5)
            .put(secondSignIn, 10)
            .put(thirdSignIn, 20)
            .put(fourthSignIn, 40)
            .put(fifthSignIn, 80).build();

    public static SignInPointDto calculateSignInPoint(SignInPointDto lastSignInPointDto) {
        int lastSignCount = lastSignInPointDto.getSignInCount();
        Date lastSignInDate = lastSignInPointDto.getSignInDate();
        int point = signInPointMap.get(firstSignIn);
        Date yesterday = new DateTime().plusDays(-1).withTimeAtStartOfDay().toDate();
        Date today = new DateTime().withTimeAtStartOfDay().toDate();
        if (yesterday.compareTo(lastSignInDate) == 0) {
            lastSignCount++;
            if (lastSignCount > fifthSignIn) {
                point = signInPointMap.get(fifthSignIn);
            } else {
                point = signInPointMap.get(lastSignCount);
            }
        } else {
            lastSignCount = firstSignIn;
        }
        return new SignInPointDto(lastSignCount, today, point);
    }
}
