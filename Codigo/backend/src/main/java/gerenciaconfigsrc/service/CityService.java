package gerenciaconfigsrc.service;

import gerenciaconfigsrc.models.Cities;

public interface CityService {

    Cities findByCity(String city);
}
