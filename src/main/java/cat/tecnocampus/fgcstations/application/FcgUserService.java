package cat.tecnocampus.fgcstations.application;

import cat.tecnocampus.fgcstations.application.DTOs.*;
import cat.tecnocampus.fgcstations.application.exception.UserDoesNotExistsException;
import cat.tecnocampus.fgcstations.application.mapper.MapperHelper;
import cat.tecnocampus.fgcstations.domain.DayTimeStart;
import cat.tecnocampus.fgcstations.domain.FavoriteJourney;
import cat.tecnocampus.fgcstations.domain.Journey;
import cat.tecnocampus.fgcstations.domain.User;
import cat.tecnocampus.fgcstations.persistence.DayTimeStartRepository;
import cat.tecnocampus.fgcstations.persistence.FavoriteJourneyRepository;
import cat.tecnocampus.fgcstations.persistence.JourneyRepository;
import cat.tecnocampus.fgcstations.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FcgUserService {
    private final UserRepository userRepository;
    private final FavoriteJourneyRepository favoriteJourneyRepository;
    private final JourneyRepository journeyRepository;
    private final DayTimeStartRepository dayTimeStartRepository;
    private final FgcStationService fgcStationService;

    public FcgUserService(UserRepository userRepository, FavoriteJourneyRepository favoriteJourneyRepository, JourneyRepository journeyRepository, DayTimeStartRepository dayTimeStartRepository, FgcStationService fgcStationService) {
        this.userRepository = userRepository;
        this.favoriteJourneyRepository = favoriteJourneyRepository;
        this.journeyRepository = journeyRepository;
        this.dayTimeStartRepository = dayTimeStartRepository;
        this.fgcStationService = fgcStationService;
    }

    public UserDTO getUserDTO(String username) {
        //TODO 10.0: get the user (domain) given her username.
        User user = getDomainUser(username);

        // TODO 11.0: get the user's favorite journeys
        user.setFavoriteJourneyList(getFavoriteJourneys(username));

        //domain users are mapped to DTOs
        return MapperHelper.userToUserDTO(user);
    }

    public User getDomainUser(String username) {
        // TODO 10.1: get the user (domain) given her username. If the user does not exist, throw a UserDoesNotExistsException
        //  You can solve this exercise without leaving this file
        return userRepository.findByUsername(username).orElseThrow(() -> new UserDoesNotExistsException("User doesn't exist: " + username));
    }


    public UserDTOnoFJ getUserDTOWithNoFavoriteJourneys(String username) {
        // TODO 12: get the user (UserDTOnoFJ) given her username. If the user does not exist, throw a UserDoesNotExistsException
        return userRepository.findProjectedByUsername(username).orElseThrow(() -> new UserDoesNotExistsException("User doesn't exist: " + username));
    }

    public UserDTOInterface getUserDTOInterface(String username) {
        // TODO 13: get the user (UserDTOInterface) given her username. If the user does not exist, throw a UserDoesNotExistsException
        return userRepository.findInterfaceByUsername(username).orElseThrow(() -> new UserDoesNotExistsException("User doesn't exist: " + username));
    }

    public List<UserDTO> getUsers() {
        //TODO 14: get all users (domain). You can solve this exercise without leaving this file
        List<User> users = userRepository.findAll(); //feed this list with the users

        //get the users' favorite journeys
        users.forEach(u -> u.setFavoriteJourneyList(getFavoriteJourneys(u.getUsername())));

        return users.stream().map(MapperHelper::userToUserDTO).toList();
    }

    // TODO 22: This method updates a user. Make sure the user is saved without explicitly calling the save method
    public void updateUser(UserDTOnoFJ userDTO) {
        User user = getDomainUser(userDTO.username());
        user.setName(userDTO.name());
        user.setSecondName(userDTO.secondName());
        user.setEmail(userDTO.email());
    }

    public List<UserTopFavoriteJourney> getTop3UsersWithMostFavoriteJourneys() {
        // TODO 16: get the top 3 users (UserTopFavoriteJourney) with the most favorite journeys
        return userRepository.findFirst3ByOrderByFavoriteJourneysDesc();
    }

    public List<UserDTOInterface> getUsersByNameAndSecondName(String name, String secondName) {
        // TODO 17: get the users (UserDTOInterface) given their name and second name. Try not to use any sql (jpql) query
        return userRepository.findByNameAndBySecondName(name, secondName);
    }

    public List<FavoriteJourney> getFavoriteJourneys(String username) {
        User user = getDomainUser(username);

        // TODO 11.1: get the user's favorite journeys given the User (domain object)
        List<FavoriteJourney> favoriteJourneys = user.getFavoriteJourneyList(); //feed this list with the favorite journeys
        return favoriteJourneys;
    }

    public List<FavoriteJourneyDTO> getFavoriteJourneysDTO(String username) {
        User user = getDomainUser(username);
        List<FavoriteJourney> favoriteJourneys = user.getFavoriteJourneyList(); //Same as TODO 11.1: feed this list with the favorite journeys

        return favoriteJourneys.stream().map(MapperHelper::favoriteJourneyToFavoriteJourneyDTO).toList();
    }

    //Adding a favorite journey to the user. We need to save a favorite journey that didn't exist before
    public void addUserFavoriteJourney(String username, FavoriteJourneyDTO favoriteJourneyDTO) {
        FavoriteJourney favoriteJourney = convertFavoriteJourneyDTO(username, favoriteJourneyDTO);

        // TODO 19: explicitly save the journey. You can solve this exercise without leaving this file
        Journey journey = favoriteJourney.getJourney();
        if (journey != null) {
            journeyRepository.save(journey);
        }

        // TODO 20: explicitly save the favorite journey. You can solve this exercise without leaving this file
        favoriteJourneyRepository.save(favoriteJourney);

        // TODO 21: explicitly save all the dayTimeStarts of the favorite journey. You can solve this exercise without leaving this file
        List<DayTimeStart> dayTimeStarts = favoriteJourney.getDayTimeStarts(); /* do something here for each daytimeStarts*/

        for (DayTimeStart dayTimeStart : dayTimeStarts) {
            if(dayTimeStart != null){
                dayTimeStartRepository.save(dayTimeStart);
            }
        }
    }

    private FavoriteJourney convertFavoriteJourneyDTO(String username, FavoriteJourneyDTO favoriteJourneyDTO) {
        FavoriteJourney favoriteJourney = new FavoriteJourney();
        favoriteJourney.setUser(getDomainUser(username));
        favoriteJourney.setId(UUID.randomUUID().toString());
        Journey journey = new Journey(fgcStationService.getStation(favoriteJourneyDTO.getOrigin()),
                fgcStationService.getStation(favoriteJourneyDTO.getDestination()));
        favoriteJourney.setJourney(journey);

        return favoriteJourney;
    }

    public List<PopularDayOfWeek> getPopularDayOfWeek() {
        // TODO 18: get the most popular day of the week (PopularDayOfWeek) among the dayTimeStarts
        return favoriteJourneyRepository.findMostPopularDayOfWeek();
    }
}
