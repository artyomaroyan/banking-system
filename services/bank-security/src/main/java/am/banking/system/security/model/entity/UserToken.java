package am.banking.system.security.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.security.model.enums.TokenPurpose;
import am.banking.system.security.model.enums.TokenState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Artyom Aroyan
 * Date: 16.04.25
 * Time: 23:15:05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_token")
public class UserToken extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String token;
    private Date expirationDate;
    private TokenState tokenState;
    private TokenPurpose tokenPurpose;
}