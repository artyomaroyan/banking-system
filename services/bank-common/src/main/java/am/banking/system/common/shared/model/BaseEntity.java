package am.banking.system.common.shared.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

/**
 * Author: Artyom Aroyan
 * Date: 24.04.25
 * Time: 01:48:49
 */
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @Column("id")
    private Integer id;
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}