package residentevil.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import residentevil.domain.entities.Capital;
import residentevil.domain.entities.Virus;
import residentevil.domain.models.binding.VirusBindingModel;
import residentevil.domain.models.service.VirusServiceModel;
import residentevil.repository.CapitalRepository;
import residentevil.repository.VirusRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VirusServiceImpl implements VirusService {

    private final VirusRepository virusRepository;
    private final CapitalRepository capitalRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public VirusServiceImpl(VirusRepository virusRepository, CapitalRepository capitalRepository, ModelMapper modelMapper) {
        this.virusRepository = virusRepository;
        this.capitalRepository = capitalRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public VirusServiceModel addVirus(VirusServiceModel virusServiceModel) {
        Virus virus = this.modelMapper.map(virusServiceModel, Virus.class);

        try{
            this.virusRepository.saveAndFlush(virus);

            return this.modelMapper.map(virus, VirusServiceModel.class);
        }catch (Exception e){
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public void editVirus(VirusBindingModel virusBindingModel) {
        Virus virus = this.modelMapper.map(virusBindingModel, Virus.class);

        List<Capital> capitals = new LinkedList<>();

        for (String capitalId : virusBindingModel.getCapitals()) {
            Capital capital = this.capitalRepository.findById(capitalId).orElse(null);

            if (capital == null) {
                continue;
            }

            capitals.add(capital);
        }

        virus.setCapitals(capitals);

        this.virusRepository.save(virus);
    }

    @Override
    public List<VirusServiceModel> findAllViruses() {
        return this.virusRepository.
                findAll()
                .stream()
                .map(v -> this.modelMapper.map(v, VirusServiceModel.class))
                .collect(Collectors.toList());

    }

    @Override
    public VirusServiceModel findVirusById(String id) {
        Virus virus = this.virusRepository.findById(id).orElse(null);

        if(virus == null){
            return null;
        }

        return this.modelMapper.map(virus, VirusServiceModel.class);
    }

    @Override
    public VirusBindingModel extractVirusByIdForEditOrDelete(String id) {
        Virus virus = this.virusRepository.findById(id).orElse(null);

        if (virus == null) {
            throw new IllegalArgumentException("Invalid id");
        }

        VirusBindingModel virusBindingModel = this.modelMapper.map(virus, VirusBindingModel.class);
        List<String> capitalIds =
                virus.getCapitals().stream().map(Capital::getId).collect(Collectors.toList());

        virusBindingModel.setCapitals(capitalIds);

        return virusBindingModel;
    }

    @Override
    public boolean deleteVirus(String id) {
       try{
           this.virusRepository.deleteById(id);

           return true;
       }catch (Exception e){
           e.printStackTrace();

           return false;
       }
    }
}
