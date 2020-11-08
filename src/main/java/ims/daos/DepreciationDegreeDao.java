package ims.daos;

import ims.entities.DepreciationDegree;
import ims.supporting.EntityManagerAssistant;

import java.util.List;

public class DepreciationDegreeDao extends EntityManagerAssistant<DepreciationDegree> {

    public DepreciationDegreeDao(Class<DepreciationDegree> classType) {
        super(classType);
    }

    public List<DepreciationDegree> getAllDegrees() {
        return getAll();
    }
}
