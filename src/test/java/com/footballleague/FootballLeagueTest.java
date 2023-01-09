package com.footballleague;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.footballleague.FootballLeague.calculateLeagueStanding;
import static com.footballleague.FootballLeague.createLeagueTable;
import static com.footballleague.FootballLeague.main;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class FootballLeagueTest {

    @Test
    void main_shouldThrowException() {
        try {
            String[] inputFilesNames = {new File("dummy.txt").getName()};
            main(inputFilesNames);
        } catch (Exception e) {
            return;
        }
        fail("Should have thrown Exception");
    }

    @Test
    void calculateLeagueStanding_shouldThrowInvalidInputException1() {
        try {
            List<String> results = new ArrayList<>();
            results.add("United 2, Chelsea");
            calculateLeagueStanding(results);
        } catch (exceptions.InvalidInputException e) {
            return;
        }
        fail("Should have thrown InvalidInputException");
    }

    @Test
    void calculateLeagueStanding_shouldThrowInvalidInputException2() {
        try {
            List<String> results = new ArrayList<>();
            results.add("United 2, Chelsea d");
            calculateLeagueStanding(results);
        } catch (exceptions.InvalidInputException e) {
            return;
        }
        fail("Should have thrown InvalidInputException");
    }

    @Test
    void calculateLeagueStanding_shouldThrowInvalidInputException3() {
        try {
            List<String> results = new ArrayList<>();
            results.add("United 2");
            calculateLeagueStanding(results);
        } catch (exceptions.InvalidInputException e) {
            return;
        }
        fail("Should have thrown InvalidInputException");
    }

    @Test
    void calculateLeagueStanding_shouldHaveCorrectRanking1() throws exceptions.InvalidInputException {
        List<String> results = new ArrayList<>();
        results.add("United 2, Chelsea 1");
        HashMap<String, Integer> leagueTable = calculateLeagueStanding(results);

        assertEquals(leagueTable.get("United"), 3);
        assertEquals(leagueTable.get("Chelsea"), 0);
    }

    @Test
    void calculateLeagueStanding_shouldHaveCorrectRanking2() throws exceptions.InvalidInputException {
        List<String> results = new ArrayList<>();
        results.add("United 2, Chelsea 1");
        results.add("United 2, Chelsea 3");
        HashMap<String, Integer> leagueTable = calculateLeagueStanding(results);

        assertEquals(leagueTable.get("United"), 3);
        assertEquals(leagueTable.get("Chelsea"), 3);
    }

    @Test
    void calculateLeagueStanding_shouldHaveCorrectRanking3() throws exceptions.InvalidInputException {
        List<String> results = new ArrayList<>();
        results.add("United 2, Chelsea 2");
        HashMap<String, Integer> leagueTable = calculateLeagueStanding(results);

        assertEquals(leagueTable.get("United"), 1);
        assertEquals(leagueTable.get("Chelsea"), 1);
    }

    @Test
    void createLeagueTable_shouldHaveAddedCorrectTeams() throws exceptions.InvalidInputException {
        List<String> data = new ArrayList<>();
        data.add("United 2, Chelsea 2");
        data.add("Liverpool 2, City 2");
        data.add("Arsenal 2, Spurs 2");
        HashMap<String, Integer> leagueTable = createLeagueTable(data);

        assertEquals(6, leagueTable.size());
        assertTrue(leagueTable.containsKey("United"));
        assertTrue(leagueTable.containsKey("Chelsea"));
        assertTrue(leagueTable.containsKey("City"));
        assertTrue(leagueTable.containsKey("Liverpool"));
        assertTrue(leagueTable.containsKey("Arsenal"));
        assertTrue(leagueTable.containsKey("Spurs"));
    }
}
