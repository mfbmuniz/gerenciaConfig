package gerenciaconfigsrc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gerenciaconfigsrc.models.PasswordReset;

@Repository
public interface PasswordResetServiceRepository extends JpaRepository<PasswordReset,Long> {

  PasswordReset findByTokenAndDeletedAtIsNull(String token);
}
