package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Connection findByFromAndTo(Account from, Account to);

    List<Connection> findByFromOrTo(Account from, Account to);
}
