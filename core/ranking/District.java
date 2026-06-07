package core.ranking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.data.Ranking;
import core.data.Team;
import util.Algorithms;

public class District extends Rankable {
    private final List<Event> events;

    private final ArrayList<Ranking> districtRankings = new ArrayList<>();

    /**
     * Constructs a new District object
     * @param events The list of events in this district
     */
    public District(List<Event> events){
        this.events = events;
        
        ArrayList<Integer> eventTeamIndices = new ArrayList<>(events.size());

        for(int i = 0; i < events.size(); i++) {
            eventTeamIndices.add(0);
        }
        
        int previousTeamNumber = -1; // Impossible value

        while (true) {
            int lowestTeamEventIndex = 0;
            int lowestTeamNumber = 25000; // Impossible value

            boolean allListsHaveBeenSearched = true;

            // Grab the lowest numbered team by taking the minimum out of all the lowest numbered team from each event
            // The event team numbers are sorted by number in ascending order
            for(int eventIndex = 0; eventIndex < eventTeamIndices.size(); eventIndex++) {
                Event event = events.get(eventIndex);
                int teamIndex = eventTeamIndices.get(eventIndex);

                if(teamIndex >= event.getTeams().size()) continue;

                allListsHaveBeenSearched = false;

                Team team = event.getTeams().get(teamIndex);
                int currentTeamNumber = team.getTeamNum();

                if(currentTeamNumber < lowestTeamNumber) {
                    lowestTeamNumber = currentTeamNumber;
                    lowestTeamEventIndex = eventIndex;
                }
            }

            if(allListsHaveBeenSearched) break; // End condition

            // Add the team if it isn't already there
            if(lowestTeamNumber != previousTeamNumber) {
                previousTeamNumber = lowestTeamNumber;
                Team lowestTeam = events.get(lowestTeamEventIndex).getTeams().get(eventTeamIndices.get(lowestTeamEventIndex));
                teams.add(lowestTeam);
            }

            eventTeamIndices.set(lowestTeamEventIndex, eventTeamIndices.get(lowestTeamEventIndex) + 1);
        }

        // District points
        HashMap<Team, Integer> totalDistrictPoints = new HashMap<>();

        for(Event event : events) {
            for(var entry : event.getDistrictPointsMap().entrySet()) {
                Team team = entry.getKey();
                totalDistrictPoints.put(team, totalDistrictPoints.getOrDefault(team, 0) + entry.getValue());
            }
        }

        for(var entry : totalDistrictPoints.entrySet()) {
            districtRankings.add(new Ranking(entry.getKey(), entry.getValue()));
        }

        Algorithms.mergeSort(districtRankings, (r1, r2) -> (int) r2.getPoints() - (int) r1.getPoints());
    }

    /** @return all the events in the district */
    public List<Event> getEvents() {
        return events;
    }
    
    /** @return The rankings of teams in the district in ascending order (1 -> n) */
    @Override
    public List<Ranking> getRankings() {
        return districtRankings;
    }
}
