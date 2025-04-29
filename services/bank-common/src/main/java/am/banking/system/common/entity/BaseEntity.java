package am.banking.system.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Author: Artyom Aroyan
 * Date: 24.04.25
 * Time: 01:48:49
 */
@Getter
@Document
public abstract class BaseEntity {
    @Id
    private Long id;
    @Setter
    @LastModifiedDate
    private LocalDateTime updatedAt;
}