package com.example.employee.task.tracker.model;

import com.example.employee.task.tracker.config.FilterConstants;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@MappedSuperclass
@FilterDef(
        name = FilterConstants.TENANT_FILTER,
        parameters = @ParamDef(name = FilterConstants.TENANT_FILTER_PARAM, type = String.class)
)
@Filter(
        name = FilterConstants.TENANT_FILTER,
        condition = "tenant_id = :" + FilterConstants.TENANT_FILTER_PARAM
)
public class TenantBaseEntity<ID> extends BaseEntity<ID> {

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

