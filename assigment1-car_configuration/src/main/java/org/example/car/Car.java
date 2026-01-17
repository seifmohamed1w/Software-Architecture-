package org.example.car;

import org.example.car.enums.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Car {

    private final CarModel model;
    private final EngineType engine;
    private final Transmission transmission;

    private final Color color;
    private final Rims rims;
    private final boolean sunroof;

    private final Set<InteriorFeature> interiorFeatures;
    private final Set<SafetyFeature> safetyFeatures;

    Car(CarModel model,
        EngineType engine,
        Transmission transmission,
        Color color,
        Rims rims,
        boolean sunroof,
        Set<InteriorFeature> interiorFeatures,
        Set<SafetyFeature> safetyFeatures) {

        this.model = Objects.requireNonNull(model, "model");
        this.engine = Objects.requireNonNull(engine, "engine");
        this.transmission = Objects.requireNonNull(transmission, "transmission");
        this.color = Objects.requireNonNull(color, "color");
        this.rims = Objects.requireNonNull(rims, "rims");
        this.sunroof = sunroof;

        this.interiorFeatures = Collections.unmodifiableSet(new HashSet<>(interiorFeatures));
        this.safetyFeatures = Collections.unmodifiableSet(new HashSet<>(safetyFeatures));
    }

    public CarModel getModel() { return model; }
    public EngineType getEngine() { return engine; }
    public Transmission getTransmission() { return transmission; }

    public Color getColor() { return color; }
    public Rims getRims() { return rims; }
    public boolean hasSunroof() { return sunroof; }

    public Set<InteriorFeature> getInteriorFeatures() { return interiorFeatures; }
    public Set<SafetyFeature> getSafetyFeatures() { return safetyFeatures; }

    @Override
    public String toString() {
        return "Car{" +
                "model=" + model +
                ", engine=" + engine +
                ", transmission=" + transmission +
                ", color=" + color +
                ", rims=" + rims +
                ", sunroof=" + sunroof +
                ", interiorFeatures=" + interiorFeatures +
                ", safetyFeatures=" + safetyFeatures +
                '}';
    }
}
