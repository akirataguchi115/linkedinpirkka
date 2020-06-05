package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Connection findByFromAndTo(Account from, Account to);
}
