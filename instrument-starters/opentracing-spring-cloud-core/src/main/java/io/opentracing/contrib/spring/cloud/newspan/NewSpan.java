package io.opentracing.contrib.spring.cloud.newspan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(value = {
    ElementType.METHOD
})
public @interface NewSpan {

  /**
   * @return - The name of the span which will be created. Default is the annotated method's name
   * separated by hyphens.
   */
  @AliasFor("value")
  String name() default "";

  /**
   * @return - The name of the span which will be created. Default is the annotated method's name
   * separated by hyphens.
   */
  @AliasFor("name")
  String value() default "";

}
