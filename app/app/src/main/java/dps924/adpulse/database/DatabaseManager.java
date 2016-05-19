package dps924.adpulse.database;

import java.util.List;

import dps924.adpulse.models.AdLocation;
import dps924.adpulse.models.Creative;
import dps924.adpulse.models.LocationArea;
import dps924.adpulse.models.ManagerNote;
import dps924.adpulse.models.Note;
import dps924.adpulse.models.PostingTip;

public class DatabaseManager {
    private static DatabaseManager databaseManager;

    private DatabaseManager() {
       /*locations =  new ArrayList<AdLocation>(AdLocation.listAll(AdLocation.class));
        for(AdLocation location : locations) {
            List<LocationArea> locationAreas =
                    LocationArea.find(LocationArea.class, "ad_location = ?", Long.toString(location.getId()));
            Set<String> locationAreaGroups = new HashSet<String>();
            location.LocationAreas = locationAreas;

            for(LocationArea locationArea : locationAreas) {
                locationAreaGroups.add(locationArea.Area);
           }
            location.LocationAreaGroups = new ArrayList<String>(locationAreaGroups);
        }*/
    }


    public static DatabaseManager getInstance() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public static void saveCreatives(List<Creative> creatives) {
        for(Creative creative : creatives) {
            creative.IsFiller = true;
            creative.save();
        }
    }

    public static void saveLocations(List<AdLocation> locations) {
        for (AdLocation location : locations) {
            location.TotalCompleted = 0;
            location.TotalLocations = location.LocationAreas.size();
            location.save();

            for (PostingTip tip : location.PostingTips) {
                tip.AdLocation = location;
                tip.save();
            }
            for (ManagerNote note : location.ManagerNotes) {
                note.AdLocation = location;
                note.save();
            }
            for (Note note : location.Notes) {
                note.AdLocation = location;
                note.save();
            }
            for (LocationArea area : location.LocationAreas) {
                Creative current = area.CreativeUpdate.CurrentCreative;
                current.IsFiller = false;
                current.save();

                Creative expected = area.CreativeUpdate.ExpectedCreative;
                expected.IsFiller = false;
                expected.save();

                area.AdLocation = location;
                area.CurrentCreative = current;
                area.ExpectedCreative = expected;
                area.save();

            }
        }
    }

}
