package am.banking.system.notification.infrastructure.configuration;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Author: Artyom Aroyan
 * Date: 01.07.25
 * Time: 23:19:53
 */
@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class EmailConfiguration {
    @NotBlank
    @Value("${spring.mail.host}")
    private String host;
    @Positive
    @Value("${spring.mail.port}")
    private int port;
    @NotBlank
    @Value("${spring.mail.username}")
    private String username;
    @NotBlank
    @Value("${spring.mail.password}")
    private String password;
    @NotBlank
    @Value("${spring.mail.protocol}")
    private String protocol;
    @AssertTrue
    @Value("${spring.mail.properties.mail.auth}")
    private boolean auth;
    @AssertTrue
    @Value("${spring.mail.properties.mail.starttls.enable}")
    private boolean enable;
    @AssertTrue
    @Value("${spring.mail.properties.mail.starttls.required}")
    private boolean require;

//    @Valid
//    private Properties properties = new Properties();
//
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class Properties {
//        @Valid
//        private Smtp smtp = new Smtp();
//
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Smtp {
//            @AssertTrue
//            private boolean auth;
//            @Valid
//            private StartTls startTls = new StartTls();
//
//            @Getter
//            @Setter
//            @NoArgsConstructor
//            @AllArgsConstructor
//            public static class StartTls {
//                @AssertTrue
//                private boolean enable;
//                @AssertTrue
//                private boolean require;
//            }
//        }
//    }
}