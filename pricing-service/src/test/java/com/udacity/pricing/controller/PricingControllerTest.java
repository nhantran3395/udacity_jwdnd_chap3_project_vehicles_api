package com.udacity.pricing.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.udacity.pricing.api.PricingController;
import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.service.PriceException;
import com.udacity.pricing.service.PricingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PricingController.class)
public class PricingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PricingService pricingService;

    @Before
    public void setUp() throws PriceException {
        Price price = new Price("USD",new BigDecimal(2000),1L);
        when(pricingService.getPrice(any())).thenReturn(price);
    }

    @Test
    public void shouldReturnPriceIfVehicleIdExist() throws Exception {
        mvc.perform(get("/services/price")
                .param("vehicleId","1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("['currency']",is("USD")))
                .andExpect(jsonPath("['price']",is(2000)))
                .andExpect(jsonPath("['vehicleId']",is(1)));
    }
}
