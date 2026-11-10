package com.faculdade.sensor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelatorioVisitanteTest {

    private final RelatorioVisitante relatorio = new RelatorioVisitante();

    @Test
    void deveFormatarSensorDeTemperatura() {
        assertEquals("[T-01] temperatura: 85.0 °C (1500 h de operação)",
                new SensorTemperatura("T-01", 1500, 85.0).aceitar(relatorio));
    }

    @Test
    void deveFormatarSensorDePressaoComConversaoParaKpa() {
        assertEquals("[P-01] pressão: 6.50 bar = 650 kPa (1200 h de operação)",
                new SensorPressao("P-01", 1200, 6.5).aceitar(relatorio));
    }

    @Test
    void deveFormatarSensorDeVibracao() {
        assertEquals("[V-01] vibração: 8.0 mm/s (300 h de operação)",
                new SensorVibracao("V-01", 300, 8.0).aceitar(relatorio));
    }

    @Test
    void linhaDeveListarOsFilhosComRecuo() {
        LinhaProducao linha = new LinhaProducao("L1",
                new SensorTemperatura("T-01", 10, 70.0),
                new SensorVibracao("V-01", 20, 2.0));

        assertEquals("Linha L1:\n"
                + "  [T-01] temperatura: 70.0 °C (10 h de operação)\n"
                + "  [V-01] vibração: 2.0 mm/s (20 h de operação)", linha.aceitar(relatorio));
    }

    @Test
    void linhasAninhadasDevemAumentarORecuoACadaNivel() {
        LinhaProducao interna = new LinhaProducao("L2", new SensorPressao("P-02", 5, 1.0));
        LinhaProducao externa = new LinhaProducao("L1", new SensorTemperatura("T-01", 10, 70.0), interna);

        assertEquals("Linha L1:\n"
                + "  [T-01] temperatura: 70.0 °C (10 h de operação)\n"
                + "  Linha L2:\n"
                + "    [P-02] pressão: 1.00 bar = 100 kPa (5 h de operação)", externa.aceitar(relatorio));
    }

    @Test
    void linhaVaziaDeveTerSoOCabecalho() {
        assertEquals("Linha L1:", new LinhaProducao("L1").aceitar(relatorio));
    }

    @Test
    void mesmoVisitanteReutilizadoDeveDarOMesmoResultado() {
        LinhaProducao linha = new LinhaProducao("L1", new SensorTemperatura("T-01", 10, 70.0));

        assertEquals(linha.aceitar(relatorio), linha.aceitar(relatorio));
    }
}
