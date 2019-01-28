package io.opentracing.contrib.spring.cloud.newspan;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.cloud.SpanUtils;
import io.opentracing.tag.Tags;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.regex.Pattern;

@Aspect
public class ContinueSpanAspect {

  private static final String COMPONENT_NAME = "continueSpan";
  private static final String CLASS_KEY = "class";
  private static final String METHOD_KEY = "method";

  private Tracer tracer;
  private Pattern skipPattern;

  public ContinueSpanAspect(Tracer tracer, SpanTracingProperties spanTracingProperties) {
    this.tracer = tracer;
    this.skipPattern = Pattern.compile(spanTracingProperties.getSkipPattern());
  }

  @Around("@annotation(io.opentracing.contrib.spring.cloud.newspan.ContinueSpan)")
  public Object traceBackgroundThread(final ProceedingJoinPoint pjp) throws Throwable {
    if (skipPattern.matcher(pjp.getTarget().getClass().getName()).matches()) {
      return pjp.proceed();
    }

    // operation name is method name
    tracer.buildSpan(pjp.getSignature().getName())
        .withTag(Tags.COMPONENT.getKey(), COMPONENT_NAME)
        .withTag(CLASS_KEY, pjp.getTarget().getClass().getSimpleName())
        .withTag(METHOD_KEY, pjp.getSignature().getName())
        .start();
    return pjp.proceed();
  }
}
