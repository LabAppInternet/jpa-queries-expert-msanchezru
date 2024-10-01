package cat.tecnocampus.fgcstations.persistence;

import cat.tecnocampus.fgcstations.application.DTOs.PopularDayOfWeek;
import cat.tecnocampus.fgcstations.domain.FavoriteJourney;
import cat.tecnocampus.fgcstations.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteJourneyRepository extends JpaRepository<FavoriteJourney, String> {

    @Query("SELECT DAYOFWEEK(f.dayTimeStarts) as dayOfWeek, COUNT(f) as count " +
            "FROM FavoriteJourney f " +
            "GROUP BY dayOfWeek " +
            "ORDER BY count DESC")
    List<PopularDayOfWeek> findMostPopularDayOfWeek();

    //TODO optional: Try to implement the query to get the FavoriteJourneysDTO of a user with its list of DayTimeStartDTO.
    // Is it possible to do it with a single query?
}
