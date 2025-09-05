package org.example.seminar8;

public class NotaDto {
    private Double nota;
    private String temaID;
    private String studentName;
    private String profesor;

    public NotaDto(Double nota, String temaID, String studentName, String profesor) {
        this.nota = nota;
        this.temaID = temaID;
        this.studentName = studentName;
        this.profesor = profesor;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public String getTemaID() {
        return temaID;
    }

    public void setTemaID(String temaID) {
        this.temaID = temaID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }
}
