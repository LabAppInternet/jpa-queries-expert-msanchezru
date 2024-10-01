package cat.tecnocampus.fgcstations.persistence;

import cat.tecnocampus.fgcstations.application.DTOs.UserDTOInterface;
import cat.tecnocampus.fgcstations.application.DTOs.UserDTOnoFJ;
import cat.tecnocampus.fgcstations.application.DTOs.UserTopFavoriteJourney;
import cat.tecnocampus.fgcstations.domain.JourneyId;
import cat.tecnocampus.fgcstations.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional <User> findByUsername(String username);

    Optional <UserDTOnoFJ> findProjectedByUsername(String username);

    Optional <UserDTOInterface> findInterfaceByUsername(String username);

    @Query("SELECT u.username, u.name, u.secondName, u.email, COUNT(u) as numberOfFavoriteJourneys "+
            "FROM User u " +
            "GROUP BY u.username " +
            "ORDER BY numberOfFavoriteJourneys DESC")
    List<UserTopFavoriteJourney> findFirst3ByOrderByFavoriteJourneysDesc();

    @Query("SELECT u.username, u.name, u.secondName, u.email "+
            "FROM User u " +
            "WHERE u.name = :name AND u.secondName = :secondName")
    List<UserDTOInterface> findByNameAndBySecondName(@Param("name") String name, @Param("secondName") String secondName);

}
