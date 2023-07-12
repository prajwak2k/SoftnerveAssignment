package com.softnerve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softnerve.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
	

}
