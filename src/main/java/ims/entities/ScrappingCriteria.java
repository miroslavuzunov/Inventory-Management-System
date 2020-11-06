package ims.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scrapping_criteria")
public class ScrappingCriteria extends BaseEntity {
    private String criteria;
    private String value;

    public ScrappingCriteria() {
    }

    @Column(name = "criteria")
    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
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
