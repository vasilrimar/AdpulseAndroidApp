package dps924.adpulse.models;

import com.orm.SugarRecord;

public class Note extends SugarRecord<Note> {
    public Boolean Deleted;
    public String Note;

    public AdLocation AdLocation;

    public Note() {}
}
