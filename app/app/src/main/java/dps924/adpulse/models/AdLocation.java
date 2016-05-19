package dps924.adpulse.models;


import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

public class AdLocation extends SugarRecord<AdLocation> {

    public String Address;
    public String City;
    public String ContactName;
    public String FormattedAddress;
    public double Latitude;
    public String LocationName;
    public double Longitude;
    public Boolean PermanentClose;
    public String PhoneNumber;
    public String PostalCode;
    public Boolean ReopenDate;
    public Boolean TemporaryClose;

    @Ignore
    public List<LocationArea> LocationAreas;

    @Ignore
    public ManagerNote[] ManagerNotes;

    @Ignore
    public Note[] Notes;

    @Ignore
    public PostingTip[] PostingTips;

    public int TotalCompleted;
    public int TotalLocations;

    public AdLocation() {
        super();
    }

    public String formattedNumber() {
        String formatted = this.PhoneNumber.substring(0, 3) + " "
                + this.PhoneNumber.substring(3,6) + " "
                + this.PhoneNumber.substring(6,10);
        return formatted;
    }
}
