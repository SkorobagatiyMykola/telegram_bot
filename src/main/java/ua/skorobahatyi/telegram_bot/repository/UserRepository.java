package ua.skorobahatyi.telegram_bot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.skorobahatyi.telegram_bot.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
}
