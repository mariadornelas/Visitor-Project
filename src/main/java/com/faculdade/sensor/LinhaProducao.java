package com.faculdade.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ConcreteElement composto: agrupa sensores e outras linhas (estrutura em árvore).
 * Imutável. Quem percorre os filhos é o visitante, que decide como combinar os resultados.
 */
public class LinhaProducao implements Elemento {

    private final String nome;
    private final List<Elemento> elementos;

    public LinhaProducao(String nome, Elemento... elementos) {
        this.nome = Objects.requireNonNull(nome, "nome não pode ser nulo");
        Objects.requireNonNull(elementos, "elementos não pode ser nulo");
        List<Elemento> copia = new ArrayList<>();
        for (Elemento e : elementos) {
            copia.add(Objects.requireNonNull(e, "elemento não pode ser nulo"));
        }
        this.elementos = Collections.unmodifiableList(copia);
    }

    public String getNome() {
        return nome;
    }

    public List<Elemento> getElementos() {
        return elementos;
    }

    @Override
    public <R> R aceitar(Visitante<R> visitante) {
        return visitante.visitar(this);
    }
}
