package gerenciaconfigsrc.repository;

import gerenciaconfigsrc.models.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitiesRepository extends JpaRepository<Cities,Long> {

    Optional<Cities> findByCity(String city);
}
