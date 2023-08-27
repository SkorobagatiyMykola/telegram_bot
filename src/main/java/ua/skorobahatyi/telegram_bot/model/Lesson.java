package ua.skorobahatyi.telegram_bot.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "lessons")
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniquePupilAndDayAndLessonAndLessonId",
                columnNames = {"pupil", "day","lesson","lessonDayId"})})
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pupil;
    private String day;
    private String lesson;
    private int lessonDayId;
}
