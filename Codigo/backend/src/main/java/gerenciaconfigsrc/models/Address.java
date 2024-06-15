package gerenciaconfigsrc.models;

import gerenciaconfigsrc.models.dto.AddressDto;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@ToString
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;
    @Column(name = "street")
    private String street;
    @Column(name = "number")
    private Integer number;
    @Column(name = "district")
    private String district;

    @Column(name = "zip_code")
    private String zipCode;

    @OneToOne
    @JoinColumn(name = "city_id")
    private Cities city;

    @OneToOne
    @JoinColumn(name = "state_id")
    private States state;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    public Address(String street, Integer number, String district, Cities city, States state, String zipCode) {
        this.street = street;
        this.number = number;
        this.district = district;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Address() {

    }

    public static Address fromAddressDTO(AddressDto address, Cities city, States states) {

        return new Address(
                address.getStreet(),
                address.getNumber(),
                address.getDistrict(),
                city,
                states,
                address.getZipCode()
        );
    }
}

