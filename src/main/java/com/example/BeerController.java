package com.example;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;

/**
 * @author Marcin Grzejszczak
 */
@RestController
class BeerController {

	private final RestTemplate restTemplate;

	int port = 8090;

	BeerController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@RequestMapping(method = RequestMethod.POST,
			value = "/beer",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String gimmeABeer(@RequestBody Person person) throws MalformedURLException {
    BeerRequest beerRequest = new BeerRequest(person.age);
    ResponseEntity<Response> response = this.restTemplate.exchange(
	  		RequestEntity
				  .post(URI.create("http://localhost:" + port + "/check"))
				  .contentType(MediaType.APPLICATION_JSON)
				  .body(beerRequest),
				Response.class);
		switch (response.getBody().status) {
			case OK:
				return "THERE YOU GO";
			default:
				return "GET LOST";
		}
	}
}

class Person {
	public String name;
	public int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public Person() {
	}
}

class Response {
	public ResponseStatus status;
}

enum ResponseStatus {
	OK, NOT_OK
}

class BeerRequest {
  public int age;

  public BeerRequest (int age) {
    this.age = age;
  }

  public BeerRequest() {}
}


