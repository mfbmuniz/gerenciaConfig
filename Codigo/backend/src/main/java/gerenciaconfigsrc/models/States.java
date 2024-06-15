package gerenciaconfigsrc.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
@Table(name = "states")
public class States {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "state_id")
    private Long id_state;

    private String state;
    private String uf;



    public States(String state, String uf) {
        this.state = state;
        this.uf = uf;
    }

    public States() {

    }
}
