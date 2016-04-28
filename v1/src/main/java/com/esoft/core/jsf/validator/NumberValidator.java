package com.esoft.core.jsf.validator;

import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

import org.omnifaces.util.Components;
import org.omnifaces.util.Messages;
import org.omnifaces.validator.ValueChangeValidator;
import org.springframework.stereotype.Component;

import com.sun.faces.util.MessageFactory;

//@Component
@FacesValidator(value = "com.esoft.core.validator.NumberValidator")
public class NumberValidator extends ValueChangeValidator implements
        PartialStateHolder {

    public static final String VALIDATOR_ID = "com.esoft.core.validator.NumberValidator";

    // 小数精度
    private Integer precision;

    // 必须为多少的倍数
    private Double cardinalNumber;

    @Override
    public void validateChangedObject(FacesContext context,
                                      UIComponent component, Object arg2) throws ValidatorException {
        Double number = null;
        if (arg2 instanceof Long) {
            number = ((Long) arg2).doubleValue();
        } else if (arg2 instanceof Double) {
            number = (Double) arg2;
        } else {
            throw new RuntimeException(arg2.toString()
                    + "is not a valid number");
        }
        if (number != null) {
            checkPrecision(context, component, number);
            checkCardinalNumber(context, component, number);
        }

    }

    private void checkCardinalNumber(FacesContext context,
                                     UIComponent component, double number) {
        if (cardinalNumber != null) {
            if (cardinalNumber == 0) {
                // 除数为0，抛异常
                // TODO:国际化
                throw new ValidatorException(Messages.createError(
                        "numberValidator cardinalNumber is zero!",
                        Components.getLabel(component)));
            }
            int decimalBitInput = getDecimalBit(number);
            int decimalBitCardinalNumber = getDecimalBit(cardinalNumber);
            int decimalBit = decimalBitInput > decimalBitCardinalNumber ? decimalBitInput : decimalBitCardinalNumber;
            int multiple = (int) Math.pow(10, decimalBit);
            number = (int) (number * multiple);
            double cardinalNumber_end = cardinalNumber * multiple;
            if (number % cardinalNumber_end != 0) {
                // 验证未通过
                // TODO:国际化
                throw new ValidatorException(Messages.createError("{0}必须为"
                                + cardinalNumber + "的倍数",
                        Components.getLabel(component)));
            }
        }
    }

    public int getDecimalBit(double target) {
        String targetStr = String.valueOf(target);
        int decimalPosition = targetStr.indexOf(".");
        if (decimalPosition > -1) {
            int decimalBit = targetStr.substring(decimalPosition + 1).length();
            return decimalBit;
        }
        return 0;
    }

    /**
     * 检查小数位数
     *
     * @param number
     */
    private void checkPrecision(FacesContext context, UIComponent component,
                                Double number) {
        if (precision != null) {
            String numberStr;
            if (number == number.longValue()) {
                numberStr = String.valueOf(number.longValue());
            } else {
                numberStr = number.toString();
            }
            String[] ns = numberStr.split("\\.");
            if (precision == 0) {
                if (ns.length != 1) {
                    throw new ValidatorException(
                            MessageFactory
                                    .getMessage(
                                            context,
                                            "com.esoft.core.validator.NumberValidator.NOT_A_INTEGER",
                                            MessageFactory.getLabel(context,
                                                    component), precision));
                }
            } else if (ns.length == 2 && ns[1].length() > precision) {
                throw new ValidatorException(
                        MessageFactory
                                .getMessage(
                                        context,
                                        "com.esoft.core.validator.NumberValidator.ERROR_PRECISION",
                                        MessageFactory.getLabel(context,
                                                component), precision));
            }
        }
    }

    @Override
    public boolean isTransient() {
        return false;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (state != null) {
            Object values[] = (Object[]) state;
            precision = (Integer) values[0];
            cardinalNumber = (Double) values[1];
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[2];
            values[0] = precision;
            values[1] = cardinalNumber;
            return (values);
        }
        return null;
    }

    @Override
    public void setTransient(boolean arg0) {

    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public Double getCardinalNumber() {
        return cardinalNumber;
    }

    public void setCardinalNumber(Double cardinalNumber) {
        this.cardinalNumber = cardinalNumber;
    }

    private boolean initialState;

    public void markInitialState() {
        initialState = true;
    }

    public boolean initialStateMarked() {
        return initialState;
    }

    public void clearInitialState() {
        initialState = false;
    }

}
