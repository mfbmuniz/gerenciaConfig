package gerenciaconfigsrc.service.impl;

import gerenciaconfigsrc.repository.CitiesRepository;
import gerenciaconfigsrc.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private CitiesRepository citiesRepository;

}
