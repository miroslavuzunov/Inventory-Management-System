package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.User;

import java.lang.reflect.Field;
import java.util.List;

public class DepreciationDegreeDao extends AbstractDao<DepreciationDegree> {

    public DepreciationDegreeDao() {
        super(DepreciationDegree.class);
    }

    public DepreciationDegree getRecordByCategory(String category) throws NoSuchFieldException {
        Field field = DepreciationDegree.class.getDeclaredField("category");
        List<DepreciationDegree> foundDegrees = getRecordsByAttribute(field, category);

        if(!foundDegrees.isEmpty())
            return foundDegrees.get(0);
        return null;
    }
}
