package am.banking.system.payment.domain.repository;

import am.banking.system.payment.domain.entity.Payment;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 20.08.25
 * Time: 01:41:30
 */
@Repository
public interface PaymentRepository extends R2dbcRepository<Payment, UUID> {
}