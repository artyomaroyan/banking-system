package am.banking.system.account.service;

import am.banking.system.account.model.dto.AccountRequest;
import am.banking.system.account.model.dto.AccountResponse;
import am.banking.system.common.reponse.Result;

/**
 * Author: Artyom Aroyan
 * Date: 01.06.25
 * Time: 15:52:35
 */
public interface IAccountCreationService {
    Result<AccountResponse> createDefaultAccount(AccountRequest request);
}