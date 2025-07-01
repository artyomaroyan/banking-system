//package am.banking.system.notification.infrastructure.configuration;
//
//import jakarta.validation.constraints.AssertTrue;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Positive;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.validation.annotation.Validated;
//
///**
// * Author: Artyom Aroyan
// * Date: 01.07.25
// * Time: 22:50:12
// */
//@Validated
//@ConfigurationProperties(prefix = "spring.mail")
//public record MailConfiguration(
//        @NotBlank String host,
//        @Positive int port,
//        @NotBlank String username,
//        @NotBlank String password,
//        @NotBlank String protocol,
//        Properties properties
//) {
//
//    record Properties(
//            Smtp smtp
//    ) {
//    }
//
//    record Smtp(
//            @AssertTrue boolean auth,
//            StartTls startTls
//    ) {
//
//        record StartTls(
//                @AssertTrue boolean enable,
//                @AssertTrue boolean require
//        ) {
//        }
//    }
//}