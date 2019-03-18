package residentevil.service;

import residentevil.domain.models.binding.VirusBindingModel;
import residentevil.domain.models.service.VirusServiceModel;

import java.util.List;

public interface VirusService {

    VirusServiceModel addVirus(VirusServiceModel virusServiceModel);

    void editVirus(VirusBindingModel virusBindingModel);

    List<VirusServiceModel> findAllViruses();

    VirusServiceModel findVirusById(String id);

    VirusBindingModel extractVirusByIdForEditOrDelete(String id);

    boolean deleteVirus(String id);
}
