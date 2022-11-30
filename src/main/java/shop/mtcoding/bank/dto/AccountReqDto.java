package shop.mtcoding.bank.dto;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.user.User;

public class AccountReqDto {

    @Setter
    @Getter
    public static class AccountSaveReqDto {
        private Long number;
        private String password;

        public Account toEntity(User user) {
            return Account.builder()
                    .password(password)
                    .number(number)
                    .balance(1000L)
                    .isActive(true)
                    .build();
        }
    }

    @Setter
    @Getter
    public static class AccountDeleteReqDto {
        // 서비스 로직을 여기 넣고 싶을 때 사용
        // private Long userId;
        private String password;
    }
}
