package gerenciaconfigsrc.repository;

import gerenciaconfigsrc.models.States;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatesRepository extends JpaRepository<States,Long> {

    Optional<States> findByUf(String cidade);
}
