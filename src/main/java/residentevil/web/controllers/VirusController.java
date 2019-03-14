package residentevil.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import residentevil.domain.models.binding.VirusAddBindingModel;
import residentevil.domain.models.service.VirusServiceModel;
import residentevil.domain.models.view.CapitalListViewModel;
import residentevil.domain.models.view.VirusListViewModel;
import residentevil.domain.models.view.VirusViewModel;
import residentevil.service.CapitalService;
import residentevil.service.VirusService;

import javax.validation.Valid;
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
                            @ModelAttribute(name = "bindingModel") VirusAddBindingModel bindingModel) {
        modelAndView.addObject("bindingModel", bindingModel);
        modelAndView.addObject("capitals", this.capitalService.findAllCapitals()
                .stream()
                .map(c -> this.modelMapper.map(c, CapitalListViewModel.class))
                .collect(Collectors.toList()));

        return super.view("add-virus", modelAndView);
    }

    @PostMapping("/add")
    public ModelAndView addConfirm(@Valid @ModelAttribute(name = "bindingModel") VirusAddBindingModel bindingModel,
                                   BindingResult bindingResult, ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("bindingModel", bindingModel);

            return super.view("add-virus", modelAndView);
        }

        VirusServiceModel virusServiceModel = this.modelMapper.map(bindingModel, VirusServiceModel.class);
        this.virusService.addVirus(virusServiceModel);

        if (virusServiceModel == null) {
            throw new IllegalArgumentException("Virus creation failed!");
        }

        return super.redirect("/");
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

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable(name = "id") String id,
                               @ModelAttribute(name = "viewModel") VirusListViewModel viewModel,
                               ModelAndView modelAndView) {

        VirusServiceModel virusServiceModel =
                this.virusService.findVirusById(id);

        if (virusServiceModel == null) {
            throw new IllegalArgumentException("Document not found!");
        }

        modelAndView.addObject("viewModel", this.modelMapper
                .map(virusServiceModel, VirusViewModel.class));

        return super.view("delete", modelAndView);
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteConfirm(@PathVariable(name = "id") String id,
                                      @ModelAttribute(name = "viewModel") VirusListViewModel viewModel,
                                      ModelAndView modelAndView) {

        if(!this.virusService.deleteVirus(id)){
            throw new IllegalArgumentException("Something went wrong!");
        }

        return super.redirect("/");
    }


}
