package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.VisitService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
@Profile({"default", "map"})
public class VisitServiceMap extends AbstractServiceMap<Visit, Long> implements VisitService {
    @Override
    Long getNextId() {
        return (long) (map.size() + 1);
    }

    @Override
    public Visit save(Visit visit) {
        if (Objects.isNull(visit)
                || Objects.isNull(visit.getPet())
                || Objects.isNull(visit.getPet().getId())
                || Objects.isNull(visit.getPet().getOwner())
                || Objects.isNull(visit.getPet().getOwner().getId())
        ){
            throw new RuntimeException("Invalid Visit!");
        }
        return super.save(visit);
    }
}
