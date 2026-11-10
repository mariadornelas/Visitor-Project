package com.faculdade.sensor;

public interface Elemento {
    <R> R aceitar(Visitante<R> visitante);
}
