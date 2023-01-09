package com.footballleague;

import java.util.*;
import java.io.*;

import exceptions.InvalidArgumentsCountException;
import exceptions.FailedToReadDataException;
import exceptions.InvalidInputException;

public class FootballLeague {

    public static void main(String[] args) throws InvalidArgumentsCountException, FailedToReadDataException, InvalidInputException {
        HashMap<String, Integer> leagueTable;
        LinkedHashSet<String> inputFilesNames = new LinkedHashSet<>();

        try {
            for (String arg : args) {
                inputFilesNames.add(new File(arg).getName());
            }
        } catch (Exception e) {
            throw new InvalidArgumentsCountException("No input file found in arguments");
        }

        List<String> data = readData(inputFilesNames);

        leagueTable = calculateLeagueStanding(data);

        int leagueRanking = 1;
        int currentPoints = -1;
        int teamCount = 1;

        System.out.println();

        for (String team: leagueTable.keySet()) {
            String key = team;
            Integer value = leagueTable.get(team);

            if (currentPoints != value) {
                leagueRanking = teamCount;
            }

            currentPoints = value;
            teamCount++;

            System.out.println(leagueRanking + ". " + key + ", " + value + " pts");
        }

        System.out.println();
    }

    public static HashMap<String, Integer> calculateLeagueStanding(List<String> results) throws InvalidInputException {
        HashMap<String, Integer> leagueTable;
        String homeTeamScore = "", awayTeamScore = "";

        leagueTable = createLeagueTable(results);

        for (String result : results) {
            if (result.split(",").length != 2) {
                throw new InvalidInputException("Invalid input for match result");
            }

            homeTeamScore = result.split(",")[0].trim();
            awayTeamScore = result.split(",")[1].trim();

            String homeTeamName = extractTeamName(homeTeamScore);
            String[] homeTeamScoreSplit = homeTeamScore.split(" ");
            Integer homeTeamGoals = null;

            try {
                homeTeamGoals = Integer.parseInt(homeTeamScoreSplit[homeTeamScoreSplit.length - 1]);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Invalid input for match score: " + homeTeamScoreSplit[homeTeamScoreSplit.length - 1]);
            }

            String awayTeamName = extractTeamName(awayTeamScore);
            String[] awayTeamScoreSplit = awayTeamScore.split(" ");
            Integer awayTeamGoals = null;

            try {
                awayTeamGoals = Integer.parseInt(awayTeamScoreSplit[awayTeamScoreSplit.length - 1]);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("Invalid input for match score: " + awayTeamScoreSplit[awayTeamScoreSplit.length - 1]);
            }

            leagueTable = determineWinner(leagueTable, homeTeamName, homeTeamGoals, awayTeamName, awayTeamGoals);
        }

        leagueTable = sortByKey(leagueTable);
        leagueTable = sortByValue(leagueTable);

        return leagueTable;
    }

    public static HashMap<String, Integer> sortByKey(HashMap<String, Integer> leagueTable) {
        HashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<String> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : leagueTable.entrySet()) {
            list.add(entry.getKey());
        }

        list.sort(Comparator.naturalOrder());

        for (String e : list) {
            for (Map.Entry<String, Integer> entry : leagueTable.entrySet()) {
                if (entry.getKey().equals(e)) {
                    sortedMap.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return sortedMap;
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> leagueTable) {
        HashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : leagueTable.entrySet()) {
            list.add(entry.getValue());
        }

        list.sort(Comparator.reverseOrder());

        for (Integer e : list) {
            for (Map.Entry<String, Integer> entry : leagueTable.entrySet()) {
                if (entry.getValue().equals(e)) {
                    sortedMap.put(entry.getKey(), e);
                }
            }
        }

        return sortedMap;
    }

    public static HashMap<String, Integer> determineWinner(HashMap<String, Integer> leagueTable, String homeTeamName, Integer homeTeamGoals, String awayTeamName, Integer awayTeamGoals) {
        if (homeTeamGoals > awayTeamGoals) { // home team won
            leagueTable.put(homeTeamName, leagueTable.get(homeTeamName) + 3);
        } else if (homeTeamGoals < awayTeamGoals) { // away team won
            leagueTable.put(awayTeamName, leagueTable.get(awayTeamName) + 3);
        } else { // draw
            leagueTable.put(homeTeamName, leagueTable.get(homeTeamName) + 1);
            leagueTable.put(awayTeamName, leagueTable.get(awayTeamName) + 1);
        }

        return leagueTable;
    }

    public static HashMap<String, Integer> createLeagueTable(List<String> results) throws InvalidInputException {
        HashMap<String, Integer> leagueTable = new LinkedHashMap<>();
        String homeTeamScore = "", awayTeamScore = "";

        for (String result : results) {
            if (result.split(",").length != 2) {
                throw new InvalidInputException("Invalid input for match scores");
            }

            homeTeamScore = result.split(",")[0].trim();
            awayTeamScore = result.split(",")[1].trim();

            leagueTable = addTeamIfNotExists(leagueTable, homeTeamScore);
            leagueTable = addTeamIfNotExists(leagueTable, awayTeamScore);
        }

        return leagueTable;
    }

    public static HashMap<String, Integer> addTeamIfNotExists(HashMap<String, Integer> leagueTable, String teamScore) {
        if (!leagueTable.containsKey(teamScore)) {
            leagueTable.put(extractTeamName(teamScore), 0);
        }

        return leagueTable;
    }

    public static String extractTeamName(String teamScore) {
        String[] teamScoreSplit = teamScore.split(" ");
        StringBuilder teamName = new StringBuilder();

        for (int i = 0; i < teamScoreSplit.length - 1; i++) {
            teamName.append(teamScoreSplit[i]).append(" ");
        }

        return teamName.toString().trim();
    }

    public static List<String> readData(LinkedHashSet<String> fileNames) throws FailedToReadDataException {
        List<String> results = new ArrayList<>();

        for (String fileName : fileNames) {
            try {
                File sample_data = new File(fileName);
                Scanner myReader = new Scanner(sample_data);

                while (myReader.hasNextLine()) {
                    String nextLine = myReader.nextLine();

                    if (!Objects.equals(nextLine, "") && nextLine != null) {
                        results.add(nextLine);
                    }
                }

                myReader.close();

            } catch (FileNotFoundException e) {
                throw new FailedToReadDataException("An error occurred reading file: " + fileName);
            }
        }

        return results;
    }
}

