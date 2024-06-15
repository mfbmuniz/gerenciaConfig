package gerenciaconfigsrc.service;

import gerenciaconfigsrc.models.States;
import javassist.NotFoundException;

public interface StateService {

    States findByUf(String uf) throws NotFoundException;
}
