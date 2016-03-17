package com.tuotiansudai.point.dto;
public enum SignInPoint {
    FIRST_SIGN_IN(1,5),
    SECOND_SIGN_IN(2,10),
    THIRD_SIGN_IN(3,20),
    FOURTH_SIGN_IN(4,40),
    FIFTH_SIGN_IN(5,80);

    private int times;

    private int point;

    SignInPoint(int times,int point){
        this.times = times;
        this.point = point;
    }

    public static int getPointByTimes(int times){
        for(SignInPoint signInPoint:SignInPoint.values()){
            if(times == signInPoint.getTimes()){
                return signInPoint.getPoint();
            }
        }
        return FIFTH_SIGN_IN.getPoint();
    }

    public int getTimes() {
        return times;
    }

    public int getPoint() {
        return point;
    }
}
