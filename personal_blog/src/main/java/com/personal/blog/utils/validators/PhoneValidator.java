package com.personal.blog.utils.validators;


import com.personal.blog.utils.validators.annotation.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone,String> {


    private final  Pattern phone = Pattern.compile("^1((3[\\d])|(4[5,6,7,9])|(5[0-3,5-9])|(6[5-7])|(7[0-8])|(8[\\d])|(9[1,8,9]))\\d{8}$");

    /**
     * 获取注解内的属性值
     * @param constraintAnnotation
     */
    @Override
    public void initialize(Phone constraintAnnotation) {

    }

    /**
     * 是否通过校验
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        Matcher m = phone.matcher(value);
        return m.matches();
    }
}