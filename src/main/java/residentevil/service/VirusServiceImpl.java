package residentevil.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import residentevil.domain.entities.Virus;
import residentevil.domain.models.service.VirusServiceModel;
import residentevil.domain.models.view.VirusListViewModel;
import residentevil.repository.VirusRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VirusServiceImpl implements VirusService {

    private final VirusRepository virusRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public VirusServiceImpl(VirusRepository virusRepository, ModelMapper modelMapper) {
        this.virusRepository = virusRepository;
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
