//package am.banking.system.security.certification;
//
//import lombok.Getter;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//
//import java.security.cert.X509Certificate;
//import java.util.Collection;
//import java.util.Objects;
//
//**
// * Author: Artyom Aroyan
// * Date: 19.05.25
// * Time: 03:55:00
// */
//public class CertificateAuthentication extends AbstractAuthenticationToken {
//    @Getter
//    private final X509Certificate certificate;
//    private final String principal;
//
//    public CertificateAuthentication(Collection<? extends GrantedAuthority> authorities, X509Certificate certificate, String principal) {
//        super(authorities);
//        this.certificate = certificate;
//        this.principal = principal;
//        setAuthenticated(true);
//    }
//
//    @Override
//    public Object getCredentials() {
//        return certificate;
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return principal;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//        CertificateAuthentication that = (CertificateAuthentication) o;
//        return Objects.equals(certificate, that.certificate) && Objects.equals(principal, that.principal);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), certificate, principal);
//    }
//}