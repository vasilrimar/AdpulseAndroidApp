package dps924.adpulse.models;

import com.orm.SugarRecord;

public class ManagerNote extends SugarRecord<ManagerNote> {
    public Boolean Deleted;
    public Boolean isRead;
    public Boolean isUrgent;
    public String Note;

    public AdLocation AdLocation;

    public ManagerNote() {}
}
