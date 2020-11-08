package ims.services;

import ims.daos.DepreciationDegreeDao;
import ims.entities.DepreciationDegree;
import ims.supporting.CustomField;

import java.util.ArrayList;
import java.util.List;

public class ProductRegistrationService {
    private final DepreciationDegreeDao depreciationDegreeDao;

    public ProductRegistrationService() {
        this.depreciationDegreeDao = new DepreciationDegreeDao(DepreciationDegree.class);
    }

    public List<CustomField> initializeDepreciationDegree() {
        List<CustomField> controllerDegrees = new ArrayList<>();
        List<DepreciationDegree> dbDegrees = depreciationDegreeDao.getAllDegrees();

        dbDegrees.forEach(degree -> {
            controllerDegrees.add(new CustomField(degree.getCategory() + " (" + degree.getProductGroup() + ")"));
        });

        return controllerDegrees;
    }
}
