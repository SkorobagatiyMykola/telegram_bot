package ua.skorobahatyi.telegram_bot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.skorobahatyi.telegram_bot.config.BotConfig;
import ua.skorobahatyi.telegram_bot.model.Lesson;
import ua.skorobahatyi.telegram_bot.model.User;
import ua.skorobahatyi.telegram_bot.repository.LessonRepository;
import ua.skorobahatyi.telegram_bot.repository.UserRepository;
import ua.skorobahatyi.telegram_bot.utils.keyboard.KeyboardRowBot;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private UserRepository userRepository;
    private LessonRepository lessonRepository;

    private final static String HELP_TEXT = "This is first my bot. Demonstrated work for my family.\n" +
            "You can execute commands from the main menu\n" +
            "Type /start to see a walcome message\n" +
            "Type /help to see this message again.";

    @Autowired
    public TelegramBot(BotConfig config, UserRepository userRepository, LessonRepository lessonRepository) {
        super(config.getBotToken());
        this.config = config;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get start work"));
        listOfCommands.add(new BotCommand("/family", "Our family"));
        listOfCommands.add(new BotCommand("/mydata", "get your data stored"));
        listOfCommands.add(new BotCommand("/deletedata", "delete my data"));
        listOfCommands.add(new BotCommand("/help", "info how to use this bot"));
        listOfCommands.add(new BotCommand("/tetiana", "get info from Tetiana"));
        listOfCommands.add(new BotCommand("/pasha", "get info from Pasha"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }

    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    startCommandReceived(chatId, HELP_TEXT);
                    break;
                case "/test":
                    startCommandReceived(chatId, "test");
                    break;
                case "/tetiana":
                    startCommandReceivedToPupil(chatId, "Виберіть день, щоб дізнатись, які уроки у Тетяни.", "Tatiana");
                    //startCommandReceivedToTetiana(chatId, "Виберіть день, щоб дізнатись, які уроки у Тетяни.");
                    break;
                case "/pasha":
                    startCommandReceivedToPupil(chatId, "Виберіть день, щоб дізнатись, які уроки у Паши.", "Pasha");
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized.");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            //  For testing
            EditMessageText message = new EditMessageText();
            message.setChatId(chatId);

            String[] arrStr = callbackData.split("_");
            String name = arrStr[0];
            String day = arrStr[1].equals("TODAY") ? DayOfWeek.from(LocalDate.now()).name() : arrStr[1];

            //List<Lesson> lessons = lessonRepository.getLessonsForTetiana(callbackData); (+) it's work
            List<Lesson> lessons = lessonRepository.getLessonsForPupilAndDay(name, day);
            String newText = generationAnswer(lessons, name, day);
            //String newText = generationAnswer(lessons, callbackData);

            message.setText(newText);
            message.setMessageId(messageId);

            try {
                execute(message);
            } catch (TelegramApiException ex) {
                log.error("Error occurred: " + ex.getMessage());
            }

        }


    }

    private String generationAnswer(List<Lesson> lessons, String day) {
        String text = "У Тетяни у " + day + " наступні уроки: \n";
        for (Lesson leson : lessons) {
            text += leson.getLessonDayId() + " - " + leson.getLesson() + "\n";
        }
        text += "Гарного дня.";
        return text;
    }

    private String generationAnswer(List<Lesson> lessons, String pupil, String day) {
        if (day.equals("SUNDAY") || day.equals("SUTURDAY"))
            return "Сьогодні вихідний, " + pupil + " відпочиває і набирається сил.";

        String name = pupil.equals("Tatiana") ? "Тетяни" : "Паші";
        String text = "У :" + name + " в " + day + " наступні уроки: \n";
        for (Lesson leson : lessons) {
            text += leson.getLessonDayId() + " - " + leson.getLesson() + "\n";
        }
        text += "Гарного дня.";
        return text;
    }


    private void registerUser(Message msg) {
        if (userRepository.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("User saved: " + user);

        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Hi, " + name + ", nice to meet you!!!" + " :smile: ");
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);
    }

    private void startCommandReceivedToTetiana(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        KeyboardRowBot keyboardRowBot = new KeyboardRowBot();

        InlineKeyboardMarkup markup = keyboardRowBot.createKeyboardForDays();
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException ex) {
            log.error("Error occurred: " + ex.getMessage());
        }
    }

    private void startCommandReceivedToPupil(long chatId, String textToSend, String pupil) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        KeyboardRowBot keyboardRowBot = new KeyboardRowBot();

        InlineKeyboardMarkup markup = keyboardRowBot.createKeyboardForDaysToPupil(pupil);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException ex) {
            log.error("Error occurred: " + ex.getMessage());
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Павлік");
        row.add("Анекдот");
        row.add("Танюшка");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Мама");
        row.add("Тато");
        row.add("Ха-ха-ха");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);


        try {
            execute(message);
        } catch (TelegramApiException ex) {
            log.error("Error occurred: " + ex.getMessage());
        }

    }


}
