package com.example.employee.task.tracker.model;

import com.example.employee.task.tracker.config.hibernate.FilterConstants;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@MappedSuperclass
@FilterDef(
        name = FilterConstants.ORGAN_FILTER,
        parameters = @ParamDef(name = FilterConstants.ORGAN_FILTER_PARAM, type = Long.class)
)
@Filter(
        name = FilterConstants.ORGAN_FILTER,
        condition = "tenant_id = :organ_prm"
)
public class OrganBaseEntity<ID> extends BaseEntity<ID> {

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Organ organ;

    public Organ getOrgan() {
        return organ;
    }

    public void setOrgan(Organ organ) {
        this.organ = organ;
    }
}

