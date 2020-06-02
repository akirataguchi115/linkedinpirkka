package projekti;

import javax.persistence.Entity;
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
    private String url;
    private String firstName;
    private String lastName;
}
