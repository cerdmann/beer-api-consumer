package com.example;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureWireMock(port = 8090)
public class BeerControllerWireMockTest extends AbstractTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  BeerController beerController;


  @Test
  public void should_give_me_a_beer_when_im_old_enough() throws Exception {
    String responseBody = "{\"status\":\"OK\"}";

    WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/check"))
        .willReturn(
            WireMock.aResponse()
                .withBody(responseBody)
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
        ));

    mockMvc.perform(MockMvcRequestBuilders.post("/beer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json.write(new Person("marcin", 22)).getJson()))
        .andExpect(status().isOk())
        .andExpect(content().string("THERE YOU GO"));
  }

  @Test
  public void should_reject_a_beer_when_im_too_young() throws Exception {
    String responseBody = "{\"status\":\"NOT_OK\"}";

    WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/check"))
        .willReturn(
            WireMock.aResponse()
                .withBody(responseBody)
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
        ));

    mockMvc.perform(MockMvcRequestBuilders.post("/beer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json.write(new Person("marcin", 15)).getJson()))
        .andExpect(status().isOk())
        .andExpect(content().string("GET LOST"));
  }
}



