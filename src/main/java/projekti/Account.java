package projekti;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor

@AllArgsConstructor
public class Account extends AbstractPersistable<Long> {

    private String email;
    private String password;
    private String name;
    private String url;
    @JoinTable(
            name = "Connection",
            joinColumns = @JoinColumn(name = "accountOneId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    @ManyToMany
    List<Account> connections = new ArrayList<>();
}
