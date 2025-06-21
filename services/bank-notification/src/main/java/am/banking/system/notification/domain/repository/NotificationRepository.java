package am.banking.system.notification.domain.repository;

import am.banking.system.notification.domain.entity.Notification;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Artyom Aroyan
 * Date: 01.05.25
 * Time: 00:47:23
 */
@Repository
public interface NotificationRepository extends ReactiveCrudRepository<Notification, Integer> {
}