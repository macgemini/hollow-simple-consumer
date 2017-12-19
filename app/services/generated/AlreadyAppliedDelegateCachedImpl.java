package services.generated;

import com.netflix.hollow.api.custom.HollowTypeAPI;
import com.netflix.hollow.api.objects.delegate.HollowCachedDelegate;
import com.netflix.hollow.api.objects.delegate.HollowObjectAbstractDelegate;
import com.netflix.hollow.core.read.dataaccess.HollowObjectTypeDataAccess;
import com.netflix.hollow.core.schema.HollowObjectSchema;

@SuppressWarnings("all")
public class AlreadyAppliedDelegateCachedImpl extends HollowObjectAbstractDelegate implements HollowCachedDelegate, AlreadyAppliedDelegate {

    private final Integer offerId;
   private AlreadyAppliedTypeAPI typeAPI;

    public AlreadyAppliedDelegateCachedImpl(AlreadyAppliedTypeAPI typeAPI, int ordinal) {
        this.offerId = typeAPI.getOfferIdBoxed(ordinal);
        this.typeAPI = typeAPI;
    }

    public int getOfferId(int ordinal) {
        return offerId.intValue();
    }

    public Integer getOfferIdBoxed(int ordinal) {
        return offerId;
    }

    @Override
    public HollowObjectSchema getSchema() {
        return typeAPI.getTypeDataAccess().getSchema();
    }

    @Override
    public HollowObjectTypeDataAccess getTypeDataAccess() {
        return typeAPI.getTypeDataAccess();
    }

    public AlreadyAppliedTypeAPI getTypeAPI() {
        return typeAPI;
    }

    public void updateTypeAPI(HollowTypeAPI typeAPI) {
        this.typeAPI = (AlreadyAppliedTypeAPI) typeAPI;
    }

}