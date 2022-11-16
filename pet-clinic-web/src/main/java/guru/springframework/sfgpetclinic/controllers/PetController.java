package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@RequestMapping("/owners/{ownerId}")
@Controller
public class PetController {
    public static final String PETS_VIEWS_CREATE_OR_UPDATE_PET_FORM = "pets/createOrUpdatePetForm";
    private final PetTypeService petTypeService;
    private final OwnerService ownerService;

    private final PetService petService;

    public PetController(PetTypeService petTypeService, OwnerService ownerService, PetService petService) {
        this.petTypeService = petTypeService;
        this.ownerService = ownerService;
        this.petService = petService;
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") Long ownerId) {
        return ownerService.findById(ownerId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }


    @GetMapping("/pets/new")
    public String initCreationForm(Owner owner, Model model) {
        Pet pet = new Pet();
        owner.addPet(pet);
        model.addAttribute("pet", pet);
        return PETS_VIEWS_CREATE_OR_UPDATE_PET_FORM;
    }

    @PostMapping("/pets/new")
    public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, Model model) {
        if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
            result.rejectValue("name", "duplicate", "already exists");
        }
        owner.addPet(pet);
        if (result.hasErrors()) {
            model.addAttribute("pet", pet);
            return PETS_VIEWS_CREATE_OR_UPDATE_PET_FORM;
        } else {
            petService.save(pet);

            return "redirect:/owners/" + owner.getId();
        }
    }

    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(@PathVariable Long petId, Model model) {
        model.addAttribute("pet", petService.findById(petId));
        return PETS_VIEWS_CREATE_OR_UPDATE_PET_FORM;
    }

    @PostMapping("/pets/{petId}/edit")
    public String processUpdateForm(Owner owner, @Valid Pet pet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            pet.setOwner(owner);
            model.addAttribute("pet", pet);
            return PETS_VIEWS_CREATE_OR_UPDATE_PET_FORM;
        } else {
            owner.addPet(pet);
            petService.save(pet);
            return "redirect:/owners/" + owner.getId();
        }
    }

    /**
     *
     *
     * This method is different implemented than official.
     * In official implementation @param pet has owner field null and because of this set owner for pet is needed.
     * Here in my implementation, I am storing owner inside update form and getting owner from database is not needed.
     * Added
     *      <input type="hidden" name="owner" th:field="*{owner}" />
     * in
     *      createOrUpdatePetForm.html, line 12
     *
     *
     *
     * Because of  @ModelAttribute("owner"), owner is fetched before call of this method.
     * I have done this for my learning purposes.
     * */
//    @PostMapping("/pets/{petId}/edit")
//    public String processUpdateForm( @Valid Pet pet, BindingResult result, Model model) {
//        log.debug("*************ENTER processUpdateForm ");
//        if (result.hasErrors()) {
//            pet.setOwner(pet.getOwner());
//            model.addAttribute("pet", pet);
//            return PETS_VIEWS_CREATE_OR_UPDATE_PET_FORM;
//        } else {
//            log.debug("*************BEFORE SAVE ");
//            petService.save(pet);
//            log.debug("*************AFTER SAVE ");
//            return "redirect:/owners/" + pet.getOwner().getId();
//        }
//    }

}
