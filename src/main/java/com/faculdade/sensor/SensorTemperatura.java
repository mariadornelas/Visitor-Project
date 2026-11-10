package com.faculdade.sensor;

public class SensorTemperatura extends Sensor {

    private final double celsius;

    public SensorTemperatura(String id, int horasOperacao, double celsius) {
        super(id, horasOperacao);
        finito(celsius, "temperatura");
        if (celsius < -273.15) {
            throw new IllegalArgumentException("abaixo do zero absoluto: " + celsius);
        }
        this.celsius = celsius;
    }

    public double getCelsius() {
        return celsius;
    }

    @Override
    public <R> R aceitar(Visitante<R> visitante) {
        return visitante.visitar(this);
    }
}
