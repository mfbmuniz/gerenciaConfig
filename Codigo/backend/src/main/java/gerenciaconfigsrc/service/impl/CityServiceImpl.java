package gerenciaconfigsrc.service.impl;

import gerenciaconfigsrc.models.Cities;
import gerenciaconfigsrc.repository.CitiesRepository;
import gerenciaconfigsrc.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CitiesRepository citiesRepository;

    @Override
    public Cities findByCity(String city) {
        return this.citiesRepository.findByCity(city)
                .orElseGet(() -> this.citiesRepository.save(new Cities(city)));
    }
}
