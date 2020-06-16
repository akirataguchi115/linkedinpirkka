package projekti;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Account> findByUrl(String url);

    Skill findByNameAndUrl(String name, String url);
}
