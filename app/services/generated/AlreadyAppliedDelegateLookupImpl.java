package services.generated;

import com.netflix.hollow.api.objects.delegate.HollowObjectAbstractDelegate;
import com.netflix.hollow.core.read.dataaccess.HollowObjectTypeDataAccess;
import com.netflix.hollow.core.schema.HollowObjectSchema;

@SuppressWarnings("all")
public class AlreadyAppliedDelegateLookupImpl extends HollowObjectAbstractDelegate implements AlreadyAppliedDelegate {

    private final AlreadyAppliedTypeAPI typeAPI;

    public AlreadyAppliedDelegateLookupImpl(AlreadyAppliedTypeAPI typeAPI) {
        this.typeAPI = typeAPI;
    }

    public int getOfferId(int ordinal) {
        return typeAPI.getOfferId(ordinal);
    }

    public Integer getOfferIdBoxed(int ordinal) {
        return typeAPI.getOfferIdBoxed(ordinal);
    }

    public AlreadyAppliedTypeAPI getTypeAPI() {
        return typeAPI;
    }

    @Override
    public HollowObjectSchema getSchema() {
        return typeAPI.getTypeDataAccess().getSchema();
    }

    @Override
    public HollowObjectTypeDataAccess getTypeDataAccess() {
        return typeAPI.getTypeDataAccess();
    }

}