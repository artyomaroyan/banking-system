//package am.banking.system.security.infrastructure.webclient.configuration;
//
//import am.banking.system.common.infrastructure.tls.WebClientFactory;
//import am.banking.system.common.infrastructure.tls.configuration.TlsProperties;
//import am.banking.system.security.application.port.in.UserTokenUseCase;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.ClientRequest;
//import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import static am.banking.system.common.infrastructure.tls.WebClientFilter.errorResponseFilter;
//import static org.springframework.http.HttpHeaders.AUTHORIZATION;
//import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//**
// * Author: Artyom Aroyan
// * Date: 20.04.25
// * Time: 00:49:55
// */
//@Configuration
//@RequiredArgsConstructor
//public class SecurityWebClientConfiguration {
//    private final UserTokenUseCase userTokenService;
//    private final TlsProperties tlsProperties;
//
//    @Bean(name = "internalWebClient")
//    public WebClient internalWebClient() {
//        return WebClient.builder()
//                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
//                .filter(errorResponseFilter())
//                .build();
//    }
//
//    @Bean(name = "securedWebClient")
//    public WebClient securedWebClient(WebClientFactory webClientFactory) {
//        return webClientFactory.createSecuredWebClient(tlsProperties.url())
//                .mutate()
//                .filter(systemTokenPropagationFilter())
//                .filter(errorResponseFilter())
//                .build();
//    }
//
//    private ExchangeFilterFunction systemTokenPropagationFilter() {
//        return ExchangeFilterFunction.ofRequestProcessor(request ->
//                userTokenService.generateSystemToken()
//                        .map(token -> ClientRequest.from(request)
//                                .header(AUTHORIZATION, "Bearer " + token)
//                                .build()));
//    }
//}