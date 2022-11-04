package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.services.PetService;
import org.springframework.stereotype.Service;

@Service
public class PetServiceMap extends AbstractServiceMap<Pet, Long> implements PetService {
    @Override
    Long getNextId() {
        return (long) (map.size() + 1);
    }
}
