package gerenciaconfigsrc.repository;

import gerenciaconfigsrc.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long > {
    User findOneByEmailAndDeletedAtIsNull(String email);
    User findOneByIdUser(Long idUser);
    Page<User> findAllByDeletedAtIsNullOrderByName(Pageable page);
    Page<User> findAllByNameIgnoreCaseAndDeletedAtIsNull(Pageable page, String name);

    // pega todos que nao seja o admin
    @Query(value = "select * from users u " +
            " inner join user_roles ur " +
            "  on u.user_id = ur.user_id " +
            " inner join roles r " +
            "  on r.roles_id = ur.role_id " +
            "where extract(day from u.due_date) = extract(day from now()) and r.\"name\" = 'ALUNO'",
    nativeQuery = true)
    List<User> findAllByCurrentDay();
}
