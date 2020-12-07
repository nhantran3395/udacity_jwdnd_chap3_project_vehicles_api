package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CarServiceTest {

    private CarService carService;

    @Mock
    private CarRepository mockCarRepo;

    @Mock
    private MapsClient mockMapsClient;

    @Mock
    private PriceClient mockPriceClient;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        carService = new CarService(mockCarRepo, mockMapsClient, mockPriceClient);
    }

    @Test
    public void shouldThrownExceptionWhenFindWithNoneExistId(){
        Long nonExistId = Long.valueOf(1000);
        when(mockCarRepo.findById(nonExistId)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class,()->carService.findById(nonExistId));
    }

    @Test
    public void shouldReturnCarWithLocationAndPriceWhenFindWithValidId(){
        Long validId = Long.valueOf(1);
        Car car = new Car();
        Location location = new Location(Double.valueOf(30),Double.valueOf(20));

        car.setId(validId);
        car.setLocation(location);

        Location locationReturned = new Location(location.getLat(), location.getLon(),"844 No Colony Road","Wallingford","CT","6492");
        String priceReturned = "USD 10026.59";

        when(mockCarRepo.findById(validId)).thenReturn(Optional.of(car));
        when(mockMapsClient.getAddress(location)).thenReturn(locationReturned);
        when(mockPriceClient.getPrice(validId)).thenReturn(priceReturned);

        Car returnCar = carService.findById(validId);
        assertEquals(validId,returnCar.getId());
        assertEquals(locationReturned,returnCar.getLocation());
        assertEquals(priceReturned,returnCar.getPrice());
    }
}
