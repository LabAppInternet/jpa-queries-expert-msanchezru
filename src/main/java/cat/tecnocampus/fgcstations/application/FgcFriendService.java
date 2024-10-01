package cat.tecnocampus.fgcstations.application;


import cat.tecnocampus.fgcstations.application.DTOs.FriendUserDTO;
import cat.tecnocampus.fgcstations.application.DTOs.UserFriendsDTO;
import cat.tecnocampus.fgcstations.application.DTOs.UserTopFriend;
import cat.tecnocampus.fgcstations.application.mapper.MapperHelper;
import cat.tecnocampus.fgcstations.domain.Friend;
import cat.tecnocampus.fgcstations.domain.User;
import cat.tecnocampus.fgcstations.persistence.FriendRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FgcFriendService {
    private final FriendRepository friendRepository;
    private final FcgUserService fcgUserService;

    public FgcFriendService(FriendRepository friendRepository, FcgUserService fcgUserService) {
        this.friendRepository = friendRepository;
        this.fcgUserService = fcgUserService;
    }

    public UserFriendsDTO getUserFriends(String username) {
        User user = fcgUserService.getDomainUser(username);

        // TODO 20: find all the friends of a user given her username. You can solve this exercise without any sql query
        List<Friend> friends = friendRepository.findAllByUserUsername(username); //feed the list with the friends of the user
        return MapperHelper.listOfAUserFriendsToUserFriendsDTO(friends);
    }

    public List<UserFriendsDTO> getAllUserFriends() {
        // TODO 21: find all the friends (domain) of all users. You can solve this exercise without leaving this file
        //  note that domain objects are mapped to DTOs
        return MapperHelper.allUserFriendListToListUserFriendsDTO(friendRepository.findAll()); // replace the empty list with the list of all users
    }

    public void saveFriends(UserFriendsDTO userFriendsDTO) {
        User user = fcgUserService.getDomainUser(userFriendsDTO.getUsername());
        friendRepository.saveAll(MapperHelper.friendsDTOToUserListOfFriends(user, userFriendsDTO));
    }

    public List<UserTopFriend> getTop3UsersWithMostFriends() {
        // TODO 22: find the top 3 users with the most friends.
        return friendRepository.findTop3UsersWithMostFriends();
    }

    // Find all users whose friends have a certain name
    public List<FriendUserDTO> getUsersByFriend(String friendName) {
        // TODO 23: find all users whose friends have a certain name.
        //return friendRepository.findUserDTOByFriend(friendName);
        List<Friend> friends = friendRepository.findAllByIdUsername(friendName);
        List<FriendUserDTO> friendUserDTOs = new ArrayList<>();

        for (Friend friend : friends) {
            friendUserDTOs.add(new FriendUserDTO() {
                @Override
                public String getUserUsername() {
                    return friend.getUsername();
                }

                @Override
                public String getUserName() {
                    return friend.getFriend();
                }
            });
        }

        return friendUserDTOs;
    }

}
