package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.PetTypeService;

public class PetTypeServiceMap extends AbstractServiceMap<PetType, Integer> implements PetTypeService {
    @Override
    Integer getNextId() {
        return map.size() + 1;
    }
}
