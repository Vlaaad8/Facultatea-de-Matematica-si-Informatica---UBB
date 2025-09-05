import java.time.LocalDateTime;
import java.util.Objects;

public class MessageTask extends Task{
    private String message;
    private String from;
    private String to;
    LocalDateTime date;

    public MessageTask(String id, String desc, String message,String from, String to, LocalDateTime date) {
        super(id, desc);
        this.message = message;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString() + "\n MessageTask{" +
                "message='" + message + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MessageTask that = (MessageTask) o;
        return Objects.equals(message, that.message) && Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), message, from, to, date);
    }

    @Override
    void execute() {
        System.out.println("Message executed" + date.format(Utils.formatter)+"\n");
    }
}
