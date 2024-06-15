package gerenciaconfigsrc.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gerenciaconfigsrc.models.RequestEntity.UserRequest;
import gerenciaconfigsrc.models.User;
import gerenciaconfigsrc.models.dto.PasswordDto;
import gerenciaconfigsrc.models.dto.UserDto;
import gerenciaconfigsrc.service.RoleService;
import gerenciaconfigsrc.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javassist.NotFoundException;

@RestController
@CrossOrigin
@RequestMapping("/pacientes")
@SecurityRequirement(name = "bearerAuth")
public class PacientesController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @PostMapping(path = "/create")
    @Operation(description = "Criar novo usuário, com qualquer role solicitada")
    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    public ResponseEntity<UserDto> createUser(
            @Parameter(description = "Json da requisição que contem o dado do usuario a ser salvo")
            @Valid @RequestBody UserRequest request) throws NotFoundException {

        UserDto userDto = this.userService.create(request);

        return ResponseEntity.ok().body(
                userDto
        );
    }

    @PostMapping(path = "/commonCreate")
    @Operation(description = "Criar novo usuário sem necessidade de login, porem somente com a role Usuario")
    public ResponseEntity<UserDto> commonCreateUser(
            @Parameter(description = "Json da requisição que contem o dado do usuario a ser salvo")
            @Valid @RequestBody UserRequest request) throws NotFoundException {

        if(request.getRoles() != null) {
            request.getRoles().clear();
            request.getRoles().add("USER");
        }

        UserDto userDto = this.userService.commonCreate(request);

        return ResponseEntity.ok().body(
                userDto
        );
    }


    @PostMapping(path = "/edit")
    @Operation(description = "Editar usuário existente")
    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    public ResponseEntity<UserDto> editUser(
            @Parameter(description = "Json da requisição que contem o dado a ser editado")
            @Valid @RequestBody UserRequest request) throws NotFoundException {

        return ResponseEntity.ok().body(
                this.userService.editUser(request)
        );
    }

    @DeleteMapping(path = "/delete/{email}")
    @Operation(description = "Desativa usuário existente")
    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    public ResponseEntity<UserDto> deleteUser(@PathVariable(value="email") final String email){
        return ResponseEntity.ok().body(
                this.userService.deleteUser(email)
        );
    }

    @DeleteMapping(path = "/delete")
    @Operation(description = "Desativa usuário existente")
    public ResponseEntity<UserDto> deleteLoggedUser(){
        return ResponseEntity.ok().body(
                this.userService.deleteLoggedUser()
        );
    }


    @GetMapping(path = "/page/{page}/size/{size}")
    @ResponseBody
    @Operation(description = "Lista usuários por página quantidade")
    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    public Page<User> listUsersByPageWithSize(
            @Parameter(description = "Página que deseja visualizar iniciando em 0", example = "0")
            @PathVariable(value="page")
            int page,
            @Parameter(description = "Quantidade de usuários a serem listados por página", example = "10")
            @PathVariable(value="size")
            int size){

        Pageable pages = PageRequest.of(page, size);
        return this.userService.listUsersByPage(pages);

    }

    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    @GetMapping(path = "page/{page}/size/{size}/name/{name}")
    @ResponseBody
    @Operation(description = "Lista usuários por página quantidade")
    public Page<User> listUserByNameAndPageWithSize(
            @Parameter(description = "Página que deseja visualizar iniciando em 0", example = "0")
            @PathVariable(value="page")
            int page,
            @Parameter(description = "Quantidade de usuários a serem listados por página", example = "10")
            @PathVariable(value="size")
            int size,
            @PathVariable(value="name")
            String name
    ){

        Pageable pages = PageRequest.of(page, size);

        return this.userService.listUsersByPageAndName(pages, name);

    }

    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    @GetMapping(path = "getuserbyid/userId/{userId}")
    @ResponseBody
    @Operation(description = "Lista usuários por página quantidade")
    public ResponseEntity<User> getUserById(
            @PathVariable(value="userId")
            Long userId)throws NotFoundException{

        return ResponseEntity.ok().body(
                this.userService.getUserById(userId)
        );
    }

    @PostMapping(path = "/resetPassword")
    @Operation(description = "Reseta senha do usuario")
    public ResponseEntity<String> resetPassword(
        @Parameter(description = "DTO com senhas novas do usuario e token")
        @RequestBody PasswordDto userPassword) {

        return ResponseEntity.ok().body(this.userService.resetPassword(userPassword));
    }

    @GetMapping(path = "/getImc/userId/{userId}")
    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    @Operation(description = "Retorna o IMC por id do usuario")
    public ResponseEntity<String> getIMC(
            @Parameter(description = "Id do usuario")
            @PathVariable(value="userId") Long userId) {

        return ResponseEntity.ok().body(this.userService.getIMC(userId));
    }

    @GetMapping(path = "/getPesoIdeal/userId/{userId}")
    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    @Operation(description = "Retorna o IMC por id do usuario")
    public ResponseEntity<String> getPesoIdeal(
            @Parameter(description = "Id do usuario")
            @PathVariable(value="userId") Long userId) {

        return ResponseEntity.ok().body(this.userService.getPesoIdeal(userId));
    }

    @GetMapping(path = "/getCpfOfuscado/userId/{userId}")
    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    @Operation(description = "Retorna o IMC por id do usuario")
    public ResponseEntity<String> getCpfOfuscado(
            @Parameter(description = "Id do usuario")
            @PathVariable(value="userId") Long userId) {

        return ResponseEntity.ok().body(this.userService.getCpfOfuscado(userId));
    }

    @GetMapping(path = "/getSituacaoImc/userId/{userId}")
    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    @Operation(description = "Retorna o IMC por id do usuario")
    public ResponseEntity<String> getSituacaoImc(
            @Parameter(description = "Id do usuario")
            @PathVariable(value="userId") Long userId) {

        return ResponseEntity.ok().body(this.userService.getSituacaoImc(userId));
    }

    @GetMapping(path = "/getCpfValido/userId/{userId}")
    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    @Operation(description = "Retorna o IMC por id do usuario")
    public ResponseEntity<String> getCpfValido(
            @Parameter(description = "Id do usuario")
            @PathVariable(value="userId") Long userId) {

        return ResponseEntity.ok().body(this.userService.getCpfValido(userId));
    }

    @GetMapping(path = "/getCalculaIdade/userId/{userId}")
    @PreAuthorize("@authorityChecker.isAllowed({'ADMIN','USER','CLIN','DEV'})")
    @Operation(description = "Retorna o IMC por id do usuario")
    public ResponseEntity<String> getCalculaIdade(
            @Parameter(description = "Id do usuario")
            @PathVariable(value="userId") Long userId) {

        return ResponseEntity.ok().body(this.userService.getCalculaIdade(userId));
    }

}
