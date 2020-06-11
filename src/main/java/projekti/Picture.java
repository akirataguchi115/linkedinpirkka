package projekti;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor

@AllArgsConstructor
public class Picture extends AbstractPersistable<Long> {

    private String name;
    private String mediaType;
    private Long size;
    //use this annotation when in Heroku environment(PostgreSQL)
    @Type(type = "org.hibernate.type.BinaryType")
//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//use these when in local H2-environment
    private byte[] content;
    @OneToMany(mappedBy = "picture")
    private List<Account> accounts;

}
