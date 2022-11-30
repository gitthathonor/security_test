package shop.mtcoding.bank.domain.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.mtcoding.bank.config.exception.CustomApiException;
import shop.mtcoding.bank.domain.AudingTime;
import shop.mtcoding.bank.domain.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "account")
@Entity
public class Account extends AudingTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private Long number;

    @Column(nullable = false, length = 4)
    private String password;

    @Column(nullable = false)
    private Long balance; // 잔액 (디폴트 값 1000원)

    // 커멜케이스는 DB에 언더스코어로 생성된다.
    @Column(nullable = false)
    private Boolean isActive; // 계좌 활성화 여부

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Account(Long id, Long number, String password, Long balance, Boolean isActive, User user) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.isActive = isActive;
        this.user = user;
    }

    // 도메인 비즈니스 로직
    // 엔티티에 setter로 값을 들고 와서 처리하는 것이 아니라 엔티티 내에 로직을 만들고 그 메서드를 호출하는 방식으로 해야한다.
    // 계좌 소유주 확인
    public void isOwner(Long userId) {
        if (user.getId() != userId) {
            throw new CustomApiException("해당 계좌의 소유자가 아닙니다.", HttpStatus.FORBIDDEN);
        }
    }

    // 계좌 패스워드 확인
    public void checkPassword(String password) {
        if(this.password != password) {
            throw new CustomApiException("계좌 패스워드가 틀렸습니다.", HttpStatus.BAD_REQUEST)
        }
    }

    // 계좌 비활성화하기
    public void deActiveAccount() {
        isActive = false;
    }

    // 계좌 삭제 팩토리 메서드
    public void deleteAccount(Long userId, String password) {
        isOwner(userId);
        checkPassword(password);
        deActiveAccount();
    }
}
