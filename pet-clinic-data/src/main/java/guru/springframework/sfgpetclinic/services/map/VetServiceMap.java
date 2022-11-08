package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Specialty;
import guru.springframework.sfgpetclinic.model.Vet;
import guru.springframework.sfgpetclinic.services.SpecialtyService;
import guru.springframework.sfgpetclinic.services.VetService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class VetServiceMap extends AbstractServiceMap<Vet, Long> implements VetService {
    private final SpecialtyService specialtyService;

    public VetServiceMap(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @Override
    Long getNextId() {
        return (long) (map.size() + 1);
    }

    @Override
    public Vet save(Vet object) {
        if (Objects.nonNull(object)) {
            if (object.getSpecialties().size() > 0) {
                object.getSpecialties().forEach(
                        specialty -> {
                            if (Objects.isNull(specialty.getId())){
                                Specialty savedSpeciality = specialtyService.save(specialty);
                                specialty.setId(savedSpeciality.getId());
                            }
                        }
                );
            }
        } else {
            return null;
        }

        return super.save(object);
    }
}
