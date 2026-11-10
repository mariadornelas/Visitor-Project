package com.faculdade.sensor;

/**
 * ConcreteVisitor 2: classifica o risco. Os limites de cada grandeza vivem aqui,
 * e não nos sensores. Para uma linha, o risco é o do seu pior elemento.
 */
public class AvaliadorRisco implements Visitante<Nivel> {

    @Override
    public Nivel visitar(SensorTemperatura s) {
        return classificar(s.getCelsius(), 80.0, 90.0);
    }

    @Override
    public Nivel visitar(SensorPressao s) {
        return classificar(s.getBar(), 8.0, 10.0);
    }

    @Override
    public Nivel visitar(SensorVibracao s) {
        return classificar(s.getMmPorSegundo(), 4.5, 7.1);
    }

    @Override
    public Nivel visitar(LinhaProducao linha) {
        Nivel pior = Nivel.NORMAL;
        for (Elemento filho : linha.getElementos()) {
            Nivel nivel = filho.aceitar(this);
            if (nivel.compareTo(pior) > 0) {
                pior = nivel;
            }
        }
        return pior;
    }

    private static Nivel classificar(double valor, double alerta, double critico) {
        if (valor >= critico) return Nivel.CRITICO;
        if (valor >= alerta) return Nivel.ALERTA;
        return Nivel.NORMAL;
    }
}
