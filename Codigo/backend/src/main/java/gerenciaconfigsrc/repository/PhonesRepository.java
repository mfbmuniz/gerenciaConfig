package gerenciaconfigsrc.repository;

import gerenciaconfigsrc.models.Phones;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhonesRepository extends JpaRepository<Phones,Long > {

    Phones findOneByIdPhonesAndDeletedAtIsNull(Long idPhone);
    Page<Phones> findAllByDeletedAtIsNullOrderByIdUser(Pageable page);
    Page<Phones> findAllByIdUserAndDeletedAtIsNull(Pageable page, Long idUser);


}
