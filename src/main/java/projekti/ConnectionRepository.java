package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Connection findByFromAndTo(Account from, Account to);

    List<Connection> findByToOrAccepted(Account to, boolean accepted);
    
    long deleteByFromAndTo(Account from, Account to);
}
