package am.banking.system.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 18.06.25
 * Time: 00:24:16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usr.user_role")
public class UserRole {
    @Id
    @Column("id")
    private UUID id;
    @Column("user_id")
    private UUID userId;
    @Column("role_id")
    private UUID roleId;

    public UserRole(UUID userId, UUID roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}