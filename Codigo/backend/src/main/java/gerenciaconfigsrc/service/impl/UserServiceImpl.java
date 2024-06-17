package gerenciaconfigsrc.service.impl;

import java.util.*;

import javax.persistence.NonUniqueResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gerenciaconfigsrc.enums.RolesEnum;
import gerenciaconfigsrc.models.Address;
import gerenciaconfigsrc.models.Cities;
import gerenciaconfigsrc.models.PasswordReset;
import gerenciaconfigsrc.models.RequestEntity.UserRequest;
import gerenciaconfigsrc.models.Role;
import gerenciaconfigsrc.models.States;
import gerenciaconfigsrc.models.User;
import gerenciaconfigsrc.models.dto.AddressDto;
import gerenciaconfigsrc.models.dto.PasswordDto;
import gerenciaconfigsrc.models.dto.UserDto;
import gerenciaconfigsrc.repository.AddressRepository;
import gerenciaconfigsrc.repository.PasswordResetServiceRepository;
import gerenciaconfigsrc.repository.UserRepository;
import gerenciaconfigsrc.service.CityService;
import gerenciaconfigsrc.service.RoleService;
import gerenciaconfigsrc.service.StateService;
import gerenciaconfigsrc.service.UserService;
import javassist.NotFoundException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private CityService cityService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StateService stateService;

    @Autowired
    private PasswordResetServiceRepository passwordResetServiceRepository;


    @Override
    public UserDto create(UserRequest user) throws NonUniqueResultException, NotFoundException {

        Optional<User> usr = Optional.ofNullable(this.userRepository.findOneByEmailAndDeletedAtIsNull(user.getEmail()));

        if(!   usr.isPresent()){

            List<Role> roles = Optional.of(this.roleService.findAllByNameIn(user.getRoles()))
                    .orElseThrow(() -> new NoSuchElementException("Role not Founded"));

            user.setPassword(this.bcryptEncoder.encode(user.getPassword()));

            Cities city = this.cityService.findByCity(user.getAddress().getCity());
            States state = this.stateService.findByUf(user.getAddress().getState());

            AddressDto addressDto = user.getAddress();

            Address address = this.addressRepository.save(Address.fromAddressDTO(addressDto, city, state));

            User savedUser = this.userRepository.save(UserRequest.toUser(user, roles, address));

            /*if(roles.stream().anyMatch(f -> f.getName().equals(RolesEnum.USER.getCode()))){
                this.monthlyPaymentService.create(savedUser);
            }*/
            return UserDto.fromUser(savedUser);
        }else{
            throw new NonUniqueResultException("Email ja cadastrado!");
        }

    }

    @Override
    public Optional<User> verifyUser(String email) {
        return  Optional.ofNullable(this.userRepository.findOneByEmailAndDeletedAtIsNull(email));
    }


    @Override
    public String resetPassword(PasswordDto passwordDto)
        throws IllegalArgumentException {
        if(!passwordDto.getPassword().equals(passwordDto.getConfirmPassword())){
            throw new IllegalArgumentException("Senhas devem ser iguais");
        }

        PasswordReset userResetToken = Optional.ofNullable(
            passwordResetServiceRepository.findByTokenAndDeletedAtIsNull(passwordDto.getToken())
            ).orElseThrow(() -> new IllegalArgumentException("Token Inválido"));

        //se token tiver expirado
        if(userResetToken.getExpirieAt().getTime() <= new Date().getTime()){
          throw new IllegalArgumentException("Token inválido");
        }

        //salva nova senha
        User user = userResetToken.getUser();
        user.setPassword(passwordDto.getPassword());
        this.save(user);

        //deleta token utilizado
        userResetToken.setDeletedAt(new Date());
        passwordResetServiceRepository.save(userResetToken);

        return "Senha resetada";

    }
    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(this.userRepository.findOneByEmailAndDeletedAtIsNull(email));
    }

    @Override
    public UserDto save(User user) {
        user.setPassword(this.bcryptEncoder.encode(user.getPassword()));
        return UserDto.fromUser(this.userRepository.save(user));
    }

    @Override
    public UserDto editUser(UserRequest userRequest) throws NotFoundException {

        User user = this.userRepository.findOneByEmailAndDeletedAtIsNull(userRequest.getEmail());
        if (user == null){
            user = this.userRepository.findOneByIdUser(userRequest.getIdUser());
        }


        //usuarios nao administradores nao podem editar Roles
        //usuarios nao administradores nao podem adicionar Roles
        User userByPrincipal = this.getUserByPrincipal();

        if(this.hasRole(userByPrincipal, RolesEnum.ADMIN)) {
            List<Role> newRoles = this.roleService.findAllByNameIn(userRequest.getRoles());

            Cities city = this.cityService.findByCity(userRequest.getAddress().getCity());
            States state = this.stateService.findByUf(userRequest.getAddress().getState());

            //if (userRequest.getAddress().getAddressId() > 0) this.addressRepository.deleteById(userRequest.getAddress().getAddressId());

            AddressDto addressDto = userRequest.getAddress();
            Address newAddress = this.addressRepository.save(Address.fromAddressDTO(addressDto, city, state));

            userRequest.setIdUser(user.getIdUser());

            User userEditado = UserRequest.toUser(userRequest,newRoles,newAddress);

            userEditado.setPassword(this.bcryptEncoder.encode(userRequest.getPassword()));
            userEditado.setIdUser(userRequest.getIdUser());

            user=userEditado;

        }else if(user == userByPrincipal) {
            //Usuarios so podem editar eles mesmos
            user.setName(userRequest.getName());
            user.setPassword(this.bcryptEncoder.encode(user.getPassword()));
        }else {
            throw new BadCredentialsException("Usuários não administradores não podem alterar outros usuários!");
        }

        return UserDto.fromUser(this.userRepository.save(user));
    }

    @Override
    public boolean hasRole(User user, RolesEnum checkRole) {

        return user.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals(checkRole.getCode()));
    }

    @Override
    public UserDto deleteUser(String email) {
        User user = this.userRepository.findOneByEmailAndDeletedAtIsNull(email);

        user.setDeletedAt(new Date());
        return UserDto.fromUser(this.userRepository.save(user));
    }

    @Override
    public UserDto deleteLoggedUser() {
        User loggedUser = this.getUserByPrincipal();
        loggedUser.setDeletedAt(new Date());
        User savedUser = this.userRepository.save(loggedUser);
        return UserDto.fromUser(savedUser);
    }


    @Override
    public User getUserByPrincipal() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!principal.equals("anonymousUser")) {
                return this.userRepository.findOneByEmailAndDeletedAtIsNull(((UserDetails) principal).getUsername());
            } else {
                return null;
            }
        }catch (Exception e){return null;}

    }
    @Override
    public Page<User> listUsersByPage(Pageable page) {
        return this.userRepository.findAllByDeletedAtIsNullOrderByName(page);
    }

    @Override
    public Page<User> listUsersByPageAndName(Pageable page, String name) {
        return this.userRepository.findAllByNameIgnoreCaseAndDeletedAtIsNull(page, name);
    }

    @Override
    public User getUserById(Long userId) throws NotFoundException {
        return this.userRepository.findOneByIdUser(userId);
    }

    @Override
    public UserDto commonCreate(UserRequest request) throws NotFoundException {
        return create(request);
    }


    @Override
    public String getIMC(Long userId) {
        User user = this.userRepository.findOneByIdUser(userId);
        return Double.toString(calcularIMC(user.getWeight(),user.getLength()));

    }

    @Override
    public String getPesoIdeal(Long userId) {
        User user = this.userRepository.findOneByIdUser(userId);
        Double resp;
        if(user.getSex().equals("M")){
            resp= ( (72.7*user.getLength())-58 );
        }else{
            resp= ( (62.1*user.getLength())- 44.7);
        }

        return Double.toString(resp);
    }

    @Override
    public String getCpfOfuscado(Long userId) {
        User user = this.userRepository.findOneByIdUser(userId);
        String resp="";
        for (int i = 0; i < user.getLegalDocument().length(); i++) {
            if(i<3 || i>5){
                resp+='*';
            }else{
                resp+=user.getLegalDocument().charAt(i);
            }
        }
        return resp;
    }

    @Override
    public String getSituacaoImc(Long userId) {
        String resp="";
        User user = this.userRepository.findOneByIdUser(userId);

        Double calculoIMC = calcularIMC(user.getWeight(),user.getLength());

        if(calculoIMC< 17){
            resp= "Muito abaixo do peso";
        }else if (calculoIMC>=17 && calculoIMC<=18.49) {
            resp= "Abaixo do peso";
        }else if (calculoIMC>=18.50 && calculoIMC<=24.99) {
            resp= "Peso normal";
        }else if (calculoIMC>=25 && calculoIMC<=29.99) {
            resp= "Acima do peso";
        }else if (calculoIMC>=30 && calculoIMC<=34.99) {
            resp= "Obesidade I";
        }else if (calculoIMC>=35 && calculoIMC<=39.99) {
            resp= "Obesidade II (severa)";
        }else if (calculoIMC>=40) {
            resp= "Obesidade III (mórbida)";
        }
        return resp;
    }

    @Override
    public String getCpfValido(Long userId) {
        User user = this.userRepository.findOneByIdUser(userId);
        return Boolean.toString(validarCPF(user.getLegalDocument()));
    }

    @Override
    public String getCalculaIdade(Long userId) {
        User user = this.userRepository.findOneByIdUser(userId);

        return calcularIdade(user.getBirthDate()).toString();
    }

    public Double calcularIMC(Double peso, Double altura){

        return ( peso / (Math.pow(altura,2))  ) ;

    }

    private Integer calcularIdade(Date birthDate){

        Calendar dateOfBirth = new GregorianCalendar();
        dateOfBirth.setTime(birthDate);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
        if (today.before(dateOfBirth)) {
            age--;
        }
        return age;
    }

    private Boolean validarCPF(String cpf){
        boolean resp = false;
        int[] cpfSplited = new int[11];
        for (int j = 0; j < cpfSplited.length ; j++) {
            cpfSplited[j]  = Integer.parseInt(""+cpf.charAt(j));
        }
        int v1=0,v2=0;

        for (int i = 0; i < cpfSplited.length-1; i++) {
            v1+= cpfSplited[i] * ( 9- (i%10) );
            v2+= cpfSplited[i] * ( 9 - ((i+1)%10) );
        }

        v1= (v1%11)%10;
        v2+= (v1*9);
        v2= (v2%11)%10;


        if (v1 != cpfSplited[9]) {
            resp= false;
        }else if( v2 != cpfSplited[10]){
            resp=false;
        }else{
            resp = true;
        }
        return resp;
    }

}
