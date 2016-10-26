package com.tuotiansudai.diagnosis.support;

import com.tuotiansudai.repository.model.UserBillModel;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class SingleObjectDiagnosis<T> {
    private final T t;
    private final String tracedObjectId;
    private final UserBillModel userBillModel;

    private boolean passed = true;
    private String problem = null;

    private SingleObjectDiagnosis(UserBillModel userBillModel, T t, Function<T, String> getObjectId) {
        this.userBillModel = userBillModel;
        passed = (t != null);
        if (!passed) {
            problem = "could not traced object for UserBill #" + userBillModel.getId();
            tracedObjectId = null;
        } else {
            tracedObjectId = getObjectId.apply(t);
        }
        this.t = t;
    }

    public static <T> SingleObjectDiagnosis<T> init(UserBillModel userBillModel, T t, Function<T, String> getObjectId) {
        return new SingleObjectDiagnosis<>(userBillModel, t, getObjectId);
    }

    public SingleObjectDiagnosis<T> check(Predicate<T> expect, String errorMessage) {
        return this.check(expect, t -> errorMessage);
    }

    public SingleObjectDiagnosis<T> check(Predicate<T> expect, Function<T, String> buildErrorMessageFunction) {
        if (passed) {
            passed = expect.test(t);
            if (!passed) {
                problem = String.format("trace UserBill #%d (%s) to %s, but %s",
                        userBillModel.getId(), userBillModel.getBusinessType(), tracedObjectId,
                        buildErrorMessageFunction.apply(t));
            }
        }
        return this;
    }

    public SingleObjectDiagnosis<T> success(Consumer<SingleObjectDiagnosisResult> consumer) {
        if (passed) {
            consumer.accept(SingleObjectDiagnosisResult.Passed);
        }
        return this;
    }

    public SingleObjectDiagnosis<T> fail(Consumer<SingleObjectDiagnosisResult> consumer) {
        if (!passed) {
            consumer.accept(SingleObjectDiagnosisResult.Abnormal(problem));
        }
        return this;
    }
}
