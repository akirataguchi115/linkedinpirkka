package projekti;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor

@AllArgsConstructor
public class Picture extends AbstractPersistable<Long> {

    private String name;
    private String mediaType;
    private Long size;
//    Use this annotation when in Heroku environment(PostgreSQL)
//    @Type(type = "org.hibernate.type.BinaryType")
//    Use these when in local H2-environment
    @Lob
    @Basic(fetch = FetchType.LAZY)

    private byte[] content;
    @OneToMany(mappedBy = "picture")
    private List<Account> accounts;

}
