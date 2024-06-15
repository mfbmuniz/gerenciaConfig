package gerenciaconfigsrc.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
@Table(name = "cities")
public class Cities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Long id_city;
    @Column(name = "city")
    private String city;

    public Cities(String city) {
        this.city = city;
    }

    public Cities() {

    }
}
