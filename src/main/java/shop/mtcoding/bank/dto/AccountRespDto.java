package shop.mtcoding.bank.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.dto.AccountRespDto.AccountListRespDto.AccountDto;

public class AccountRespDto {

    @Setter
    @Getter
    public static class AccountSaveRespDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }

    }

    @Getter
    @Setter
    public static class AccountListRespDto {
        // Entity 리턴하면 안 되니까 Dto로 각각 만들어서 리턴해야한다. 보안적으로 노출하면 안 되는 필드까지 다 던지기 때문
        private UserDto user;
        private List<AccountDto> accounts;

        public AccountListRespDto(List<Account> accounts) {
            // 목적 Entity를 Dto로 바꾸려고 한다.
            this.user = new UserDto(accounts.get(0).getUser());
            this.accounts = accounts.stream().map((account) -> new AccountDto(account)).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public class UserDto {
            private Long id; // user 필드
            private String fullName;

            public UserDto(User user) {
                this.id = user.getId();
                this.fullName = user.getFullName();
            }

        }

        @Getter
        @Setter
        public class AccountDto {
            private Long id;
            private Long number;
            private Long balalnce;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balalnce = account.getBalance();
            }

        }

    }
}
