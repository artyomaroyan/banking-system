package am.banking.system.security.certification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Artyom Aroyan
 * Date: 19.05.25
 * Time: 04:08:32
 */
@Slf4j
@Component
public class CertificateAuthenticationConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.fromCallable(() -> {
            try {
                X509Certificate[] certificates = Objects.requireNonNull(
                        exchange.getRequest()
                                .getSslInfo(), "SSL info is missing")
                        .getPeerCertificates();

                assert certificates != null;
                if (certificates.length == 0) {
                    throw new BadCredentialsException("No client certificates found");
                }

                X509Certificate clientCertificate = certificates[0];
                String subjectDN = clientCertificate.getSubjectX500Principal().getName();

                String commonName = extractCommonName(subjectDN);
                if (commonName == null) {
                    throw new BadCredentialsException("CN not found in certificate");
                }
                return new CertificateAuthentication(List.of(), clientCertificate, commonName);

            } catch (Exception ex) {
                log.error("Failed to extract CN from client certificate", ex);
                throw new BadCredentialsException("Invalid client certificate", ex);
            }
        });
    }

    private String extractCommonName(String subjectDN) {
        Matcher matcher = Pattern.compile("CN=([^,]+)").matcher(subjectDN);
        return matcher.find() ? matcher.group(1) : null;
    }
}