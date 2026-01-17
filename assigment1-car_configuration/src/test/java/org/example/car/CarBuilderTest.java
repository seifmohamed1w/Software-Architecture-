// src/test/java/org/example/car/CarBuilderTest.java
package org.example.car;

import org.example.car.enums.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarBuilderTest {

    @Test
    void build_validSuv_shouldSucceed() {
        Car car = new CarBuilder()
                .setModel(CarModel.SUV)
                .setEngine(EngineType.V6)
                .setTransmission(Transmission.AUTOMATIC)
                .setSunroof(true)
                .addSafety(SafetyFeature.AIRBAGS)
                .addSafety(SafetyFeature.REAR_CAMERA) // mandatory for SUV (per CarOptions)
                .build();

        assertEquals(CarModel.SUV, car.getModel());
        assertEquals(EngineType.V6, car.getEngine());
        assertEquals(Transmission.AUTOMATIC, car.getTransmission());
        assertTrue(car.getSafetyFeatures().contains(SafetyFeature.AIRBAGS));
        assertTrue(car.getSafetyFeatures().contains(SafetyFeature.REAR_CAMERA));
    }

    @Test
    void build_missingMandatoryAirbags_shouldFail() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> new CarBuilder()
                .setModel(CarModel.SEDAN)
                .setEngine(EngineType.V6)
                .setTransmission(Transmission.MANUAL)
                .build());

        assertTrue(ex.getMessage().toLowerCase().contains("mandatory")
                || ex.getMessage().toLowerCase().contains("missing"));
    }

    @Test
    void build_suvMissingRearCamera_shouldFail() {
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> new CarBuilder()
                .setModel(CarModel.SUV)
                .setEngine(EngineType.V6)
                .setTransmission(Transmission.AUTOMATIC)
                .addSafety(SafetyFeature.AIRBAGS)
                .build());

        assertTrue(ex.getMessage().toLowerCase().contains("rear_camera")
                || ex.getMessage().toLowerCase().contains("rear camera"));
    }

    @Test
    void setEngine_sportsWithoutV8_shouldFailEarly() {
        assertThrows(IllegalStateException.class, () -> new CarBuilder()
                .setModel(CarModel.SPORTS)
                .setEngine(EngineType.V6)); // SPORTS allows only V8
    }

    @Test
    void setSunroof_onSports_shouldFailEarly() {
        assertThrows(IllegalStateException.class, () -> new CarBuilder()
                .setModel(CarModel.SPORTS)
                .setSunroof(true));
    }

    @Test
    void setTransmission_electricManual_shouldFailEarly() {
        assertThrows(IllegalStateException.class, () -> new CarBuilder()
                .setModel(CarModel.SEDAN)
                .setEngine(EngineType.ELECTRIC)
                .setTransmission(Transmission.MANUAL));
    }

    @Test
    void build_missingRequiredFields_shouldFail() {
        assertThrows(IllegalStateException.class, () -> new CarBuilder()
                .setModel(CarModel.SEDAN)
                .setEngine(EngineType.V6)
                .addSafety(SafetyFeature.AIRBAGS)
                .build()); // missing transmission
    }

    @Test
    void build_validSedanElectricAutomatic_shouldSucceed() {
        Car car = new CarBuilder()
                .setModel(CarModel.SEDAN)
                .setEngine(EngineType.ELECTRIC)
                .setTransmission(Transmission.AUTOMATIC)
                .addSafety(SafetyFeature.AIRBAGS)
                .addInterior(InteriorFeature.GPS)
                .setColor(Color.BLUE)
                .setRims(Rims.STANDARD)
                .build();

        assertEquals(CarModel.SEDAN, car.getModel());
        assertEquals(EngineType.ELECTRIC, car.getEngine());
        assertEquals(Transmission.AUTOMATIC, car.getTransmission());
        assertTrue(car.getInteriorFeatures().contains(InteriorFeature.GPS));
    }
}
