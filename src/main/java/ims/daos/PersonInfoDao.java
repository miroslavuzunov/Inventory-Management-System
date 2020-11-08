package ims.daos;

import ims.entities.PersonInfo;
import ims.entities.User;
import ims.supporting.EntityManagerAssistant;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonInfoDao extends EntityManagerAssistant<PersonInfo> {

    public PersonInfoDao(Class<PersonInfo> classType) {
        super(classType);
    }

    public PersonInfo getPersonInfoByEgn(String egn) {
        return getRecordByAttribute("egn", egn);
    }
}
