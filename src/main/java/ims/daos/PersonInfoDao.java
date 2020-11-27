package ims.daos;

import ims.entities.PersonInfo;

import java.lang.reflect.Field;

public class PersonInfoDao extends AbstractDao<PersonInfo> {

    public PersonInfoDao() {
        super(PersonInfo.class);
    }

    public PersonInfo getRecordByEgn(String egn) throws NoSuchFieldException {
        Field field = PersonInfo.class.getDeclaredField("egn");
        PersonInfo personInfo =
                getRecordsByAttribute(field, egn).get(0);

        if (personInfo != null)
            return personInfo;
        return new PersonInfo();
    }
}
