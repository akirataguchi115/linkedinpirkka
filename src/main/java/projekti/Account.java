package projekti;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    List<Account> connections;
    @JoinTable(
            name = "Commend",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id")
    )
    @ManyToMany
    List<Skill> commends;
    @OneToMany(mappedBy = "account")
    private List<Post> posts;
    @OneToMany(mappedBy = "account")
    private List<Comment> comments;
    @ManyToOne
    private Picture picture;
}
