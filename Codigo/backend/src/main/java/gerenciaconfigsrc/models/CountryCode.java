package gerenciaconfigsrc.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
@Table(name = "country_code")
public class CountryCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_code_id")
    private Long countryCodeId;

    @Column(name = "country_code")
    Integer countryCode;

    public CountryCode(Long countryCodeId, Integer countryCode) {
        this.countryCodeId = countryCodeId;
        this.countryCode = countryCode;
    }

    public CountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    public CountryCode() {
    }
}
