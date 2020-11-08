package ims.entities;

import ims.enums.Criteria;

import javax.persistence.*;

@Entity
@Table(name = "scrapping_criteria")
public class ScrappingCriteria extends BaseEntity {
    private Criteria criteria;
    private String value;

    public ScrappingCriteria() {
    }

    @Column(name = "criteria")
    @Enumerated(EnumType.STRING)
    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
