package shop.mtcoding.bank.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("select u from User u join fetch u.accounts ac where ac.isActive = true and u.id = :userId")
    // 기본적으로 User객체가 Account를 들고 있지 않기 때문에 오류가 뜰 것
    // 그래서 해결하기 위해서 양방향 매핑을 사용해야 한다.
    User findByActiveUserIdV3(@Param("userId") Long userId);
}
