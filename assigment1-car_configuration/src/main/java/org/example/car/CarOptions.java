package org.example.car;

import org.example.car.enums.*;

import java.util.EnumSet;
import java.util.Set;

public final class CarOptions {
    private final CarModel model;

    public CarOptions(CarModel model) {
        this.model = model;
    }

    public Set<EngineType> allowedEngines() {
        return switch (model) {
            case SEDAN -> EnumSet.of(EngineType.V6, EngineType.ELECTRIC);
            case SUV -> EnumSet.of(EngineType.V6, EngineType.V8);
            case SPORTS -> EnumSet.of(EngineType.V8);
        };
    }

    public Set<Transmission> allowedTransmissions(EngineType engine) {
        if (engine == EngineType.ELECTRIC) return EnumSet.of(Transmission.AUTOMATIC);
        return EnumSet.allOf(Transmission.class);
    }

    public boolean sunroofAvailable() {
        return model != CarModel.SPORTS; // example rule
    }

    public Set<SafetyFeature> mandatorySafety() {
        return (model == CarModel.SUV)
                ? EnumSet.of(SafetyFeature.AIRBAGS, SafetyFeature.REAR_CAMERA)
                : EnumSet.of(SafetyFeature.AIRBAGS);
    }
}
