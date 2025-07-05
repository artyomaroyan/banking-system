package am.banking.system.security.application.port.out;

/**
 * Author: Artyom Aroyan
 * Date: 05.07.25
 * Time: 15:00:41
 */
public interface CustomPasswordEncoder {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}