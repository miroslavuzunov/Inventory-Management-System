package ims.entities;

import ims.enums.PhoneType;

import javax.persistence.*;

@Entity
@Table(name = "phone_number")
public class PhoneNumber extends BaseEntity {
    private PhoneType phoneType;
    private String number;
    private User owner;

    public PhoneNumber() {
    }

    @Column(name = "phone_type")
    @Enumerated(EnumType.STRING)
    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    @Column(name = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
