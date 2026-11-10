package com.faculdade.sensor;

public class SensorVibracao extends Sensor {

    private final double mmPorSegundo;

    public SensorVibracao(String id, int horasOperacao, double mmPorSegundo) {
        super(id, horasOperacao);
        finito(mmPorSegundo, "vibração");
        if (mmPorSegundo < 0) {
            throw new IllegalArgumentException("vibração não pode ser negativa: " + mmPorSegundo);
        }
        this.mmPorSegundo = mmPorSegundo;
    }

    public double getMmPorSegundo() {
        return mmPorSegundo;
    }

    @Override
    public <R> R aceitar(Visitante<R> visitante) {
        return visitante.visitar(this);
    }
}
