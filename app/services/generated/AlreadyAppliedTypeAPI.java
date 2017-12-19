package services.generated;

import com.netflix.hollow.api.custom.HollowObjectTypeAPI;
import com.netflix.hollow.core.read.dataaccess.HollowObjectTypeDataAccess;

@SuppressWarnings("all")
public class AlreadyAppliedTypeAPI extends HollowObjectTypeAPI {

    private final AlreadyAppliedDelegateLookupImpl delegateLookupImpl;

    AlreadyAppliedTypeAPI(AlreadyAppliedAPI api, HollowObjectTypeDataAccess typeDataAccess) {
        super(api, typeDataAccess, new String[] {
            "offerId"
        });
        this.delegateLookupImpl = new AlreadyAppliedDelegateLookupImpl(this);
    }

    public int getOfferId(int ordinal) {
        if(fieldIndex[0] == -1)
            return missingDataHandler().handleInt("AlreadyApplied", ordinal, "offerId");
        return getTypeDataAccess().readInt(ordinal, fieldIndex[0]);
    }

    public Integer getOfferIdBoxed(int ordinal) {
        int i;
        if(fieldIndex[0] == -1) {
            i = missingDataHandler().handleInt("AlreadyApplied", ordinal, "offerId");
        } else {
            boxedFieldAccessSampler.recordFieldAccess(fieldIndex[0]);
            i = getTypeDataAccess().readInt(ordinal, fieldIndex[0]);
        }
        if(i == Integer.MIN_VALUE)
            return null;
        return Integer.valueOf(i);
    }



    public AlreadyAppliedDelegateLookupImpl getDelegateLookupImpl() {
        return delegateLookupImpl;
    }

    @Override
    public AlreadyAppliedAPI getAPI() {
        return (AlreadyAppliedAPI) api;
    }

}