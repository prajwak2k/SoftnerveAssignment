package com.softnerve.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softnerve.model.Patient;
import com.softnerve.repository.PatientRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@Api(tags = { "CRUD Operation on patient details"})
public class PatientController {
	@Autowired
	PatientRepository repository;
	@ApiOperation(value = "Add a new  Patient" , notes = "Create a new patient")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400 , message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error")
			})
	@PostMapping("/patient")
	public  String savePatient(@RequestBody Patient patient) {
	 repository.save(patient);
	 return "Patient data is Strore";
	}
	
	@ApiOperation(value = "Get all patient data" , notes =" get all patient")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400 , message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error")
			})
	@GetMapping("/patient")
	public ResponseEntity<List<Patient>> getAllPatient(){
		List<Patient> list = new ArrayList<>();
		repository.findAll().forEach(list::add);
		return new ResponseEntity<List<Patient>>(list,HttpStatus.OK);
				
	}
	
	@ApiOperation(value = " Get single Patient Details by ID" , notes=" One patient details")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400 , message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error")
			})
	@GetMapping("/patient/{pid}")
	
	public Optional<Patient> getFindById(@PathVariable ("pid") int id){
		return repository.findById(id);
	}
	@ApiOperation(value = "Update Patient Details", notes="update patients details by using Id")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400 , message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error")
			})
	@PutMapping("/patient/update/{pid}")
	public String getPatientById(@PathVariable("pid") int id, @RequestBody Patient patient ) {
		Optional<Patient>optional=repository.findById(id);
		if(optional.isPresent()) {
			Patient patient2 = optional.get();
			patient2.setName(patient.getName());
			patient2.setContactdetails(patient.getContactdetails());
			patient2.setAddress(patient.getAddress());
			patient2.setPincode(patient.getPincode());
			repository.save(patient2);
			return"Patient details Is Upadte Successfully";
		}else {
			return"The Patient Details Does Not Exits";
		}	
	}
	
	@ApiOperation(value = "Delete Patient Details" , notes =" delete details by patient Id")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400 , message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error")
			})
	@DeleteMapping("/patient/delete/{pid}")
	public void deletePatientById(@PathVariable ("pid") int id) {
		repository.deleteById(id);
		System.out.println("Patients Details Is deleted Successfully");
	}		
}
