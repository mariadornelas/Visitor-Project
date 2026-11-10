package com.faculdade.sensor;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElementosTest {

    private static class Espiao implements Visitante<String> {
        final List<Object> recebidos = new ArrayList<>();

        @Override
        public String visitar(SensorTemperatura s) {
            recebidos.add(s);
            return "temperatura";
        }

        @Override
        public String visitar(SensorPressao s) {
            recebidos.add(s);
            return "pressao";
        }

        @Override
        public String visitar(SensorVibracao s) {
            recebidos.add(s);
            return "vibracao";
        }

        @Override
        public String visitar(LinhaProducao l) {
            recebidos.add(l);
            return "linha";
        }
    }

    @Test
    void cadaElementoDeveChamarOMetodoVisitarDoSeuProprioTipo() {
        Espiao espiao = new Espiao();

        assertEquals("temperatura", new SensorTemperatura("T", 0, 20).aceitar(espiao));
        assertEquals("pressao", new SensorPressao("P", 0, 5).aceitar(espiao));
        assertEquals("vibracao", new SensorVibracao("V", 0, 2).aceitar(espiao));
        assertEquals("linha", new LinhaProducao("L").aceitar(espiao));
    }

    @Test
    void despachoDeveSerPeloTipoRealMesmoComTipoEstaticoGenerico() {
        Elemento t = new SensorTemperatura("T", 0, 20);
        Sensor p = new SensorPressao("P", 0, 5);
        Elemento v = new SensorVibracao("V", 0, 2);
        Espiao espiao = new Espiao();

        assertEquals("temperatura", t.aceitar(espiao));
        assertEquals("pressao", p.aceitar(espiao));
        assertEquals("vibracao", v.aceitar(espiao));
    }

    @Test
    void elementoDeveEntregarASiMesmoAoVisitante() {
        SensorPressao p = new SensorPressao("P", 0, 5);
        LinhaProducao l = new LinhaProducao("L", p);
        Espiao espiao = new Espiao();

        p.aceitar(espiao);
        l.aceitar(espiao);

        assertSame(p, espiao.recebidos.get(0));
        assertSame(l, espiao.recebidos.get(1));
    }

    @Test
    void aceitarDeveDevolverExatamenteOResultadoDoVisitante() {
        Visitante<Integer> constante = new Visitante<>() {
            public Integer visitar(SensorTemperatura s) {
                return 42;
            }

            public Integer visitar(SensorPressao s) {
                return 42;
            }

            public Integer visitar(SensorVibracao s) {
                return 42;
            }

            public Integer visitar(LinhaProducao l) {
                return 42;
            }
        };

        assertEquals(42, new SensorVibracao("V", 0, 2).aceitar(constante));
    }

    @Test
    void sensoresDevemExporSeusDados() {
        SensorTemperatura t = new SensorTemperatura("T-01", 1500, 85.5);
        SensorPressao p = new SensorPressao("P-01", 1200, 6.5);
        SensorVibracao v = new SensorVibracao("V-01", 300, 8.0);

        assertEquals("T-01", t.getId());
        assertEquals(1500, t.getHorasOperacao());
        assertEquals(85.5, t.getCelsius());
        assertEquals(6.5, p.getBar());
        assertEquals(8.0, v.getMmPorSegundo());
    }

    @Test
    void deveRejeitarIdEHorasInvalidos() {
        assertThrows(NullPointerException.class, () -> new SensorTemperatura(null, 0, 20));
        assertThrows(IllegalArgumentException.class, () -> new SensorTemperatura("  ", 0, 20));
        assertThrows(IllegalArgumentException.class, () -> new SensorTemperatura("T", -1, 20));
    }

    @Test
    void deveRejeitarValoresFisicamenteInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new SensorTemperatura("T", 0, Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> new SensorTemperatura("T", 0, -300));
        assertThrows(IllegalArgumentException.class, () -> new SensorPressao("P", 0, -0.1));
        assertThrows(IllegalArgumentException.class, () -> new SensorPressao("P", 0, Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> new SensorVibracao("V", 0, -1));
        assertThrows(IllegalArgumentException.class, () -> new SensorVibracao("V", 0, Double.NaN));
    }

    @Test
    void valoresNosLimitesFisicosDevemSerAceitos() {
        assertDoesNotThrow(() -> new SensorTemperatura("T", 0, -273.15));
        assertDoesNotThrow(() -> new SensorPressao("P", 0, 0));
        assertDoesNotThrow(() -> new SensorVibracao("V", 0, 0));
    }

    @Test
    void linhaDeveGuardarOsFilhosNaOrdem() {
        SensorTemperatura t = new SensorTemperatura("T", 0, 20);
        SensorPressao p = new SensorPressao("P", 0, 5);

        LinhaProducao linha = new LinhaProducao("L1", t, p);

        assertEquals("L1", linha.getNome());
        assertEquals(List.of(t, p), linha.getElementos());
    }

    @Test
    void listaDeFilhosDeveSerSomenteLeitura() {
        LinhaProducao linha = new LinhaProducao("L1", new SensorTemperatura("T", 0, 20));

        assertThrows(UnsupportedOperationException.class, () -> linha.getElementos().clear());
    }

    @Test
    void linhaDeveCopiarOArrayRecebidoNaCriacao() {
        Elemento[] original = {new SensorTemperatura("T", 0, 20)};
        LinhaProducao linha = new LinhaProducao("L1", original);

        original[0] = new SensorPressao("P", 0, 5);

        assertInstanceOf(SensorTemperatura.class, linha.getElementos().get(0));
    }

    @Test
    void linhaDeveRejeitarArgumentosNulos() {
        assertThrows(NullPointerException.class, () -> new LinhaProducao(null));
        assertThrows(NullPointerException.class, () -> new LinhaProducao("L", (Elemento[]) null));
        assertThrows(NullPointerException.class, () -> new LinhaProducao("L", (Elemento) null));
    }

    @Test
    void linhaPodeConterOutrasLinhas() {
        LinhaProducao interna = new LinhaProducao("L2", new SensorVibracao("V", 0, 2));
        LinhaProducao externa = new LinhaProducao("L1", interna);

        assertSame(interna, externa.getElementos().get(0));
    }
}
