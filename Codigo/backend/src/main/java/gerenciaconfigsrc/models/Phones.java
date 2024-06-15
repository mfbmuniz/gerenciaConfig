package gerenciaconfigsrc.models;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@ToString
@Table(name = "phones")
public class Phones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phones_id")
    private Long idPhones;

    @Column(name = "phone")
    private String phone;

    @OneToOne
    @JoinColumn(name = "country_code_id")
    private CountryCode idCountry;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User idUser;


    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    public Phones() {
    }

    public Phones(Long idPhones, String phone, CountryCode idCountry,
                  User idUser, Date createdAt, Date deletedAt) {
        this.idPhones = idPhones;
        this.phone = phone;
        this.idCountry = idCountry;
        this.idUser = idUser;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public Phones(String phone, CountryCode idCountry, User idUser,
                  Date createdAt, Date deletedAt) {
        this.phone = phone;
        this.idCountry = idCountry;
        this.idUser = idUser;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }
}
