package org.example.practic2.domain;

import java.time.LocalDateTime;

public class Need extends Entity{
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Long personInNeed;
    private Long personToSave;
    private String status;

    public Need(String title, String description, LocalDateTime deadline, Long personInNeed, Long personToSave, String status) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.personInNeed = personInNeed;
        this.personToSave = personToSave;
        this.status = status;
    }

    public Need(String title,String description, LocalDateTime deadline, Long personInNeed) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.personInNeed = personInNeed;
        this.personToSave =null;
        this.status = "Caut erou!";
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Long getPersoninneed() {
        return personInNeed;
    }

    public Long getPersontosave() {
        return personToSave;
    }

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setPersonToSave(Long personToSave) {
        this.personToSave = personToSave;
    }

}
