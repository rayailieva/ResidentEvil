package residentevil.service;

import residentevil.domain.models.service.VirusServiceModel;

import java.util.List;

public interface VirusService {

    VirusServiceModel addVirus(VirusServiceModel virusServiceModel);

    List<VirusServiceModel> findAllViruses();

    VirusServiceModel findVirusById(String id);

    boolean deleteVirus(String id);
}
