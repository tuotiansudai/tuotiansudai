package coupon.aspect;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.SignInResult;
import coupon.repository.model.UserGroup;
import coupon.service.CouponAssignmentService;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
public class CouponAspect {
    static Logger logger = Logger.getLogger(CouponAspect.class);

    private final static List<UserGroup> userGroups = Lists.newArrayList(UserGroup.ALL_USER,
            UserGroup.INVESTED_USER,
            UserGroup.REGISTERED_NOT_INVESTED_USER,
            UserGroup.AGENT,
            UserGroup.CHANNEL,
            UserGroup.STAFF,
            UserGroup.STAFF_RECOMMEND_LEVEL_ONE,
            UserGroup.IMPORT_USER,
            UserGroup.NOT_ACCOUNT_NOT_INVESTED_USER,
            UserGroup.MEMBERSHIP_V0,
            UserGroup.MEMBERSHIP_V1,
            UserGroup.MEMBERSHIP_V2,
            UserGroup.MEMBERSHIP_V3,
            UserGroup.MEMBERSHIP_V4,
            UserGroup.MEMBERSHIP_V5);

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Pointcut("execution(* com.tuotiansudai.service.UserService.registerUser(..))")
    public void registerUserPointcut() {
    }

    @Pointcut("execution(* *..SignInClient.login(..))")
    public void loginSuccessPointcut() {
    }

    @Pointcut("execution(* *..SignInClient.refresh(..))")
    public void refreshTokenPointcut() {
    }

    @AfterReturning(value = "registerUserPointcut()", returning = "returnValue")
    public void afterReturningUserRegister(JoinPoint joinPoint, boolean returnValue) {
        if (!returnValue) {
            return;
        }
        logger.info("after user register pointcut");
        Object dto = joinPoint.getArgs()[0];
        String mobile;
        try {
            Method getMobile = dto.getClass().getMethod("getMobile");
            mobile = (String) getMobile.invoke(dto);
            couponAssignmentService.assignUserCoupon(mobile,
                    Lists.newArrayList(UserGroup.ALL_USER, UserGroup.NEW_REGISTERED_USER, UserGroup.NOT_ACCOUNT_NOT_INVESTED_USER));
        } catch (Exception e) {
            logger.error("after user register aspect fail ", e);
        }
    }

    @AfterReturning(value = "loginSuccessPointcut() || refreshTokenPointcut()", returning = "signInResult")
    public void afterReturningUserLogin(JoinPoint joinPoint, SignInResult signInResult) {
        try {
            if (signInResult != null && signInResult.isResult()) {
                logger.info("assign coupon after user login");
                couponAssignmentService.assignUserCoupon(signInResult.getUserInfo().getLoginName(), userGroups);
            }
        } catch (Exception e) {
            logger.error("assign coupon after user login is failed ", e);
        }
    }
}
