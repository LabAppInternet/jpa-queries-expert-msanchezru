package cat.tecnocampus.fgcstations.persistence;

import cat.tecnocampus.fgcstations.application.DTOs.FriendUserDTO;
import cat.tecnocampus.fgcstations.application.DTOs.UserTopFriend;
import cat.tecnocampus.fgcstations.domain.Friend;
import cat.tecnocampus.fgcstations.domain.FriendId;
import cat.tecnocampus.fgcstations.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {
    List<Friend> findAllByUserUsername(String username);

    @Query("SELECT f.user.username, f.user.name, f.user.secondName, f.user.email, COUNT(f) as numberOfFriends " +
            "FROM Friend f " +
            "GROUP BY f.user.username " +
            "ORDER BY numberOfFriends DESC"
            )
    List<UserTopFriend> findTop3UsersWithMostFriends();


    /*
    @Query("SELECT f.user.username, f.id.friend " +
            "FROM Friend f " +
            "WHERE f.id.friend = :friendName")
    List<FriendUserDTO> findUserDTOByFriend(@Param("friendName") String friendName);
     */

    List<Friend> findAllByIdUsername(String username);
}
