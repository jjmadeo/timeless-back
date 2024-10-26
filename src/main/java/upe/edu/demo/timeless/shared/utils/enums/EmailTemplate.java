package upe.edu.demo.timeless.shared.utils.enums;

public enum EmailTemplate {
    TURNO_TOMADO("turno_tomado.html", "Turno Confirmado"),
    TURNO_CANCELADO("cancelacion.html", "Turno Cancelado"),
    TURNO_CONFIRMADO("confirmacion.html", "Turno Confirmado"),
    RECORDATORIO_TURNO("recordatorio.html", "Recordatorio de Turno"),

    GENERAL("general.html", "Notificacion");

    private final String templateFile;
    private final String subject;

    EmailTemplate(String templateFile, String subject) {
        this.templateFile = templateFile;
        this.subject = subject;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public String getSubject() {
        return subject;
    }
}
