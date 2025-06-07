package am.banking.system.security.model.entity;

import am.banking.system.security.model.enums.TokenPurpose;
import am.banking.system.security.model.enums.TokenState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Author: Artyom Aroyan
 * Date: 16.04.25
 * Time: 23:15:05
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_token", schema = "security")
public class UserToken implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long userId;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenState tokenState;
    @Enumerated(EnumType.STRING)
    private TokenPurpose tokenPurpose;
    private Date expirationDate;
}