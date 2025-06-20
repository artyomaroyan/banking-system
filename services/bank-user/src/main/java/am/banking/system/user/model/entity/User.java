package am.banking.system.user.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.common.enums.AccountState;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Author: Artyom Aroyan
 * Date: 13.04.25
 * Time: 23:30:13
 */
@Getter
@AllArgsConstructor
@Table("user_db.usr.usr")
public class User extends BaseEntity {
    @Column(name = "username")
    private final String username;
    @Column(name = "first_name")
    private final String firstName;
    @Column(name = "last_name")
    private final String lastName;
    @Column(name = "email")
    private final String email;
    @Column(name = "password")
    private final String password;
    @Column(name = "phone")
    private final String phone;
    @Column(name = "age")
    private final Integer age;
    @Column(name = "account_state")
    private final AccountState accountState;
}