package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController controller;

    List<Owner> owners;
    final Long id1 = 1L;
    final Long id2 = 2L;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        owners = new LinkedList<>();
        owners.add(Owner.builder().id(id1).build());
        owners.add(Owner.builder().id(id2).build());

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void displayOwnerDetails() throws Exception {
        Owner owner = Owner.builder().id(id1).build();

        when(ownerService.findById(anyLong())).thenReturn(owner);

        mockMvc.perform(get("/owners/" + id1))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andExpect(model().attribute("owner", hasProperty("id", is(id1))));
    }


    @Test
    void getFindForm() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/findOwners"))
                .andExpect(status().isOk());

        verifyZeroInteractions(ownerService);
    }

    @Test
    void processFindFormManyOwners() throws Exception {

        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(owners);

        mockMvc.perform(get("/owners"))
                .andExpect(model().attributeExists("selections"))
                .andExpect(view().name("owners/ownersList"))
                .andExpect(status().isOk());

        verify(ownerService, never()).findAll();
    }

    @Test
    void processFindFormOneOwner() throws Exception {

        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(Arrays.asList(Owner.builder().id(id1).build()));

        mockMvc.perform(get("/owners"))
                .andExpect(view().name("redirect:/owners/"+ id1))
                .andExpect(status().is3xxRedirection());

        verify(ownerService, never()).findAll();
        verify(ownerService, times(1)).findAllByLastNameLike(anyString());
    }

    @Test
    void processFindFormNoOwners() throws Exception {
        when(ownerService.findAllByLastNameLike(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/owners"))
                .andExpect(view().name("owners/findOwners"))
                .andExpect(status().isOk());

        verify(ownerService, never()).findAll();
        verify(ownerService, times(1)).findAllByLastNameLike(anyString());
    }
}