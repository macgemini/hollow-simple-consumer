package models;

import com.netflix.hollow.core.write.objectmapper.HollowPrimaryKey;

@HollowPrimaryKey(fields="offerId")
public class AlreadyApplied {

    public int offerId;
    
    public AlreadyApplied() { }
    
    public AlreadyApplied(int offerId) {
        this.offerId = offerId;
    }

}
