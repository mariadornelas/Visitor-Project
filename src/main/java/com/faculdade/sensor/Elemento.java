package com.faculdade.sensor;

/** Element: tudo o que pode ser visitado. */
public interface Elemento {

    /**
     * Despacho duplo: o elemento entrega a si mesmo ao método {@code visitar} certo.
     * Cada classe concreta precisa implementar este método, porque só ela sabe o
     * seu próprio tipo em tempo de compilação.
     */
    <R> R aceitar(Visitante<R> visitante);
}
