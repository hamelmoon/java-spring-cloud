package io.opentracing.contrib.spring.cloud.newspan;

import io.opentracing.Tracer;
import io.opentracing.contrib.spring.tracer.configuration.TracerAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ConditionalOnBean(Tracer.class)
@AutoConfigureAfter(TracerAutoConfiguration.class)
@ConditionalOnProperty(name = "opentracing.spring.cloud.span.enabled", havingValue = "true")
@EnableConfigurationProperties(SpanTracingProperties.class)
public class SpanAutoConfiguration {

  private static final Logger log = LoggerFactory
      .getLogger(SpanAutoConfiguration.class.getName());

  @Bean
  @ConditionalOnClass(name = "org.aspectj.lang.ProceedingJoinPoint")
  public NewSpanAspect newSpanAspect(Tracer tracer,
                                     SpanTracingProperties spanTracingProperties) {
    log.info("----------------- NewSpanAspect Activated ---------------");
    return new NewSpanAspect(tracer, spanTracingProperties);
  }

}
