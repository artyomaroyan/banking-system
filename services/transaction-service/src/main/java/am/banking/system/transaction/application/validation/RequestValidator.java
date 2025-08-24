package am.banking.system.transaction.application.validation;

import am.banking.system.common.shared.enums.Currency;
import am.banking.system.transaction.api.dto.TransactionRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Author: Artyom Aroyan
 * Date: 21.08.25
 * Time: 23:59:56
 */
@Component
public class RequestValidator implements RequestValidation {
    private static final BigDecimal MIN_TRANSFER_AMOUNT_AMD = BigDecimal.valueOf(100.0);
    @Override
    public Mono<Boolean> isValidRequest(TransactionRequest request) {
        return null;
    }

    // ToDo: Amount Validation,
    //  Must be positive (amount > 0)
    private Mono<Boolean> isAmountPositive(BigDecimal amount) {
        return null;
    }

    // ToDo: Debit Account Balance Check
    //  Ensure sufficient funds in the debit account
    //  Consider reserved/blocked amounts (e.g., pending transactions)

    // ToDo: Credit Account Existence
    //  Verify the credit account exists
    //  Validate account status (active, closed, frozen)

    // ToDo: Currency Match / Conversion
    //  Debit and credit currency match, or Conversion rate exists and is applied correctly
    private Mono<Currency> checkCurrency(Currency currency, String debitAccount, String creditAccount) {
        return null;
    }

    // ToDo: Transaction Type Validation
    //  Ensure transaction type is allowed (transfer, payment, refund, etc.)

    // ToDo: User Ownership / Authorization
    //  Ensure user owns the debit account or has authority to act on it
    //  Optional: validate multi-user access rules if account is shared

    // ToDo: API Gateway / Auth Check
    //  You already noted this; in microservices, assume the API Gateway enforces authentication & basic permission,
    //  but adding a domain-level authorization check can prevent bypass

    // ToDo:Idempotency / Duplicate Prevention
    //  Validate transaction reference ID or idempotency key to avoid double processing

    // ToDo: Transaction Limits / Thresholds
    //  Daily / monthly transfer limits
    //  Max per transaction amount

    // ToDo: Account Blacklist / Sanctions
    //  Check if either account is flagged for fraud or restricted

    // ToDo: Self-Transfer Checks (optional)
    //  Decide if transferring to same account is allowed

    // ToDo: Request Schema Validation
    //  Non-null fields
    //  Amount is numeric, not negative
    //  Account numbers / IBAN format

    // ToDo: Date / Time Validation
    //  Transaction timestamp is valid and not in the future
    //  Optional: prevent backdated transactions

    // ToDo: Metadata Validation
    //  Optional: description length, allowed characters, tags

    // ToDo: KYC / AML Checks
    //  Large transfers trigger anti-money-laundering rules
    //  High-risk countries / accounts

    // ToDo: Transaction Fees Validation
    //  Ensure fees are calculated correctly if applicable

    // ToDo: Daily / Monthly Usage Quotas
    //  Prevent overuse per account or per user
}