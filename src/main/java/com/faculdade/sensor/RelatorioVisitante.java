package com.faculdade.sensor;

import java.util.Locale;
import java.util.StringJoiner;

public class RelatorioVisitante implements Visitante<String> {

    @Override
    public String visitar(SensorTemperatura s) {
        return String.format(Locale.ROOT, "[%s] temperatura: %.1f °C (%d h de operação)",
                s.getId(), s.getCelsius(), s.getHorasOperacao());
    }

    @Override
    public String visitar(SensorPressao s) {
        return String.format(Locale.ROOT, "[%s] pressão: %.2f bar = %.0f kPa (%d h de operação)",
                s.getId(), s.getBar(), s.getBar() * 100, s.getHorasOperacao());
    }

    @Override
    public String visitar(SensorVibracao s) {
        return String.format(Locale.ROOT, "[%s] vibração: %.1f mm/s (%d h de operação)",
                s.getId(), s.getMmPorSegundo(), s.getHorasOperacao());
    }

    @Override
    public String visitar(LinhaProducao linha) {
        StringJoiner texto = new StringJoiner("\n");
        texto.add("Linha " + linha.getNome() + ":");
        for (Elemento filho : linha.getElementos()) {
            filho.aceitar(this).lines().forEach(l -> texto.add("  " + l));
        }
        return texto.toString();
    }
}
