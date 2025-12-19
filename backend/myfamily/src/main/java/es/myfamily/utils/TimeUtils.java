package es.myfamily.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

@Component
public class TimeUtils {

  public Long getCurrentTimestamp() {
    return System.currentTimeMillis();
  }

  public Long convertToTimestamp(LocalDateTime dateTime) {
    return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
  }

  public LocalDateTime convertFromTimestamp(Long timestamp) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
  }

  public Integer getCurrentMonth() {
    return LocalDateTime.now().getMonthValue();
  }

  public Integer getCurrentYear() {
    return LocalDateTime.now().getYear();
  }

}
