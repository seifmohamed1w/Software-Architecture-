// src/main/java/org/example/car/CarBuilder.java
package org.example.car;

import org.example.car.enums.*;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public class CarBuilder {

    private CarModel model;
    private CarOptions options; // model-specific availability rules

    private EngineType engine;
    private Transmission transmission;

    // Optional exterior defaults
    private Color color = Color.BLACK;
    private Rims rims = Rims.STANDARD;
    private boolean sunroof = false;

    private final Set<InteriorFeature> interior = EnumSet.noneOf(InteriorFeature.class);
    private final Set<SafetyFeature> safety = EnumSet.noneOf(SafetyFeature.class);

    public CarBuilder setModel(CarModel model) {
        this.model = Objects.requireNonNull(model, "model cannot be null");
        this.options = new CarOptions(this.model);
        return this;
    }

    public CarBuilder setEngine(EngineType engine) {
        engine = Objects.requireNonNull(engine, "engine cannot be null");
        require(model, "model must be set before engine");

        if (!options.allowedEngines().contains(engine)) {
            throw new IllegalStateException("Engine " + engine + " is not available for model " + model);
        }

        this.engine = engine;

        // If engine changed, ensure existing transmission (if already set) is still valid
        if (this.transmission != null && !options.allowedTransmissions(this.engine).contains(this.transmission)) {
            throw new IllegalStateException(
                    "Transmission " + transmission + " is not compatible with engine " + this.engine
            );
        }

        return this;
    }

    public CarBuilder setTransmission(Transmission transmission) {
        transmission = Objects.requireNonNull(transmission, "transmission cannot be null");
        require(engine, "engine must be set before transmission");

        if (!options.allowedTransmissions(engine).contains(transmission)) {
            throw new IllegalStateException("Transmission " + transmission + " is not available for engine " + engine);
        }

        this.transmission = transmission;
        return this;
    }

    public CarBuilder setColor(Color color) {
        this.color = Objects.requireNonNull(color, "color cannot be null");
        return this;
    }

    public CarBuilder setRims(Rims rims) {
        this.rims = Objects.requireNonNull(rims, "rims cannot be null");
        return this;
    }

    public CarBuilder setSunroof(boolean enabled) {
        require(model, "model must be set before sunroof");

        if (enabled && !options.sunroofAvailable()) {
            throw new IllegalStateException("Sunroof is not available for model " + model);
        }

        this.sunroof = enabled;
        return this;
    }

    public CarBuilder addInterior(InteriorFeature feature) {
        this.interior.add(Objects.requireNonNull(feature, "feature cannot be null"));
        return this;
    }

    public CarBuilder addSafety(SafetyFeature feature) {
        this.safety.add(Objects.requireNonNull(feature, "feature cannot be null"));
        return this;
    }

    public Car build() {
        require(model, "model is required");
        require(engine, "engine is required");
        require(transmission, "transmission is required");

        // Enforce mandatory safety per model
        for (SafetyFeature required : options.mandatorySafety()) {
            if (!safety.contains(required)) {
                throw new IllegalStateException("Missing mandatory safety feature: " + required);
            }
        }

        // Final availability sanity checks (defensive)
        if (!options.allowedEngines().contains(engine)) {
            throw new IllegalStateException("Engine " + engine + " is not available for model " + model);
        }

        if (!options.allowedTransmissions(engine).contains(transmission)) {
            throw new IllegalStateException("Transmission " + transmission + " is not available for engine " + engine);
        }

        if (sunroof && !options.sunroofAvailable()) {
            throw new IllegalStateException("Sunroof is not available for model " + model);
        }

        return new Car(model, engine, transmission, color, rims, sunroof, interior, safety);
    }

    private static void require(Object value, String message) {
        if (value == null) throw new IllegalStateException(message);
    }
}
