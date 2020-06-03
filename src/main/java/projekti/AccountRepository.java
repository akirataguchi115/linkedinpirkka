package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByEmail(String email);

    Account findByUrl(String url);

    List<Account> findByNameContainingIgnoreCase(String name);
}
