package am.banking.system.user.infrastructure.security.abstraction;

/**
 * Author: Artyom Aroyan
 * Date: 02.05.25
 * Time: 00:21:22
 */
public interface IPasswordServiceClient {
    String hashPassword(String password);
    Boolean validatePassword(String rawPassword, String hashedPassword);
}