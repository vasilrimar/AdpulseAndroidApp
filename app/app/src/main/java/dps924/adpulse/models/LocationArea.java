package dps924.adpulse.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class LocationArea extends SugarRecord<LocationArea> {
    public String Area;
    public String Board;
    public Boolean IsComplete;

    public AdLocation AdLocation;

    @Ignore
    public CreativeUpdate CreativeUpdate;

    public Creative CurrentCreative;
    public Creative ExpectedCreative;

    public LocationArea() {
        super();
        IsComplete = false;
    }
}
