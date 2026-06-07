package ranking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import data.Ranking;
import data.Team;
import tba.Conversions;
import util.Algorithms;
import util.ComparatorFactory;

public class District extends Rankable {
    private final List<Event> events;

    private final ArrayList<Ranking> districtRankings = new ArrayList<>();

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

            // Grab the lowest numbered team by grabbing the lowest numbered team from each event
            // The event team numbers are sorted by number in ascending order, so 
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

            if(allListsHaveBeenSearched) break;

            if(lowestTeamNumber != previousTeamNumber) {
                previousTeamNumber = lowestTeamNumber;
                Team lowestTeam = events.get(lowestTeamEventIndex).getTeams().get(eventTeamIndices.get(lowestTeamEventIndex));
                teams.add(lowestTeam);
            }

            eventTeamIndices.set(lowestTeamEventIndex, eventTeamIndices.get(lowestTeamEventIndex) + 1);
        }

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

        Algorithms.mergeSort(districtRankings, (r1, r2) -> (int) r1.getPoints() - (int) r2.getPoints());
    }

    public List<Event> getEvents() {
        return events;
    }
    
    @Override
    public List<Ranking> getRankings() {
        return districtRankings;
    }
}
