package com.bosssoft.pay.sdk.core.internal.validation;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Title 校验工具类
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public class ValidationUtils {

    // private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(false)
            .buildValidatorFactory()
            .getValidator();

    private static Validator failFastValidator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory()
            .getValidator();

    /**
     * 校验对象(默认模式)
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> ValidationResult validate(T obj){
        return validate(validator, obj);
    }


    /**
     * 校验属性(默认模式)
     * @param obj
     * @param propertyName
     * @param <T>
     * @return
     */
    public static <T> ValidationResult validateProperty(T obj, String propertyName) {
        return validateProperty(validator, obj, propertyName);
    }

    /**
     * 校验对象(快速失败模式)
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> ValidationResult validateByFailFast(T obj){
        return validate(failFastValidator, obj);
    }


    /**
     * 校验属性(快速失败模式)
     * @param obj
     * @param propertyName
     * @param <T>
     * @return
     */
    public static <T> ValidationResult validatePropertyByFailFast(T obj, String propertyName) {
        return validateProperty(failFastValidator, obj, propertyName);
    }

    /**
     * 校验对象
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> ValidationResult validate(Validator validator, T obj){
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if( CollectionUtils.isNotEmpty(set) ){
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<String, String>();
            for(ConstraintViolation<T> cv : set){
                errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }

    /**
     * 校验属性
     * @param obj
     * @param propertyName
     * @param <T>
     * @return
     */
    public static <T> ValidationResult validateProperty(Validator validator, T obj, String propertyName){
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, Default.class);
        if( CollectionUtils.isNotEmpty(set) ){
            result.setHasErrors(true);
            Map<String,String> errorMsg = new HashMap<String,String>();
            for(ConstraintViolation<T> cv : set){
                errorMsg.put(propertyName, cv.getMessage());
            }
            result.setErrorMsg(errorMsg);
        }
        return result;
    }
}
