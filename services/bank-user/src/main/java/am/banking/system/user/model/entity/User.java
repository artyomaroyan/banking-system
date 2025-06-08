package am.banking.system.user.model.entity;

import am.banking.system.common.entity.BaseEntity;
import am.banking.system.common.enums.AccountState;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * Author: Artyom Aroyan
 * Date: 13.04.25
 * Time: 23:30:13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usr", schema = "usr")
public class User extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Integer age;
    private AccountState state;
    private Set<Role> roles;
}