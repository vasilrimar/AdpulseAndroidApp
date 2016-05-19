package dps924.adpulse.models;

import com.orm.SugarRecord;

public class Creative extends SugarRecord<Creative>{

    public int CampaignID;
    public String CampaignName;
    public String Name;
    public Boolean IsFiller;

    public Creative() {}
}
