package gerenciaconfigsrc.repository;

import gerenciaconfigsrc.models.CountryCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface CountryCodeRepository extends JpaRepository<CountryCode,Long > {



    CountryCode findOneByCountryCodeId(Long idCountryCode);
    Page<CountryCode> findAll(Pageable page);


}
