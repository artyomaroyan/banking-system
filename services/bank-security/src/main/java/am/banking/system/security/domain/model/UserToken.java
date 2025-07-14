package am.banking.system.security.domain.model;

import am.banking.system.common.shared.model.BaseEntity;
import am.banking.system.security.domain.enums.TokenPurpose;
import am.banking.system.security.domain.enums.TokenState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Author: Artyom Aroyan
 * Date: 16.04.25
 * Time: 23:15:05
 */
@Getter
@Builder
@AllArgsConstructor
@Table("security.user_token")
public class UserToken extends BaseEntity {
    @Column("user_id")
    private final Integer userId;
    @Column("token")
    private final String token;
    @Column("expires_at")
    private final LocalDateTime expirationDate;
    @Column("token_state")
    private final TokenState tokenState;
    @Column("token_purpose")
    private final TokenPurpose tokenPurpose;
}