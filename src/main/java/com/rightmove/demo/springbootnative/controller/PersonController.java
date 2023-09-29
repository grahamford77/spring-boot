package com.rightmove.demo.springbootnative.controller;

import java.net.URI;

import com.rightmove.demo.springbootnative.repository.ObjectNotFoundException;
import com.rightmove.demo.springbootnative.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
	private static final String GET_PERSON_BY_ID_URL = "/person/%s";

	private final PersonService personService;

	@GetMapping("{id}")
	public ResponseEntity<PersonDto> getPerson(@PathVariable String id) {
		try {
			PersonDto personDto = personService.getPerson(id);
			return ResponseEntity.ok(personDto);
		} catch (ObjectNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<PersonDto> storePerson(@RequestBody PersonDto personDto) {
		String id = personService.storePerson(personDto);
		return ResponseEntity.created(URI.create(String.format(GET_PERSON_BY_ID_URL, id))).build();
	}
}
