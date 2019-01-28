package io.opentracing.contrib.spring.cloud.hystrix;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Order(-1)
public class HystrixCommandAspect {

  private static final String COMPONENT_NAME = "HystrixCommandAspect";
  private static final String CLASS_KEY = "class";
  private static final String METHOD_KEY = "method";

  private Tracer tracer;

  public HystrixCommandAspect(Tracer tracer) {
    this.tracer = tracer;
  }

//  @Pointcut("")
//  public void execute() {}

  @Around("@annotation(com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand)")
  public Object traceHystrix(final ProceedingJoinPoint pjp) throws Throwable {
    Scope scope = tracer.buildSpan("HystrixCommand-" + pjp.getSignature().getName())
        .withTag(Tags.COMPONENT.getKey(), COMPONENT_NAME)
        .withTag(CLASS_KEY, pjp.getTarget().getClass().getSimpleName())
        .withTag(METHOD_KEY, pjp.getSignature().getName())
        .startActive(true);
    try {
      return pjp.proceed();
    } catch (Exception ex) {
      captureException(scope.span(), ex);
      throw ex;
    } finally {
      scope.close();
    }
  }

  private static void captureException(Span span, Exception ex) {
    Map<String, Object> exceptionLogs = new LinkedHashMap<>(2);
    exceptionLogs.put("event", Tags.ERROR.getKey());
    exceptionLogs.put("error.object", ex);
    span.log(exceptionLogs);
    Tags.ERROR.set(span, true);
  }
}
