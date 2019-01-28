package io.opentracing.contrib.spring.cloud.newspan;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("opentracing.spring.cloud.span")
public class SpanTracingProperties {

  private boolean enabled = true;
  private String skipPattern = "";

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getSkipPattern() {
    return skipPattern;
  }

  public void setSkipPattern(String skipPattern) {
    this.skipPattern = skipPattern;
  }

}
