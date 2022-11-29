package shop.mtcoding.bank.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import shop.mtcoding.bank.config.dummy.DummyEntity;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;

@ActiveProfiles("test")
@DataJpaTest // db관련된 것들만 메모리에 뜸.
public class AccountRepositoryTest extends DummyEntity {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private EntityManager em;

  // 메서드 실행전
  @BeforeEach
  public void setUp() {
    // 1. User생성
    User ssar = userRepository.save(newUser("ssar"));

    // 2. 계좌 생성
    Account ssarAccount = accountRepository.save(newAccount(1111L, ssar));
    Account ssarAccount2 = accountRepository.save(newAccount(2222L, ssar));
  }

  // 메서드 실행 이후
  @AfterEach // JpaTest는 Transactional 어노테이션이 있어서 메서드 실행 직후 자동 롤백됨.
  // @Rollback(value = false)쓰지 말자 => 테스트 격리 하지 않을 때만 사용
  public void tearDown() {
    // AI 초기화 설정
    em.createNativeQuery("ALTER TABLE transaction ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
    em.createNativeQuery("ALTER TABLE account ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
    em.createNativeQuery("ALTER TABLE users ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
  }

  @Test
  public void findByUserId_test() throws Exception {
    // given
    Long userId = 1L;

    // when
    List<Account> accountList = accountRepository.findByActiveUserId(userId);
    // Entity에서 toString 절대 사용하지마라!(lazyLoading 때문에 제대로 된 값이 나오지 않는다.)

    // then (실제값, 기댓값)
    assertThat(accountList.size()).isEqualTo(2);
    assertThat(accountList.get(0).getNumber()).isEqualTo(11112L);
  }
}
