package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OwnerServiceMapTest {

    @Mock
    PetService petService;
    @Mock
    PetTypeService petTypeService;

    OwnerServiceMap ownerServiceMap;

    final Long ownerId = 1L;
    final String lastname = "Smith";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ownerServiceMap =  new OwnerServiceMap(petService, petTypeService);

        ownerServiceMap.save(Owner.builder().id(ownerId).lastName(lastname).build());
    }

    @Test
    void findAll() {
        Set<Owner> owners = ownerServiceMap.findAll();

        assertEquals(1, owners.size());
    }

    @Test
    void findById() {
        Owner owner = ownerServiceMap.findById(ownerId);

        assertEquals(ownerId, owner.getId());
    }

//    @DirtiesContext
    @Test
    void saveExistingId() {
        Long id2 = 2L;
        Owner owner2 = Owner.builder().id(id2).build();
        Owner savedOwner = ownerServiceMap.save(owner2);

        assertEquals(id2, savedOwner.getId());
    }

    /**
     * Owner without id must set id to new user
     * */
    @Test
    void saveNoId() {
        Owner owner2 = Owner.builder().lastName("last_name").build();
        Owner savedOwner = ownerServiceMap.save(owner2);

        assertNotNull(savedOwner);
        assertNotNull(savedOwner.getId());
    }

    @Test
    void delete() {
        ownerServiceMap.delete(ownerServiceMap.findById(ownerId));

        assertEquals(0, ownerServiceMap.findAll().size());
    }

    @Test
    void deleteById() {
        ownerServiceMap.deleteById(ownerId);

        assertEquals(0, ownerServiceMap.findAll().size());
    }

    @Test
    void findByLastNameNotFound() {
        Owner foundOwner = ownerServiceMap.findByLastName("bz");

        assertNull(foundOwner);
    }
    @Test
    void findByLastNameFound() {
        Owner foundOwner = ownerServiceMap.findByLastName(lastname);

        assertNotNull(foundOwner);
        assertEquals(lastname, foundOwner.getLastName());
        assertEquals(ownerId, foundOwner.getId());
    }
}