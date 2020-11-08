package ims.daos;

import ims.supporting.EntityManagerAssistant;
import ims.entities.PhoneNumber;

import javax.persistence.EntityManager;

public class PhoneNumberDao extends EntityManagerAssistant<PhoneNumber> {

    public PhoneNumberDao(Class<PhoneNumber> classType) {
        super(classType);
    }

    public void addUserViaPhoneNumber(PhoneNumber phoneNumber) {
        updateRecord(phoneNumber);
    }
}
