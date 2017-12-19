package services.generated;

import com.netflix.hollow.api.client.HollowAPIFactory;
import com.netflix.hollow.api.custom.HollowAPI;
import com.netflix.hollow.api.objects.provider.HollowFactory;
import com.netflix.hollow.core.read.dataaccess.HollowDataAccess;

import java.util.Collections;
import java.util.Set;

public class AlreadyAppliedAPIFactory implements HollowAPIFactory {

    private final Set<String> cachedTypes;

    public AlreadyAppliedAPIFactory() {
        this(Collections.<String>emptySet());
    }

    public AlreadyAppliedAPIFactory(Set<String> cachedTypes) {
        this.cachedTypes = cachedTypes;
    }

    @Override
    public HollowAPI createAPI(HollowDataAccess dataAccess) {
        return new AlreadyAppliedAPI(dataAccess, cachedTypes);
    }

    @Override
    public HollowAPI createAPI(HollowDataAccess dataAccess, HollowAPI previousCycleAPI) {
        return new AlreadyAppliedAPI(dataAccess, cachedTypes, Collections.<String, HollowFactory<?>>emptyMap(), (AlreadyAppliedAPI) previousCycleAPI);
    }

}