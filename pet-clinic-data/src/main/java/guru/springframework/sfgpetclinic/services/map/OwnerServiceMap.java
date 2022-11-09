package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Profile({"default", "map"})
public class OwnerServiceMap extends AbstractServiceMap<Owner, Long> implements OwnerService {
    private final PetService petService;
    private final PetTypeService petTypeService;

    public OwnerServiceMap(PetService petService, PetTypeService petTypeService) {
        this.petService = petService;
        this.petTypeService = petTypeService;
    }

    @Override
    public Owner findByLastName(String lastName) {
        return map.values()
                .stream()
                .filter(owner -> owner.getLastName().equals(lastName))
                .findFirst()
                .get();
    }

    @Override
    Long getNextId() {
        return (long) (map.size() + 1);
    }

    @Override
    public Owner save(Owner object) {
        if (Objects.nonNull(object)) {
            if (Objects.nonNull(object.getPets())) {
                object.getPets().forEach(
                        pet -> {
                            if (Objects.nonNull(pet.getPetType())) {
                                if (Objects.isNull(pet.getPetType().getId())) {
                                    //it is needed to save pet type to map
                                    pet.setPetType(petTypeService.save(pet.getPetType()));
                                }
                            } else {
                                throw new RuntimeException("Pet Type is required!");
                            }

                            //save pet if it isn't saved
                            if (Objects.isNull(pet.getId())){
                                Pet savedPet = petService.save(pet);
                                pet.setId(savedPet.getId());
                            }
                        });
            }

            return super.save(object);
        } else {
            return null;
        }
    }
}
