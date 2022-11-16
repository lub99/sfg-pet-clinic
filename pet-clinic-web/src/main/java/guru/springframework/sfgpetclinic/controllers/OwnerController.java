package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/owners")
@Controller
public class OwnerController {

    public static final String VIEWS_OWNERS_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/{ownerId}")
    public ModelAndView showOwnerDetails(@PathVariable long ownerId){
        ModelAndView mav = new ModelAndView("owners/ownerDetails");
        mav.addObject(ownerService.findById(ownerId));
        return mav;
    }

    @GetMapping("/find")
    public String getFindForm(Model model){
        model.addAttribute("owner", new Owner());
        return "owners/findOwners";
    }

    @GetMapping
    public String processFindForm(Owner owner, BindingResult result, Model model){
        // allow parameterless GET request for /owners to return all records
        if (owner.getLastName() == null) {
            owner.setLastName(""); // empty string signifies the broadest possible search
        }

        // find owners by last name
        List<Owner> results = ownerService.findAllByLastNameLike("%"+ owner.getLastName() + "%");

        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("lastName", "notFound", "not found");
            return "owners/findOwners";
        } else if (results.size() == 1) {
            // 1 owner found
            owner = results.get(0);
            return "redirect:/owners/" + owner.getId();
        } else {
            // multiple owners found
            model.addAttribute("selections", results);
            return "owners/ownersList";
        }
    }

    @GetMapping("/new")
    public String getCreateOwnerForm(Model model){
        model.addAttribute("owner", new Owner());
        return VIEWS_OWNERS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/new")
    public String createOwner(@Valid Owner toSaveOwner, BindingResult result){
        if (result.hasErrors()){
            return VIEWS_OWNERS_CREATE_OR_UPDATE_FORM;
        }
        Owner savedOwner = ownerService.save(toSaveOwner);
        return "redirect:/owners/" + savedOwner.getId();
    }

    @GetMapping("{ownerId}/edit")
    public String getUpdateOwnerForm(@PathVariable String ownerId, Model model){
        model.addAttribute("owner", ownerService.findById(Long.valueOf(ownerId)));
        return VIEWS_OWNERS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("{ownerId}/edit")
    public String updateOwner(@PathVariable String ownerId,  @Valid Owner toSaveOwner, BindingResult result){
        if (result.hasErrors()){
            return VIEWS_OWNERS_CREATE_OR_UPDATE_FORM;
        }

        toSaveOwner.setId(Long.valueOf(ownerId));
        Owner savedOwner = ownerService.save(toSaveOwner);
        return "redirect:/owners/" + savedOwner.getId();
    }

}
