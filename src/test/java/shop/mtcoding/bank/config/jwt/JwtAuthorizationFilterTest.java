package shop.mtcoding.bank.config.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.config.enums.UserEnum;
import shop.mtcoding.bank.domain.user.User;

@Sql("classpath:db/truncate.sql") // 롤백 대신 사용 (auto_increment 초기화 + 데이터 비우기)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class JwtAuthorizationFilterTest {

  @Autowired
  private MockMvc mvc;

  @Test
  public void authorizaion_success_test() throws Exception {
    // given
    User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
    LoginUser loginUser = new LoginUser(user);
    String jwtToken = JwtProcess.create(loginUser);
    System.out.println("테스트 : " + jwtToken);

    // when
    ResultActions resultActions = mvc
        .perform(get("/api/user/test").header(JwtProperties.HEADER_STRING, jwtToken));

    // then
    resultActions.andExpect(status().isNotFound());
  }

  @Test
  public void authorizaion_failure_test() throws Exception {
    // given

    ResultActions resultActions = mvc
        .perform(get("/api/user/test"));

    // then
    resultActions.andExpect(status().isForbidden());
  }

}