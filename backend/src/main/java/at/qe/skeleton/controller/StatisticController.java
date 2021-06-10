package at.qe.skeleton.controller;

import at.qe.skeleton.exceptions.StatisticsNotPresentException;
import at.qe.skeleton.model.GameDto;
import at.qe.skeleton.model.UserDto;
import at.qe.skeleton.payload.response.SuccessResponse;
import at.qe.skeleton.services.StatisticService;
import org.apache.coyote.Response;
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

    @GetMapping("/users/{userId}/statistics/winratio")
    private ResponseEntity<?> getWinLossRatio(@PathVariable Long userId){
        int numberOfWins = statisticService.getNumberOfWins(userId);
        int numberOfGames = statisticService.getNumberOfGames(userId);

        if(numberOfGames == 0){
            throw new StatisticsNotPresentException(userId);
        }

        float ratio = (float) numberOfWins / (float) numberOfGames;

        return ResponseEntity.ok((new SuccessResponse(ratio)).toString());
    }

    @GetMapping("/users/{userId}/statistics/history")
    private ResponseEntity<?> getMatchHistory(@PathVariable Long userId){
        List<GameDto> matches = statisticService.getMatchHistory(userId);
        if(matches.isEmpty()){
            throw new StatisticsNotPresentException(userId);
        }

        return ResponseEntity.ok((new SuccessResponse(matches)).toString());
    }

    @GetMapping("/users/{userId}/statistics/lastplayedwith")
    private ResponseEntity<?> getLastPlayedWith(@PathVariable Long userId){
        if(statisticService.getMatchHistory(userId).isEmpty()){
            throw new StatisticsNotPresentException(userId);
        }
        List<UserDto> lastUsers = statisticService.getLastPlayedWith(userId);


        return ResponseEntity.ok((new SuccessResponse(lastUsers)).toString());
    }


}
