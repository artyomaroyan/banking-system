//package am.banking.system.security.certification;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//import static am.banking.system.common.enums.PermissionEnum.DO_INTERNAL_TASKS;
//import static am.banking.system.common.enums.RoleEnum.SYSTEM;
//
//**
// * Author: Artyom Aroyan
// * Date: 19.05.25
// * Time: 03:59:20
// */
//@Slf4j
//@Component
//public class CertificateAuthenticationManager implements ReactiveAuthenticationManager {
//
//    @Override
//    public Mono<Authentication> authenticate(Authentication authentication) {
//        if (!(authentication instanceof CertificateAuthentication auth)) {
//            return Mono.empty();
//        }
//
//        String cn = auth.getPrincipal().toString(); // CN stands for common name
//        log.info("Custom Log:: Authenticating CN: {}", cn);
//        if ("bank-user".equals(cn)) {
//            List<GrantedAuthority> authorities = List.of(
//                    new SimpleGrantedAuthority(SYSTEM.name()),
//                    new SimpleGrantedAuthority(DO_INTERNAL_TASKS.name())
//            );
//            log.info("Custom Log:: Authenticated CN: {}", cn);
//            log.info("Custom Log:: Authenticated Authorities: {}", authorities);
//            return Mono.just(new CertificateAuthentication(authorities, auth.getCertificate(), cn));
//        }
//        log.warn("Custom Log:: Untrusted Common Name: {} ", cn);
//        return Mono.error(new BadCredentialsException("Untrusted CN: " + cn));
//    }
//}