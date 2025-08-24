package am.banking.system.account.application.configuration;

import am.banking.system.account.api.dto.AccountRequest;
import am.banking.system.account.domain.entity.Account;
import am.banking.system.common.shared.dto.account.AccountResponse;
import am.banking.system.common.shared.enums.AccountState;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Artyom Aroyan
 * Date: 22.07.25
 * Time: 01:15:24
 */
@Configuration
@RequiredArgsConstructor
public class AccountMappingConfiguration {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupRequestMapping() {
        modelMapper.createTypeMap(AccountRequest.class, Account.class)
                .setProvider(ctx -> {
                    AccountRequest request = (AccountRequest) ctx.getSource();
                    return new Account(
                            request.accountOwnerId(),
                            request.accountNumber(),
                            request.accountOwnerUsername(),
                            request.balance(),
                            request.accountType(),
                            request.currency(),
                            AccountState.ACTIVE,
                            1L
                    );
                });
    }

    @PostConstruct
    public void setupResponseMapping() {
        modelMapper.createTypeMap(Account.class, AccountResponse.class)
                .setProvider(ctx -> {
                    Account account = (Account) ctx.getSource();
                    return new AccountResponse(
                            account.getAccountOwnerId(),
                            account.getAccountNumber(),
                            account.getAccountOwnerUsername(),
                            account.getBalance(),
                            account.getAccountType(),
                            account.getCreatedAt()
                    );
                });
    }
}