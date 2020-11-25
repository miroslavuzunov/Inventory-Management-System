package ims.daos;

import ims.entities.DepreciationDegree;
import ims.entities.PersonInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

public class PersonInfoDao extends AbstractDao<PersonInfo> {

    public PersonInfoDao() {
        super(PersonInfo.class);
    }

    public PersonInfo getRecordByEgn(String egn) throws NoSuchFieldException {
        Field field = PersonInfo.class.getDeclaredField("egn");
        PersonInfo personInfo =
                getRecordByAttribute(field, egn);

        if (personInfo != null)
            return personInfo;
        return new PersonInfo();
    }
}
