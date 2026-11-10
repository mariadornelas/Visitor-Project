package com.faculdade.sensor;

/**
 * Visitor: uma operação sobre a estrutura de equipamentos, com UM método para cada
 * tipo concreto de elemento. Cada visitante concentra, num único lugar, o que a
 * operação faz com sensores de temperatura, de pressão, de vibração e com linhas.
 *
 * @param <R> o tipo do resultado da operação (texto, nível de risco, lista...)
 */
public interface Visitante<R> {

    R visitar(SensorTemperatura sensor);

    R visitar(SensorPressao sensor);

    R visitar(SensorVibracao sensor);

    R visitar(LinhaProducao linha);
}
