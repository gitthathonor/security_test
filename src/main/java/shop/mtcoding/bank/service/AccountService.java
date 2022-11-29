package shop.mtcoding.bank.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.config.exception.CustomApiException;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.AccountRespDto.AccountListRespDto;
import shop.mtcoding.bank.dto.AccountRespDto.AccountSaveRespDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserRepository userRepository;
  private final AccountRepository accountRepository;

  @Transactional
  public AccountSaveRespDto 계좌생성(AccountSaveReqDto accountSaveReqDto, Long userId) {
    log.debug("디버그 : AccountService 계좌생성 서비스 호출됨");
    // 어떤 것을 넣을 때는 항상 검증이 필요하다.
    // 1. 검증(권한, 값 검증)
    User userPS = userRepository.findById(userId)
        .orElseThrow(() -> new CustomApiException("탈퇴된 유저로 계좌를 생성할 수 없습니다.", HttpStatus.FORBIDDEN));

    // 2. 실행
    Account account = accountSaveReqDto.toEntity(userPS);
    // 새로운 계좌가 생성, user는 현재 세션에 정보가 있는 user이다.
    Account accountPS = accountRepository.save(account);

    // 3. Dto 응답
    return new AccountSaveRespDto(accountPS);
  }

  // 본인계좌목록보기
  public AccountListRespDto 본인_계좌목록보기(Long userId) {
    List<Account> accountListPS = accountRepository.findByActiveUserId(userId);
    return new AccountListRespDto(accountListPS);
  }

  // 계좌상세보기(Account + LIst<Transaction>) Transaction 구현하고 만들기

  // 본인계좌삭제하기

}
