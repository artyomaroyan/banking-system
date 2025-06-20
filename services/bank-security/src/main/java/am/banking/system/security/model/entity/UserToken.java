package am.banking.system.security.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.security.model.enums.TokenPurpose;
import am.banking.system.security.model.enums.TokenState;
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