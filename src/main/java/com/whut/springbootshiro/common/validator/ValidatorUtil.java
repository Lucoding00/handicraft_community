package com.whut.springbootshiro.common.validator;

import com.whut.springbootshiro.common.Constant;
import com.whut.springbootshiro.common.exception.BussiException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorUtil {

    private static final Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static void validator(Object object) {

        Set<ConstraintViolation<Object>> validateSet = validator.validate(object);
        // 如果数据校验器校验的结果不为空 说明就存在数据校验不通过
        if (validateSet != null && !validateSet.isEmpty()) {
            // 遍历校验不通过的信息
            for (ConstraintViolation<Object> objectConstraintViolation : validateSet) {
                // 校验不通过的原因
                String message = objectConstraintViolation.getMessage();
                Integer code = Constant.PARAM_CHECKED_ERROR;
                throw new BussiException(code, message);
            }
        }
    }
}
