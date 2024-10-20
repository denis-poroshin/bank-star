package ru.star.springbankstar.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.star.springbankstar.ProductDto.RecommendationDto;
import ru.star.springbankstar.UserDto.UserDto;
import ru.star.springbankstar.repositorys.TelegramBotRepository;
import ru.star.springbankstar.services.RecommendationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    @Autowired
    private TelegramBot telegramBot;
    private final TelegramBotRepository telegramBotRepository;
    private final RecommendationService recommendationService;

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    public TelegramBotUpdatesListener(TelegramBotRepository telegramBotRepository, RecommendationService recommendationService) {
        this.telegramBotRepository = telegramBotRepository;
        this.recommendationService = recommendationService;
    }


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            String message = update.message().text();
            String[] splitMessage = message.split(" ");
            if (splitMessage[0].equals("/recommend")){
                Collection<UserDto> userList = telegramBotRepository.getUser(splitMessage[1]);
                userList.forEach(userDto -> {
                    RecommendationDto recommendation = recommendationService.getRecommendation(userDto.getId());
                    String messageUser = ("Здравствуйте %s %s\n" +
                            "Новые продукты для вас: %s").formatted(userDto.getFirstName(),
                            userDto.getLastName(),
                            recommendation.getRecommendations());
                    SendMessage sendMessage = new SendMessage(update.message().chat().id(),
                            messageUser);
                    telegramBot.execute(sendMessage);
                });
            }

            SendMessage sendMessage;
            switch (update.message().text()){
                case "/help":
                    sendMessage = new SendMessage(update.message().chat().id(),
                            getHelp());
                    telegramBot.execute(sendMessage);
                    break;
                case "/start":
                    String nameUser = update.message().from().firstName();
                    sendMessage = new SendMessage(update.message().chat().id(),
                            getHello(nameUser));
                    telegramBot.execute(sendMessage);
                    break;

            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    private String getHelp(){
        return "Чтобы узнать какие продукты больше всего тебе подходят\n" +
                "пропиши команду /recommend и свое имя.\n"
                + "К примеру /recommend sheron.berge";
    }

    private String getHello(String firstName){
        logger.info("Hello bot {}", firstName);
        return "Привет " + firstName + "!\n"
                + "Этот телеграм бот предназначен для нахождения самых лучших продуктов для пользоватлей.\n"
                + "Чтобы узнать как им пользоваться введи команду /help.";
    }


}
