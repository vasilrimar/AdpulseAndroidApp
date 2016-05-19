package dps924.adpulse.models;

import com.orm.SugarRecord;

public class PostingTip extends SugarRecord<PostingTip> {
    public Boolean Deleted;
    public String Note;

    public AdLocation AdLocation;

    public PostingTip() { }

}
