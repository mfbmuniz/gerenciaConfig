package gerenciaconfigsrc.models;

import gerenciaconfigsrc.enums.SexEnum;
import gerenciaconfigsrc.models.dto.UserDto;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long idUser;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "sex")
    private String sex;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "age")
    private Integer age;

    @Column(name = "length")
    private double length;

    @Column(name = "weight")
    private double weight;

    @Column(name = "legal_document")
    private String legalDocument;

    @Column(name = "imc")
    private double imc;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;


    @Column(name = "phone1")
    private String phone1;

    @Column(name = "phone2")
    private String phone2;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "modified_at")
    private Date modifiedAt;

    public User(){}

    public User(String name,
                String email,
                String password,
                Date birthDate,
                String phone1,
                String phone2,
                String legal_document,
                Address address,
                SexEnum sex,
                List<Role> roles,
                Integer age,
                double length,
                double weight,
                String lastName,
                double imc,
                Date modifiedAt,
                Date createdAt,
                Date deletedAt

    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.sex = sex.getCode();
        this.legalDocument = legal_document;
        this.address = address;
        this.roles = roles;
        this.age=age;
        this.length=length;
        this.weight =weight;
        this.lastName=lastName;
        this.imc=imc;
        this.modifiedAt=modifiedAt;
        this.createdAt=createdAt;
        this.deletedAt = deletedAt;
    }

    public User(Long idUser,
                String name,
                String email,
                String password,
                Date birthDate,
                String phone1,
                String phone2,
                String legal_document,
                Address address,
                SexEnum sex,
                List<Role> roles,
                Date createdAt,
                Date deletedAt,
                Date modifiedAt,
                Integer age,
                double length,
                double weight,
                String lastName,
                double imc
    ) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.sex = sex.getCode();
        this.legalDocument = legal_document;
        this.address = address;
        this.roles = roles;
        this.age=age;
        this.length=length;
        this.weight = weight;
        this.lastName=lastName;
        this.imc= imc;
        this.modifiedAt=modifiedAt;
        this.createdAt=createdAt;
        this.deletedAt = deletedAt;
    }
    public User(Long idUser,String name, String email, String password, String sex, String legal_document, Address address,
                List<Role> roles, Date birthDate, String phone1, String phone2, Integer age,
                double length,
                double weight,
                String lastName,
                double imc,
                Date modifiedAt,
                Date createdAt,
                Date deletedAt) {

        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.password = password;
        this.sex = sex;
        this.legalDocument = legal_document;
        this.address = address;
        this.roles = roles;
        this.birthDate = birthDate;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.age=age;
        this.length=length;
        this.weight = weight;
        this.lastName=lastName;
        this.imc= imc;
        this.modifiedAt= modifiedAt;
        this.createdAt=createdAt;
        this.deletedAt = deletedAt;
    }



    public static User fromUserResponse(UserDto user){
        return null;
    }

}
