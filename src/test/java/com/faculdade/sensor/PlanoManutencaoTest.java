package com.faculdade.sensor;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanoManutencaoTest {

    private final PlanoManutencao plano = new PlanoManutencao();

    @Test
    void temperaturaDeveVencerEmDuasMilHoras() {
        assertEquals(List.of(), new SensorTemperatura("T", 1999, 20).aceitar(plano));
        assertEquals(List.of("T"), new SensorTemperatura("T", 2000, 20).aceitar(plano));
    }

    @Test
    void pressaoDeveVencerEmMilHoras() {
        assertEquals(List.of(), new SensorPressao("P", 999, 5).aceitar(plano));
        assertEquals(List.of("P"), new SensorPressao("P", 1000, 5).aceitar(plano));
    }

    @Test
    void vibracaoDeveVencerEmQuinhentasHoras() {
        assertEquals(List.of(), new SensorVibracao("V", 499, 2).aceitar(plano));
        assertEquals(List.of("V"), new SensorVibracao("V", 500, 2).aceitar(plano));
    }

    @Test
    void mesmasHorasDevemVencerParaUmTipoENaoParaOutro() {
        // 1200 h: vence pressão e vibração, mas ainda não vence temperatura.
        assertEquals(List.of(), new SensorTemperatura("T", 1200, 20).aceitar(plano));
        assertEquals(List.of("P"), new SensorPressao("P", 1200, 5).aceitar(plano));
        assertEquals(List.of("V"), new SensorVibracao("V", 1200, 2).aceitar(plano));
    }

    @Test
    void linhaDeveJuntarOsVencidosNaOrdemDaTravessiaIncluindoAninhadas() {
        LinhaProducao interna = new LinhaProducao("L2",
                new SensorTemperatura("T-02", 2500, 70),   // vencido
                new SensorPressao("P-02", 100, 5));         // em dia
        LinhaProducao externa = new LinhaProducao("L1",
                new SensorTemperatura("T-01", 1500, 85),   // em dia
                new SensorPressao("P-01", 1200, 6.5),       // vencido
                new SensorVibracao("V-01", 300, 8),         // em dia
                interna);

        assertEquals(List.of("P-01", "T-02"), externa.aceitar(plano));
    }

    @Test
    void linhaVaziaOuSemVencidosDeveDarListaVazia() {
        assertEquals(List.of(), new LinhaProducao("L1").aceitar(plano));
        assertEquals(List.of(), new LinhaProducao("L1", new SensorVibracao("V", 10, 2)).aceitar(plano));
    }

    @Test
    void visitanteDeveSerReutilizavelSemAcumularEstado() {
        LinhaProducao linha = new LinhaProducao("L1", new SensorPressao("P", 2000, 5));

        assertEquals(List.of("P"), linha.aceitar(plano));
        assertEquals(List.of("P"), linha.aceitar(plano));   // não duplica na segunda vez
    }
}
