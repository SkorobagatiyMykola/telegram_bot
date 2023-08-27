package ua.skorobahatyi.telegram_bot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity(name= "users")
@Data
@Getter@Setter
public class User {
    @Id
    private Long chatId;

    private String firstName;
    private String lastName;

    private String userName;

    private Timestamp registeredAt;

}
