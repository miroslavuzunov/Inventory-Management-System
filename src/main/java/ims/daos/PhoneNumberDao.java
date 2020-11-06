package ims.daos;

import ims.supporting.EntityManagerAssistant;
import ims.entities.PhoneNumber;

import javax.persistence.EntityManager;

public class PhoneNumberDao {
    public PhoneNumberDao() {
    }

    public void addUserViaPhoneNumber(PhoneNumber phoneNumber) {
        EntityManager manager = EntityManagerAssistant.initEntityManager();
        EntityManagerAssistant.beginTransaction(manager);

        manager.merge(phoneNumber); //save or update

        EntityManagerAssistant.commit(manager);
        EntityManagerAssistant.closeEntityManager(manager);
    }
}
