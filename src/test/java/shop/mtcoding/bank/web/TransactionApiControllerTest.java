package shop.mtcoding.bank.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.bank.config.dummy.DummyEntity;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.TransactionReqDto.DepositReqDto;

@Sql("classpath:db/truncate.sql") // 롤백 대신 사용 (auto_increment 초기화 + 데이터 비우기)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // RANDOM_PORT로 하게 되면 실제 DB 환경으로 하는데, 그러면 격리가 되지 않는다.
public class TransactionApiControllerTest extends DummyEntity {

  private static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";
  private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded; charset=utf-8";

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper om;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @BeforeEach
  public void setUp() {
    User ssar = newUser("ssar");
    userRepository.save(ssar);
    Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
    Account ssarAccount2 = accountRepository.save(newAccount(2222L, ssar));
  }

  @Test
  public void deposit_test() throws Exception {
    // given
    DepositReqDto depositReqDto = new DepositReqDto();
    depositReqDto.setNumber(1111L);
    depositReqDto.setAmount(500L);
    depositReqDto.setGubun("DEPOSIT");
    String requestBody = om.writeValueAsString(depositReqDto);
    System.out.println("테스트 : " + requestBody);

    // when
    ResultActions resultActions = mvc
        .perform(post("/api/deposit").content(requestBody)
            .contentType(APPLICATION_JSON_UTF8));
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    System.out.println("테스트 : " + responseBody);

    // then
    resultActions.andExpect(status().isCreated());
    resultActions.andExpect(jsonPath("$.data.from").value("ATM"));
  }
}
