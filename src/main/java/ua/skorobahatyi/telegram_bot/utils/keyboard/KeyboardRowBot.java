package ua.skorobahatyi.telegram_bot.utils.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardRowBot {

    public InlineKeyboardMarkup createKeyboardForDays(){
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        InlineKeyboardButton mondayBtn = new InlineKeyboardButton();
        mondayBtn.setText("Понеділок");
        mondayBtn.setCallbackData("MONDAY");

        InlineKeyboardButton tuesdayBtn = new InlineKeyboardButton();
        tuesdayBtn.setText("Вівторок");
        tuesdayBtn.setCallbackData("TUESDAY");

        InlineKeyboardButton wednesdayBtn = new InlineKeyboardButton();
        wednesdayBtn.setText("Середа");
        wednesdayBtn.setCallbackData("WEDNESDAY");

        firstRow.add(mondayBtn);
        firstRow.add(tuesdayBtn);
        firstRow.add(wednesdayBtn);


        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton thursdayBtn = new InlineKeyboardButton();
        thursdayBtn.setText("Четвер");
        thursdayBtn.setCallbackData("THURSDAY");

        InlineKeyboardButton fridayBtn = new InlineKeyboardButton();
        fridayBtn.setText("П'ятниця");
        fridayBtn.setCallbackData("FRIDAY");

        InlineKeyboardButton todayBtn = new InlineKeyboardButton();
        todayBtn.setText("Сьогодні");
        todayBtn.setCallbackData("TODAY");

        secondRow.add(thursdayBtn);
        secondRow.add(fridayBtn);
        secondRow.add(todayBtn);

        rows.add(firstRow);
        rows.add(secondRow);

        markupInLine.setKeyboard(rows);
        return markupInLine;
    }


    public InlineKeyboardMarkup createKeyboardForDaysToPupil(String pupil){
        String pupilSplit=pupil+"_";
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        InlineKeyboardButton mondayBtn = new InlineKeyboardButton();
        mondayBtn.setText("Понеділок");
        mondayBtn.setCallbackData(pupilSplit+"MONDAY");

        InlineKeyboardButton tuesdayBtn = new InlineKeyboardButton();
        tuesdayBtn.setText("Вівторок");
        tuesdayBtn.setCallbackData(pupilSplit+"TUESDAY");

        InlineKeyboardButton wednesdayBtn = new InlineKeyboardButton();
        wednesdayBtn.setText("Середа");
        wednesdayBtn.setCallbackData(pupilSplit+"WEDNESDAY");

        firstRow.add(mondayBtn);
        firstRow.add(tuesdayBtn);
        firstRow.add(wednesdayBtn);


        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton thursdayBtn = new InlineKeyboardButton();
        thursdayBtn.setText("Четвер");
        thursdayBtn.setCallbackData(pupilSplit+"THURSDAY");

        InlineKeyboardButton fridayBtn = new InlineKeyboardButton();
        fridayBtn.setText("П'ятниця");
        fridayBtn.setCallbackData(pupilSplit+"FRIDAY");

        InlineKeyboardButton todayBtn = new InlineKeyboardButton();
        todayBtn.setText("Сьогодні");
        todayBtn.setCallbackData(pupilSplit+"TODAY");

        secondRow.add(thursdayBtn);
        secondRow.add(fridayBtn);
        secondRow.add(todayBtn);

        rows.add(firstRow);
        rows.add(secondRow);

        markupInLine.setKeyboard(rows);
        return markupInLine;
    }

}
