package ranking;

import java.util.ArrayList;
import java.util.List;
import data.Ranking;
import data.Team;

public class District extends Rankable {
    private List<Event> events;

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
    }
    
    @Override
    public List<Ranking> getRankings() {
        // TODO loop through all events and sim event district points then sum and rank
        throw new UnsupportedOperationException("Unimplemented method 'getRankings'");
    }
}
