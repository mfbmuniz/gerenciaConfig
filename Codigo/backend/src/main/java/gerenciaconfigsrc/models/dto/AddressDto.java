package gerenciaconfigsrc.models.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String street;
    private Integer number;
    private String district;
    private String city;
    private String state;

    private Long addressId;

    private String zipCode;

    public AddressDto(String street, Integer number, String district, String city, String state, String zipCode) {
        this.street = street;
        this.number = number;
        this.district = district;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public AddressDto() {
    }
}
