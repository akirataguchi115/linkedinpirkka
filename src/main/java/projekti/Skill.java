package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor

@AllArgsConstructor
public class Skill extends AbstractPersistable<Long> {

    private String name;
    @ManyToMany(mappedBy = "commends")
    private List<Account> commends;

}
