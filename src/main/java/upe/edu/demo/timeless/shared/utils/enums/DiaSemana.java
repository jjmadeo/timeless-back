package upe.edu.demo.timeless.shared.utils.enums;

public enum DiaSemana {
    LUNES(1),
    MARTES(2),
    MIERCOLES(3),
    JUEVES(4),
    VIERNES(5),
    SABADO(6),
    DOMINGO(7);

    private final int valor;

    DiaSemana(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public static DiaSemana fromValor(int valor) {
        for (DiaSemana dia : DiaSemana.values()) {
            if (dia.getValor() == valor) {
                return dia;
            }
        }
        throw new IllegalArgumentException("Valor inválido para el día de la semana: " + valor);
    }

    public static int buscarPorNombre(String nombre) {
        for (DiaSemana dia : DiaSemana.values()) {
            if (dia.name().equalsIgnoreCase(nombre)) {
                return dia.getValor();
            }
        }
        throw new IllegalArgumentException("Nombre de día inválido: " + nombre);
    }
}
