package com.faculdade.sensor;

import java.util.ArrayList;
import java.util.List;

public class PlanoManutencao implements Visitante<List<String>> {

    @Override
    public List<String> visitar(SensorTemperatura s) {
        return vencido(s, 2000);
    }

    @Override
    public List<String> visitar(SensorPressao s) {
        return vencido(s, 1000);
    }

    @Override
    public List<String> visitar(SensorVibracao s) {
        return vencido(s, 500);
    }

    @Override
    public List<String> visitar(LinhaProducao linha) {
        List<String> ids = new ArrayList<>();
        for (Elemento filho : linha.getElementos()) {
            ids.addAll(filho.aceitar(this));
        }
        return ids;
    }

    private static List<String> vencido(Sensor sensor, int intervaloHoras) {
        return sensor.getHorasOperacao() >= intervaloHoras ? List.of(sensor.getId()) : List.of();
    }
}
