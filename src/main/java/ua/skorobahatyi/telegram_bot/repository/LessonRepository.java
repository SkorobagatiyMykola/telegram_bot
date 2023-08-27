package ua.skorobahatyi.telegram_bot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.skorobahatyi.telegram_bot.model.Lesson;

import java.util.List;

@Repository
public interface LessonRepository extends CrudRepository<Lesson,Long> {

    @Query(value = "SELECT * FROM lessons l WHERE l.day=:day ORDER BY l.lesson_day_id", nativeQuery = true)
    List<Lesson> getLessonsForTetiana(String day);

    @Query(value = "SELECT * FROM lessons l WHERE l.pupil=:pupil AND l.day=:day ORDER BY l.lesson_day_id", nativeQuery = true)
    List<Lesson> getLessonsForPupilAndDay(String pupil,String day);
}
