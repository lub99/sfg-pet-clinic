package guru.springframework.sfgpetclinic.services.jpa;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.repositories.PetTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceJpaTest {

    @Mock
    OwnerRepository ownerRepository;

    @InjectMocks
    OwnerServiceJpa service;

    final String lastname = "Smith";
    final Long id = 1L;
    Owner returnOwner;

    @BeforeEach
    void setUp() {
        returnOwner = Owner.builder().id(id).lastName(lastname).build();
    }

    @Test
    void findByLastNameFound() {
        //given
        when(ownerRepository.findByLastName(any())).thenReturn(Optional.of(returnOwner));

        //when
        Owner foundOwner = service.findByLastName("Smith");

        //then
        assertNotNull(foundOwner);
        assertEquals(lastname, foundOwner.getLastName());
    }

    @Test
    void findAll() {
        Set<Owner> mockOwnerSet = new HashSet<>();
        mockOwnerSet.add(Owner.builder().id(1L).build());
        mockOwnerSet.add(Owner.builder().id(2L).build());

        when(ownerRepository.findAll()).thenReturn(mockOwnerSet);

        Set<Owner> foundOwners = service.findAll();

        assertNotNull(foundOwners);
        assertEquals(2, foundOwners.size());
    }

    @Test
    void findById() {
        when(ownerRepository.findById(any())).thenReturn(Optional.of(returnOwner));

        Owner foundOwner = service.findById(id);

        assertNotNull(foundOwner);
        assertEquals(id, foundOwner.getId());
    }

    @Test
    void findByIdNotFound() {
        when(ownerRepository.findById(any())).thenReturn(Optional.empty());

        Owner foundOwner = service.findById(id);

        assertNull(foundOwner);
    }

    @Test
    void saveOwnerWithId() {
        when(ownerRepository.save(any())).thenReturn(returnOwner);

        Owner savedOwner = service.save(returnOwner);

        assertNotNull(savedOwner);
        assertEquals(id, savedOwner.getId());
    }

    @Test
    void delete() {
        service.delete(returnOwner);

        verify(ownerRepository, times(1)).delete(any());
    }

    @Test
    void deleteById() {
        service.deleteById(id);

        verify(ownerRepository, times(1)).deleteById(anyLong());
    }
}