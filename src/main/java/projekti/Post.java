package projekti;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor

@AllArgsConstructor
public class Post extends AbstractPersistable<Long> {

    private String content;
    private Timestamp date;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
    private String url;
    @ManyToMany(mappedBy = "upvotes")
    private List<Account> upvotes;

}
