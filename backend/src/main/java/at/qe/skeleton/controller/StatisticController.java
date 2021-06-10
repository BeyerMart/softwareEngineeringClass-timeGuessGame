package at.qe.skeleton.controller;

import at.qe.skeleton.exceptions.StatisticsNotPresentException;
import at.qe.skeleton.model.GameDto;
import at.qe.skeleton.model.UserDto;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatisticController {
    @Autowired
    StatisticService statisticService;


    /**
     * Winratio of a specific user
     *
     * @param userId ID of user to get winratio from
     * @return ResponseEntity which includes the winratio
     */
    @GetMapping("/users/{userId}/statistics/winratio")
    private ResponseEntity<?> getWinLossRatio(@PathVariable Long userId) {
        int numberOfWins = statisticService.getNumberOfWins(userId);
        int numberOfGames = statisticService.getNumberOfGames(userId);

        if (numberOfGames == 0) {
            throw new StatisticsNotPresentException(userId);
        }

        float ratio = (float) numberOfWins / (float) numberOfGames;

        return ResponseEntity.ok((new SuccessResponse(ratio)).toString());
    }

    /**
     * Matchhistory of a specific user
     *
     * @param userId ID of user to get matchhistory from
     * @return Response entity which includes all previous matches of the user
     * @throws StatisticsNotPresentException if the user never played a game
     */
    @GetMapping("/users/{userId}/statistics/history")
    private ResponseEntity<?> getMatchHistory(@PathVariable Long userId) {
        List<GameDto> matches = statisticService.getMatchHistory(userId);
        if (matches.isEmpty()) {
            throw new StatisticsNotPresentException(userId);
        }

        return ResponseEntity.ok((new SuccessResponse(matches)).toString());
    }

    /**
     * Previously played with / previous teammates
     *
     * @param userId ID of user to get previous teammates from
     * @return Response entity which includes all previous teammates of the user
     * @throws StatisticsNotPresentException if there are no previous matches
     */
    @GetMapping("/users/{userId}/statistics/lastplayedwith")
    private ResponseEntity<?> getLastPlayedWith(@PathVariable Long userId) {
        if (statisticService.getMatchHistory(userId).isEmpty()) {
            throw new StatisticsNotPresentException(userId);
        }
        List<UserDto> lastUsers = statisticService.getLastPlayedWith(userId);

        return ResponseEntity.ok((new SuccessResponse(lastUsers)).toString());
    }

    /**
     * Total amount of played games
     *
     * @param userId ID of user to get total amount of played games from
     * @return Response entity which includes the number of played games
     */
    @GetMapping("/users/{userId}/statistics/totalgames")
    private ResponseEntity<?> getNumberOfTotalGames(@PathVariable Long userId) {
        int numberOfGames = statisticService.getNumberOfGames(userId);

        return ResponseEntity.ok((new SuccessResponse(numberOfGames)).toString());
    }

}
