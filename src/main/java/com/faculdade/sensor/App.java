package com.faculdade.sensor;

public class App {

    public static void main(String[] args) {
        SensorTemperatura t1 = new SensorTemperatura("T-01", 1500, 85.0);
        SensorPressao p1 = new SensorPressao("P-01", 1200, 6.5);
        SensorVibracao v1 = new SensorVibracao("V-01", 300, 8.0);
        LinhaProducao l2 = new LinhaProducao("L2",
                new SensorTemperatura("T-02", 2500, 70.0),
                new SensorPressao("P-02", 100, 10.5));
        LinhaProducao l1 = new LinhaProducao("L1", t1, p1, v1, l2);

        System.out.println("=== Visitante 1: relatório ===");
        System.out.println(l1.aceitar(new RelatorioVisitante()));

        System.out.println();
        System.out.println("=== Visitante 2: risco ===");
        AvaliadorRisco risco = new AvaliadorRisco();
        System.out.println("T-01: " + t1.aceitar(risco));
        System.out.println("P-01: " + p1.aceitar(risco));
        System.out.println("V-01: " + v1.aceitar(risco));
        System.out.println("L2:   " + l2.aceitar(risco));
        System.out.println("L1:   " + l1.aceitar(risco) + "  (o pior de toda a árvore)");

        System.out.println();
        System.out.println("=== Visitante 3: manutenção vencida ===");
        System.out.println(l1.aceitar(new PlanoManutencao()));

        System.out.println();
        System.out.println("=== Operação nova, criada agora, sem tocar em nenhum sensor ===");
        int total = l1.aceitar(new Visitante<Integer>() {
            @Override
            public Integer visitar(SensorTemperatura s) {
                return 1;
            }

            @Override
            public Integer visitar(SensorPressao s) {
                return 1;
            }

            @Override
            public Integer visitar(SensorVibracao s) {
                return 1;
            }

            @Override
            public Integer visitar(LinhaProducao linha) {
                int soma = 0;
                for (Elemento e : linha.getElementos()) {
                    soma += e.aceitar(this);
                }
                return soma;
            }
        });
        System.out.println("Sensores em toda a linha L1: " + total);
    }
}
