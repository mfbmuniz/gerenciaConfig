package gerenciaconfigsrc.models.RequestEntity;

import gerenciaconfigsrc.enums.RolesEnum;
import gerenciaconfigsrc.models.Address;
import gerenciaconfigsrc.models.Role;
import gerenciaconfigsrc.models.User;
import gerenciaconfigsrc.models.dto.AddressDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
public class UserRequest {

    @NotNull(message = "Campo name não pode ser nulo")
    @NotEmpty(message = "Campo name não pode ser vazio")
    private String name;
    @NotNull(message = "Campo email não pode ser nulo")
    @NotEmpty(message = "Campo email não pode ser vazio")
    @Email(message = "Digite um email válido")
    private String email;
    @NotNull(message = "Campo password não pode ser nulo")
    @NotEmpty(message = "Campo password não pode ser vazio")
    private String password;

    @NotNull(message = "Campo roles não pode ser nulo")
    private List<String> roles;

    @NotNull(message = "Campo legal_document não pode ser nulo")
    @NotEmpty(message = "Campo legal_document não pode ser vazio")
    private String legalDocument;

    @NotNull(message = "Campo address não pode ser nulo")
    private AddressDto address;

    @NotNull(message = "Campo sex não pode ser nulo")
    @NotEmpty(message = "Campo sex não pode ser vazio")
    private String sex;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthDate;

    @NotNull(message = "Campo phone1 não pode ser nulo")
    @NotEmpty(message = "Campo phone1 não pode ser vazio")
    private String phone1;

    @NotNull(message = "Campo phone2 não pode ser nulo")
    @NotEmpty(message = "Campo phone2 não pode ser vazio")
    private String phone2;
    private Long idUser;
    @NotNull(message = "Campo phone2 não pode ser nulo")
    @NotEmpty(message = "Campo phone2 não pode ser vazio")
    private String age;

    @NotNull(message = "Campo phone2 não pode ser nulo")
    @NotEmpty(message = "Campo phone2 não pode ser vazio")
    private String length;

    @NotNull(message = "Campo phone2 não pode ser nulo")
    @NotEmpty(message = "Campo phone2 não pode ser vazio")
    private String weigth;

    @NotNull(message = "Campo phone2 não pode ser nulo")
    @NotEmpty(message = "Campo phone2 não pode ser vazio")
    private String lastName;

    @NotNull(message = "Campo phone2 não pode ser nulo")
    @NotEmpty(message = "Campo phone2 não pode ser vazio")
    private String imc;
    private String modifiedAt;
    private Date createdAt;
    private Date deletedAt;

    public UserRequest() {
    }

    public UserRequest(String name, String email, String password, List<String> roles, String legalDocument,
                       AddressDto address, String sex, Date birthDate, String phone1, String phone2,
                       Long idUser, String age, String length, String weigth, String lastName, String imc) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.legalDocument = legalDocument;
        this.address = address;
        this.sex = sex;
        this.birthDate = birthDate;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.idUser = idUser;
        this.age=age;
        this.length=length;
        this.weigth=weigth;
        this.lastName=lastName;
        this.imc=imc;

    }

    public UserRequest(String name, String email, String password, List<String> roles, String legalDocument,
                       AddressDto address, String sex, Date birthDate, String phone1, String phone2,
                       String age, String length, String weigth, String lastName, String imc) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.legalDocument = legalDocument;
        this.address = address;
        this.sex = sex;
        this.birthDate = birthDate;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.age=age;
        this.length=length;
        this.weigth=weigth;
        this.lastName=lastName;
        this.imc=imc;
    }


    public static User toUser(UserRequest user, List<Role> roles, Address address) {
        Date dueDate;
        Calendar c = Calendar.getInstance();

        /*if(roles.stream().anyMatch(f -> f.getName().equals(RolesEnum.ALUNO.getCode()))) {
            c.setTime(user.getDueDate());
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            dueDate = c.getTime();
        }else {
            dueDate = null;
        }*/

        c.setTime(user.getBirthDate());
        Date birthDate = c.getTime();

        User newUSer;

        if(!roles.stream().anyMatch(f -> f.getName().equals(RolesEnum.USER.getCode()))) {
            newUSer = new User(
                    user.getIdUser() != null ? user.getIdUser() : 0,
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getSex(),
                    user.getLegalDocument(),
                    address,
                    roles,
                    birthDate,
                    user.getPhone1(),
                    user.getPhone2(),
                    Integer.parseInt(user.getAge()),
                    Double.parseDouble(user.getLength()),
                    Double.parseDouble(user.getWeigth()),
                    user.getLastName(),
                    Double.parseDouble(user.getImc()),
                    null,
                    new Date(),
                    null
                    );

        }else {

            newUSer = new User(

                    user.getIdUser() != null ? user.getIdUser() : 0,
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getSex(),
                    user.getLegalDocument(),
                    address,
                    roles,
                    birthDate,
                    user.getPhone1(),
                    user.getPhone2(),
                    user.getAge() != null ? Integer.parseInt(user.getAge()) : null,
                    user.getLength() != null ? Double.parseDouble(user.getLength()) : null,
                    user.getWeigth() != null ? Double.parseDouble(user.getWeigth()) : null,
                    user.getLastName(),
                    user.getImc() != null ? Double.parseDouble(user.getImc()) : null,
                    null,
                    new Date(),
                    null
            );
        }
        return newUSer;
    }

}
