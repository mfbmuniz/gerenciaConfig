package gerenciaconfigsrc.service.impl;

import gerenciaconfigsrc.models.States;
import gerenciaconfigsrc.repository.StatesRepository;
import gerenciaconfigsrc.service.StateService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateServiceImpl implements StateService {

    @Autowired
    private StatesRepository statesRepository;

    @Override
    public States findByUf(String uf) throws NotFoundException {
        return this.statesRepository.findByUf(uf)
                .orElseThrow(() -> new NotFoundException("Estado nao encontrado para a UF "+uf));
    }
}
