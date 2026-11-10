package com.faculdade.sensor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvaliadorRiscoTest {

    private final AvaliadorRisco risco = new AvaliadorRisco();

    private Nivel temp(double v) {
        return new SensorTemperatura("T", 0, v).aceitar(risco);
    }

    private Nivel pressao(double v) {
        return new SensorPressao("P", 0, v).aceitar(risco);
    }

    private Nivel vibracao(double v) {
        return new SensorVibracao("V", 0, v).aceitar(risco);
    }

    @Test
    void temperaturaDeveUsarLimites80E90InclusivosEmCadaUm() {
        assertEquals(Nivel.NORMAL, temp(79.9));
        assertEquals(Nivel.ALERTA, temp(80.0));
        assertEquals(Nivel.ALERTA, temp(89.9));
        assertEquals(Nivel.CRITICO, temp(90.0));
    }

    @Test
    void pressaoDeveUsarLimites8E10() {
        assertEquals(Nivel.NORMAL, pressao(7.9));
        assertEquals(Nivel.ALERTA, pressao(8.0));
        assertEquals(Nivel.ALERTA, pressao(9.9));
        assertEquals(Nivel.CRITICO, pressao(10.0));
    }

    @Test
    void vibracaoDeveUsarLimites4_5E7_1() {
        assertEquals(Nivel.NORMAL, vibracao(4.4));
        assertEquals(Nivel.ALERTA, vibracao(4.5));
        assertEquals(Nivel.ALERTA, vibracao(7.0));
        assertEquals(Nivel.CRITICO, vibracao(7.1));
    }

    @Test
    void mesmoValorDeveTerGravidadeDiferenteConformeOTipoDoSensor() {
        // 8,0 é alerta de pressão, crítico de vibração e perfeitamente normal de temperatura.
        assertEquals(Nivel.NORMAL, temp(8.0));
        assertEquals(Nivel.ALERTA, pressao(8.0));
        assertEquals(Nivel.CRITICO, vibracao(8.0));
    }

    @Test
    void linhaDeveTerOPiorNivelDosFilhos() {
        LinhaProducao linha = new LinhaProducao("L1",
                new SensorTemperatura("T", 0, 70),      // NORMAL
                new SensorPressao("P", 0, 9),           // ALERTA
                new SensorVibracao("V", 0, 8));         // CRITICO

        assertEquals(Nivel.CRITICO, linha.aceitar(risco));
    }

    @Test
    void linhaDeveConsiderarLinhasAninhadas() {
        LinhaProducao interna = new LinhaProducao("L2", new SensorPressao("P", 0, 12));   // CRITICO
        LinhaProducao externa = new LinhaProducao("L1", new SensorTemperatura("T", 0, 70), interna);

        assertEquals(Nivel.CRITICO, externa.aceitar(risco));
    }

    @Test
    void linhaSoComSensoresNormaisDeveSerNormal() {
        LinhaProducao linha = new LinhaProducao("L1", new SensorTemperatura("T", 0, 70), new SensorPressao("P", 0, 3));

        assertEquals(Nivel.NORMAL, linha.aceitar(risco));
    }

    @Test
    void linhaComPiorSendoAlertaNaoDeveVirarCritica() {
        LinhaProducao linha = new LinhaProducao("L1", new SensorTemperatura("T", 0, 85), new SensorPressao("P", 0, 3));

        assertEquals(Nivel.ALERTA, linha.aceitar(risco));
    }

    @Test
    void linhaVaziaDeveSerNormal() {
        assertEquals(Nivel.NORMAL, new LinhaProducao("L1").aceitar(risco));
    }
}
