package com.faculdade.sensor;

import java.util.Objects;

public abstract class Sensor implements Elemento {

    private final String id;
    private final int horasOperacao;

    protected Sensor(String id, int horasOperacao) {
        Objects.requireNonNull(id, "id não pode ser nulo");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id não pode ser vazio");
        }
        if (horasOperacao < 0) {
            throw new IllegalArgumentException("horas de operação não podem ser negativas");
        }
        this.id = id;
        this.horasOperacao = horasOperacao;
    }

    public String getId() {
        return id;
    }

    public int getHorasOperacao() {
        return horasOperacao;
    }

    /** Valida que o valor é um número finito. */
    protected static double finito(double valor, String nome) {
        if (Double.isNaN(valor) || Double.isInfinite(valor)) {
            throw new IllegalArgumentException(nome + " inválido: " + valor);
        }
        return valor;
    }
}
