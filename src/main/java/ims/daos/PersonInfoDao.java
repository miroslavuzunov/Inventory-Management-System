package ims.daos;

import ims.entities.PersonInfo;
import ims.entities.User;

import java.lang.reflect.Field;

public class PersonInfoDao extends AbstractDao<PersonInfo> {

    public PersonInfoDao() {
        super(PersonInfo.class);
    }

    public PersonInfo getRecordByEgn(String egn) throws NoSuchFieldException {
        Field field = PersonInfo.class.getDeclaredField("egn");

        if(!getRecordsByAttribute(field, egn).isEmpty())
            return getRecordsByAttribute(field, egn).get(0);
        return new PersonInfo();
    }
}
