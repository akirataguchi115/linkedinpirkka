package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Connection findByFromAndTo(Account from, Account to);

    List<Connection> findByToAndAcceptedOrFromAndAccepted(Account to, boolean accepted, Account from, boolean accept);
    
    long deleteByFromAndTo(Account from, Account to);
}