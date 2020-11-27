package ims.daos;

import ims.entities.DepreciationDegree;

import java.lang.reflect.Field;

public class DepreciationDegreeDao extends AbstractDao<DepreciationDegree> {

    public DepreciationDegreeDao() {
        super(DepreciationDegree.class);
    }

    public DepreciationDegree getRecordByCategory(String category) throws NoSuchFieldException {
        Field field = DepreciationDegree.class.getDeclaredField("category");
        DepreciationDegree depreciationDegree =
                getRecordsByAttribute(field, category).get(0);

        if (depreciationDegree != null)
            return depreciationDegree;
        return new DepreciationDegree();
    }
}
