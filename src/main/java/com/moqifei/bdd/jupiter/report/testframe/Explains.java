package com.moqifei.bdd.jupiter.report.testframe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Description:方法说明描述 <br>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Explains {

  /** 说明信息 */
  String value() default "";
}
