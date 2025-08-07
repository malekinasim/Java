package com.example.employee.task.tracker.service.organ;

import com.example.employee.task.tracker.config.exception.CustomException;
import com.example.employee.task.tracker.model.Organ;
import com.example.employee.task.tracker.repoeitory.organ.OrganRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganServiceImpl implements OrganService{
    private final OrganRepository organRepository;

    public OrganServiceImpl(OrganRepository organRepository) {
        this.organRepository = organRepository;
    }

    @Override
    public Organ getEmployeeOrgan(String employeeNumber) {
        return organRepository.findEmployeeOrgan(employeeNumber).orElseThrow(
                ()-> new CustomException("error.employee-organ.not-found")
        );
    }

    @Override
    public Organ getOrganByCode(String organCode) {
        return organRepository.findOrganByCode(organCode).orElseThrow(
                ()-> new CustomException("error.employee-organ.not-found")
        );
    }

    @Override
    public Organ findById(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public List<Organ> findAll() {
        return List.of();
    }

    @Override
    public Organ save(Organ organ) {
        return null;
    }

    @Override
    public Organ update(Organ organ) {
        return null;
    }
}
