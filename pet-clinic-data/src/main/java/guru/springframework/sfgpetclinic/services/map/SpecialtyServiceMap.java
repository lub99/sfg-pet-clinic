package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Specialty;
import guru.springframework.sfgpetclinic.services.SpecialtyService;
import org.springframework.stereotype.Service;

@Service
public class SpecialtyServiceMap extends AbstractServiceMap<Specialty, Long> implements SpecialtyService {
    @Override
    Long getNextId() {
        return (long) (map.size() + 1);
    }
}
