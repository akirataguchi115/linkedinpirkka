package projekti;

import java.util.List;
import javax.persistence.CascadeType;
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
    @OneToMany(mappedBy = "from")
    private List<Connection> sentRequests;
    @OneToMany(mappedBy = "to")
    private List<Connection> receivedRequests;
    @OneToMany(mappedBy = "account")
    private List<Comment> comments;
    @ManyToOne(cascade = CascadeType.ALL)
    private Picture picture;
    @JoinTable(
            name = "Commend",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id")
    )
    @ManyToMany
    List<Skill> commends;
    @JoinTable(
            name = "Upvote",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id")
    )
    @ManyToMany
    List<Post> upvotes;
}
