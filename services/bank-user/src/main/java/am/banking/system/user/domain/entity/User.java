package am.banking.system.user.domain.entity;

import am.banking.system.common.shared.enums.AccountState;
import am.banking.system.common.shared.model.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Author: Artyom Aroyan
 * Date: 13.04.25
 * Time: 23:30:13
 */
@Getter
@Builder
@Table("usr.usr")
public class User extends BaseEntity {
    @Column("username")
    private final String username;
    @Column("first_name")
    private final String firstName;
    @Column("last_name")
    private final String lastName;
    @Column("email")
    private final String email;
    @Column("password")
    private final String password;
    @Column("phone")
    private final String phone;
    @Column("age")
    private final Integer age;
    @Column("account_state")
    private final AccountState accountState;
}