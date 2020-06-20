package io.reflectoring.cache.service;

import io.reflectoring.cache.dao.Car;
import io.reflectoring.cache.dao.CarRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @CachePut(value = "cars")
    public Car update(Car car) {
        if (carRepository.existsById(car.getId())) {
            return carRepository.save(car);
        }
        throw new IllegalArgumentException("A car mus have an id to be updated");
    }

    @Cacheable(value = "cars")
    public Car get(UUID uuid) {
        return carRepository.findById(uuid)
                .orElseThrow(() -> new IllegalStateException("car with id " + uuid + " was not found"));
    }

    @CacheEvict(value = "cars")
    public void delete(UUID uuid) {
        carRepository.deleteById(uuid);
    }

}
