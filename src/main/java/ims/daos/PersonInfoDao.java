package ims.daos;

import ims.entities.PersonInfo;
import ims.entities.User;

import java.lang.reflect.Field;
import java.util.List;

public class PersonInfoDao extends AbstractDao<PersonInfo> {

    public PersonInfoDao() {
        super(PersonInfo.class);
    }

    public PersonInfo getRecordByEgn(String egn) throws NoSuchFieldException {
        Field field = PersonInfo.class.getDeclaredField("egn");
        List<PersonInfo> foundInfo = getRecordsByAttribute(field, egn);

        if(!foundInfo.isEmpty())
            return foundInfo.get(0);
        return null;
    }
}
