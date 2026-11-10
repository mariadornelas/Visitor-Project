package com.faculdade.sensor;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Provas da estrutura do padrão: extensibilidade por visitantes e organização das classes. */
class PadraoVisitorTest {

    private static LinhaProducao planta() {
        return new LinhaProducao("L1",
                new SensorTemperatura("T-01", 1500, 85.0),
                new SensorPressao("P-01", 1200, 6.5),
                new SensorVibracao("V-01", 300, 8.0),
                new LinhaProducao("L2",
                        new SensorTemperatura("T-02", 2500, 70.0),
                        new SensorPressao("P-02", 100, 10.5)));
    }

    @Test
    void novaOperacaoDeveSerAdicionadaSemAlterarNenhumElemento() {
        // Operação criada só neste teste: soma de horas de operação de toda a árvore.
        Visitante<Integer> somaHoras = new Visitante<>() {
            public Integer visitar(SensorTemperatura s) {
                return s.getHorasOperacao();
            }

            public Integer visitar(SensorPressao s) {
                return s.getHorasOperacao();
            }

            public Integer visitar(SensorVibracao s) {
                return s.getHorasOperacao();
            }

            public Integer visitar(LinhaProducao l) {
                int soma = 0;
                for (Elemento e : l.getElementos()) soma += e.aceitar(this);
                return soma;
            }
        };

        assertEquals(1500 + 1200 + 300 + 2500 + 100, planta().aceitar(somaHoras));
    }

    @Test
    void mesmaEstruturaDeveGerarResultadosDeTiposDiferentesConformeOVisitante() {
        LinhaProducao planta = planta();

        String texto = planta.aceitar(new RelatorioVisitante());
        Nivel nivel = planta.aceitar(new AvaliadorRisco());
        List<String> vencidos = planta.aceitar(new PlanoManutencao());

        assertTrue(texto.startsWith("Linha L1:"));
        assertEquals(Nivel.CRITICO, nivel);
        assertEquals(List.of("P-01", "T-02"), vencidos);
    }

    @Test
    void estruturaNaoDeveSerAlteradaPorNenhumVisitante() {
        LinhaProducao planta = planta();
        String antes = planta.aceitar(new RelatorioVisitante());

        planta.aceitar(new AvaliadorRisco());
        planta.aceitar(new PlanoManutencao());

        assertEquals(antes, planta.aceitar(new RelatorioVisitante()));
    }

    @Test
    void visitanteDeveTerUmMetodoPorTipoDeElementoConcreto() {
        List<Class<?>> tipos = Arrays.stream(Visitante.class.getDeclaredMethods())
                .peek(m -> assertEquals("visitar", m.getName()))
                .map(m -> m.getParameterTypes()[0])
                .toList();

        assertEquals(4, tipos.size());
        assertTrue(tipos.containsAll(List.of(SensorTemperatura.class, SensorPressao.class,
                SensorVibracao.class, LinhaProducao.class)));
    }

    @Test
    void cadaElementoConcretoDeveDeclararOSeuProprioAceitar() throws NoSuchMethodException {
        // Se aceitar fosse herdado, o tipo estático seria sempre o da superclasse e o despacho duplo falharia.
        for (Class<?> c : List.of(SensorTemperatura.class, SensorPressao.class, SensorVibracao.class,
                LinhaProducao.class)) {
            Method m = c.getDeclaredMethod("aceitar", Visitante.class);
            assertEquals(c, m.getDeclaringClass(), c.getSimpleName());
        }
    }

    @Test
    void sensorBaseNaoDeveImplementarAceitar() {
        boolean declara = Arrays.stream(Sensor.class.getDeclaredMethods()).anyMatch(m -> m.getName().equals("aceitar"));

        assertFalse(declara);
        assertTrue(Modifier.isAbstract(Sensor.class.getModifiers()));
    }

    @Test
    void elementosDevemSerImutaveisPoisVisitanteSoLe() {
        for (Class<?> c : List.of(Sensor.class, SensorTemperatura.class, SensorPressao.class, SensorVibracao.class,
                LinhaProducao.class)) {
            for (Field f : c.getDeclaredFields()) {
                assertTrue(Modifier.isFinal(f.getModifiers()), c.getSimpleName() + "." + f.getName() + " é mutável");
            }
        }
    }

    @Test
    void elementosNaoDevemConhecerOsVisitantesConcretos() {
        // Os elementos só dependem da interface Visitante, nunca de RelatorioVisitante, AvaliadorRisco etc.
        for (Class<?> c : List.of(Sensor.class, SensorTemperatura.class, SensorPressao.class, SensorVibracao.class,
                LinhaProducao.class, Elemento.class)) {
            for (Method m : c.getDeclaredMethods()) {
                for (Class<?> p : m.getParameterTypes()) {
                    assertFalse(Visitante.class.isAssignableFrom(p) && p != Visitante.class,
                            c.getSimpleName() + " depende de visitante concreto " + p.getSimpleName());
                }
            }
            for (Field f : c.getDeclaredFields()) {
                assertFalse(Visitante.class.isAssignableFrom(f.getType()), c.getSimpleName() + "." + f.getName());
            }
        }
    }
}
