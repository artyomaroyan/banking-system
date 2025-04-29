package am.banking.system.user.model.entity;

import am.banking.system.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "user")
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
    @DBRef
    private Set<Role> roles;
}