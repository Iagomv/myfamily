package es.myfamily.utils;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvitationCodeUtils {

  @Value("${invitation.code.length}")
  private int codeLength;

  private static InvitationCodeUtils instance;

  public InvitationCodeUtils() {
    instance = this;
  }

  public static String generateInvitationCode() {
    int length = instance != null ? instance.codeLength : 8;
    SecureRandom secureRandom = new SecureRandom();
    byte[] randomBytes = new byte[length];
    secureRandom.nextBytes(randomBytes);
    String encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    return encoded.substring(0, Math.min(length, encoded.length()));
  }
}
