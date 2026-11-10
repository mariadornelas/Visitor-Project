package com.faculdade.sensor;

/** ConcreteElement: pressão em bar. */
public class SensorPressao extends Sensor {

    private final double bar;

    public SensorPressao(String id, int horasOperacao, double bar) {
        super(id, horasOperacao);
        finito(bar, "pressão");
        if (bar < 0) {
            throw new IllegalArgumentException("pressão não pode ser negativa: " + bar);
        }
        this.bar = bar;
    }

    public double getBar() {
        return bar;
    }

    @Override
    public <R> R aceitar(Visitante<R> visitante) {
        return visitante.visitar(this);
    }
}
