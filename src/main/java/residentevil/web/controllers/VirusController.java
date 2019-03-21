package residentevil.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import residentevil.domain.entities.Magnitude;
import residentevil.domain.entities.Mutation;
import residentevil.domain.models.binding.VirusBindingModel;
import residentevil.domain.models.service.CapitalServiceModel;
import residentevil.domain.models.service.VirusServiceModel;
import residentevil.domain.models.view.CapitalListViewModel;
import residentevil.domain.models.view.VirusListViewModel;
import residentevil.service.CapitalService;
import residentevil.service.VirusService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/viruses")
public class VirusController extends BaseController {

    private final CapitalService capitalService;
    private final VirusService virusService;
    private final ModelMapper modelMapper;

    @Autowired
    public VirusController(CapitalService capitalService, VirusService virusService, ModelMapper modelMapper) {
        this.capitalService = capitalService;
        this.virusService = virusService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public ModelAndView add(ModelAndView modelAndView,
                            @ModelAttribute(name = "virusBindingModel") VirusBindingModel bindingModel) {
        modelAndView.addObject("virusBindingModel", bindingModel);

        this.addObjectsInModelAndView(modelAndView);

        modelAndView.addObject("capitals", this.capitalService.findAllCapitals()
                .stream()
                .map(c -> this.modelMapper.map(c, CapitalListViewModel.class))
                .collect(Collectors.toList()));


        return super.view("add-virus", modelAndView);
    }

    @PostMapping("/add")
    public ModelAndView addConfirm(@Valid @ModelAttribute(name = "bindingModel") VirusBindingModel bindingModel,
                                   BindingResult bindingResult, ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("virusBindingModel", bindingModel);

            return super.view("add-virus", modelAndView);
        }

        VirusServiceModel virusServiceModel = this.modelMapper.map(bindingModel, VirusServiceModel.class);
        this.virusService.addVirus(virusServiceModel);

        if (virusServiceModel == null) {
            throw new IllegalArgumentException("Virus creation failed!");
        }

        return super.redirect("/show");
    }

    @GetMapping("/show")
    public ModelAndView show(ModelAndView modelAndView,
                             @ModelAttribute(name = "viewModel") VirusListViewModel viewModel) {
        modelAndView.addObject("viewModel", viewModel);

        modelAndView.addObject("viruses", this.virusService.findAllViruses()
                .stream()
                .map(v -> this.modelMapper.map(v, VirusListViewModel.class))
                .collect(Collectors.toList()));

        return super.view("viruses", modelAndView);
    }

    @GetMapping(value = "/edit/{id}")
    public ModelAndView edit(@PathVariable(name = "id") String id ,ModelAndView modelAndView) {

        VirusBindingModel virus = this.virusService.extractVirusByIdForEditOrDelete(id);
        modelAndView.addObject("virusBindingModel", virus);

        this.addObjectsInModelAndView(modelAndView);

        return super.view("edit-virus", modelAndView);
    }


    @PostMapping(value = "/edit/{id}")
    public ModelAndView editConfirm(@PathVariable(name = "id") String id ,ModelAndView modelAndView,  BindingResult bindingResult,
                                    @Valid @ModelAttribute("virusBindingModel") VirusBindingModel virusBindingModel) {

        if (bindingResult.hasErrors()) {
            this.addObjectsInModelAndView(modelAndView);

            return super.view("viruses/edit-virus", modelAndView);
        }

        this.virusService.editVirus(virusBindingModel);

        return super.redirect("/home");
    }


    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable(name = "id") String id, ModelAndView modelAndView) {

        VirusBindingModel virus = this.virusService.extractVirusByIdForEditOrDelete(id);
        modelAndView.addObject("virusBindingModel", virus);

        this.addObjectsInModelAndView(modelAndView);

        return super.view("delete-virus", modelAndView);
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteConfirm(@PathVariable(name = "id") String id) {

        if(!this.virusService.deleteVirus(id)){
            throw new IllegalArgumentException("Something went wrong!");
        }


        return super.redirect("/home");
    }

    private void addObjectsInModelAndView(ModelAndView modelAndView) {
        modelAndView.addObject("mutations", Mutation.values());
        modelAndView.addObject("magnitudes", Magnitude.values());
    }
}
