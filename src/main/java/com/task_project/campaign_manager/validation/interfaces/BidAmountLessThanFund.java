package com.task_project.campaign_manager.validation.interfaces;

import com.task_project.campaign_manager.validation.impl.BidAmountLessThanFundValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BidAmountLessThanFundValidator.class)
public @interface BidAmountLessThanFund {
    String message() default "Bid amount must be less than fund";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
