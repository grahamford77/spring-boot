package com.rightmove.demo.springbootnative.service;

import java.io.IOException;

import com.rightmove.demo.springbootnative.client.AgifyClient;
import com.rightmove.demo.springbootnative.controller.PersonDto;
import com.rightmove.demo.springbootnative.domain.Person;
import com.rightmove.demo.springbootnative.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {
	private static final String AGIFY_EXCEPTION_MESSAGE = "Unable to get age, using to 999";

	private final PersonRepository personRepository;
	private final AgifyClient agifyClient;

	public PersonDto getPerson(String id) {
		Person person = personRepository.getById(id);
		return PersonDto.fromPerson(person);
	}

	public String storePerson(PersonDto personDto) {
		long age;
		try {
			age = agifyClient.getAgeForName(personDto.firstName());
		} catch (IOException e) {
			age = 999L;
			log.warn(AGIFY_EXCEPTION_MESSAGE, e);
		} catch (InterruptedException e) {
			age = 999L;
			log.warn(AGIFY_EXCEPTION_MESSAGE, e);
			Thread.currentThread().interrupt();
		}

		Person personEntity = personDto.toPerson(age);
		return personRepository.store(personEntity);
	}
}
