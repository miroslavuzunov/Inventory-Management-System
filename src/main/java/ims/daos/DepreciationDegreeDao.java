package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.User;

import java.lang.reflect.Field;

public class DepreciationDegreeDao extends AbstractDao<DepreciationDegree> {

    public DepreciationDegreeDao() {
        super(DepreciationDegree.class);
    }

    public DepreciationDegree getRecordByCategory(String category) throws NoSuchFieldException {
        Field field = DepreciationDegree.class.getDeclaredField("category");

        if(!getRecordsByAttribute(field, category).isEmpty())
            return getRecordsByAttribute(field, category).get(0);
        return new DepreciationDegree();
    }
}
