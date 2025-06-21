package am.banking.system.security.domain.model;

import am.banking.system.common.shared.model.BaseEntity;
import am.banking.system.security.domain.enums.TokenPurpose;
import am.banking.system.security.domain.enums.TokenState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

/**
 * Author: Artyom Aroyan
 * Date: 16.04.25
 * Time: 23:15:05
 */
@Table
@Getter
@Builder
@AllArgsConstructor
public class UserToken extends BaseEntity {
    private final Integer userId;
    private final String token;
    private final Date expirationDate;
    private final TokenState tokenState;
    private final TokenPurpose tokenPurpose;
}