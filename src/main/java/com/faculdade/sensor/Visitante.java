package com.faculdade.sensor;

public interface Visitante<R> {

    R visitar(SensorTemperatura sensor);

    R visitar(SensorPressao sensor);

    R visitar(SensorVibracao sensor);

    R visitar(LinhaProducao linha);
}
