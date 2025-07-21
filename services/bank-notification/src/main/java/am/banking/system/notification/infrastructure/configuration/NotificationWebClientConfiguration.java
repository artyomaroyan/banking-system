//package am.banking.system.notification.infrastructure.configuration;
//
//import am.banking.system.common.infrastructure.tls.WebClientFactory;
//import am.banking.system.common.infrastructure.tls.configuration.TlsProperties;
//import am.banking.system.notification.application.port.out.InternalTokenClientPort;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.ClientRequest;
//import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import static am.banking.system.common.infrastructure.tls.WebClientFilter.errorResponseFilter;
//import static org.springframework.http.HttpHeaders.AUTHORIZATION;
//
//**
// * Author: Artyom Aroyan
// * Date: 12.05.25
// * Time: 02:53:01
// */
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class NotificationWebClientConfiguration {
//    private final TlsProperties tlsProperties;
//
//    @Bean
//    public WebClient securedWebClient(WebClientFactory webClientFactory) {
//        return webClientFactory.createSecuredWebClient(tlsProperties.url());
//    }
//
//    @Bean(name = "notificationWebClient")
//    public WebClient notificationWebClient(WebClientFactory webClientFactory, ObjectProvider<InternalTokenClientPort> jwtProvider) {
//        return webClientFactory.createSecuredWebClient(tlsProperties.url())
//                .mutate()
//                .filter(systemTokenPropagationFilter(jwtProvider))
//                .filter(errorResponseFilter())
//                .build();
//    }
//
//    private ExchangeFilterFunction systemTokenPropagationFilter(ObjectProvider<InternalTokenClientPort> jwtProvider) {
//        return ExchangeFilterFunction.ofRequestProcessor(request ->
//                jwtProvider.getObject()
//                        .generateSystemToken()
//                        .map(token -> ClientRequest.from(request)
//                                .header(AUTHORIZATION, "Bearer " + token)
//                                .build()));
//    }
//}