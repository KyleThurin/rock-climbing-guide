package com.techelevator.dao;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;
import com.techelevator.exception.DaoException;
import org.junit.jupiter.api.BeforeEach;
import com.techelevator.model.Area;
import org.junit.jupiter.api.Test;
import java.util.List;



public class JdbcAreaDaoTest extends BaseDaoTest {
    //('Red River Gorge', 40.617471, -8.753421, 'Cras non velit nec nisi vulputate nonummy.')
    protected static final Area AREA_1 = new Area(4,"Test Place", 40.617471, -8.753421, "Some kind of info");
    protected static final Area AREA_2 = new Area(4,"Red River Gorge", 40.617471, -8.753421, "Cras non velit nec nisi vulputate nonummy.");
    private JdbcAreaDao dao;
    @BeforeEach
    public void setup() {
        dao = new JdbcAreaDao(dataSource);
    }
    @Test
    public void getAreas_returns_all_areas() {

        // Arrange: use expected values above

        // Act
        List<Area> actualAreas = dao.listOfAreas();

        // Assert
        //      size of list is 3
        //      there are states AA, BB, CC
        assertEquals(3, actualAreas.size());

        // TODO: No order is specified, so no assumptions should
        //  be made as to which state is at which index
        //assertAreasMatch(AREA_1, actualAreas.get(0));

        //Assert.fail();
    }
    /*private void assertAreasMatch(Area expected, Area actual) {
        assertEquals(expected.getAreaID(), actual.getStateAbbreviation());
        assertEquals(expected.getStateName(), actual.getStateName());
    }*/
}
