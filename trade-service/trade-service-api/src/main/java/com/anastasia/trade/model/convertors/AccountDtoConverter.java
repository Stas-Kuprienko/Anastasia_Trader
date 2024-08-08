package com.anastasia.trade.model.convertors;

import com.anastasia.trade.entities.user.Account;
import com.anastasia.trade.entities.user.RiskProfile;
import com.anastasia.trade.model.AccountDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AccountDtoConverter implements Converter<Account, AccountDto> {

    private final RiskProfileDtoConverter riskProfileDtoConverter = new RiskProfileDtoConverter();

    @Override
    public AccountDto convert(@NonNull Account account) {
        return new AccountDto(
                account.getId(),
                account.getUser().getId(),
                account.getClientId(),
                account.getBroker().toString(),
                account.getToken(),
                riskProfileDtoConverter.convert(account.getRiskProfile())
        );
    }

    static class RiskProfileDtoConverter implements Converter<RiskProfile, AccountDto.RiskProfileDto> {

        @Override
        public AccountDto.RiskProfileDto convert(@NonNull RiskProfile riskProfile) {
            return new AccountDto.RiskProfileDto(

            );
        }
    }
}