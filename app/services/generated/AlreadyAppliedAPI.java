package services.generated;

import com.netflix.hollow.api.custom.HollowAPI;
import com.netflix.hollow.api.objects.provider.HollowFactory;
import com.netflix.hollow.api.objects.provider.HollowObjectCacheProvider;
import com.netflix.hollow.api.objects.provider.HollowObjectFactoryProvider;
import com.netflix.hollow.api.objects.provider.HollowObjectProvider;
import com.netflix.hollow.api.sampling.HollowObjectCreationSampler;
import com.netflix.hollow.api.sampling.HollowSamplingDirector;
import com.netflix.hollow.api.sampling.SampleResult;
import com.netflix.hollow.core.read.dataaccess.HollowDataAccess;
import com.netflix.hollow.core.read.dataaccess.HollowObjectTypeDataAccess;
import com.netflix.hollow.core.read.dataaccess.HollowTypeDataAccess;
import com.netflix.hollow.core.read.dataaccess.missing.HollowObjectMissingDataAccess;
import com.netflix.hollow.core.util.AllHollowRecordCollection;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public class AlreadyAppliedAPI extends HollowAPI {

    private final HollowObjectCreationSampler objectCreationSampler;

    private final AlreadyAppliedTypeAPI alreadyAppliedTypeAPI;

    private final HollowObjectProvider alreadyAppliedProvider;

    public AlreadyAppliedAPI(HollowDataAccess dataAccess) {
        this(dataAccess, Collections.<String>emptySet());
    }

    public AlreadyAppliedAPI(HollowDataAccess dataAccess, Set<String> cachedTypes) {
        this(dataAccess, cachedTypes, Collections.<String, HollowFactory<?>>emptyMap());
    }

    public AlreadyAppliedAPI(HollowDataAccess dataAccess, Set<String> cachedTypes, Map<String, HollowFactory<?>> factoryOverrides) {
        this(dataAccess, cachedTypes, factoryOverrides, null);
    }

    public AlreadyAppliedAPI(HollowDataAccess dataAccess, Set<String> cachedTypes, Map<String, HollowFactory<?>> factoryOverrides, AlreadyAppliedAPI previousCycleAPI) {
        super(dataAccess);
        HollowTypeDataAccess typeDataAccess;
        HollowFactory factory;

        objectCreationSampler = new HollowObjectCreationSampler("AlreadyApplied");

        typeDataAccess = dataAccess.getTypeDataAccess("AlreadyApplied");
        if(typeDataAccess != null) {
            alreadyAppliedTypeAPI = new AlreadyAppliedTypeAPI(this, (HollowObjectTypeDataAccess)typeDataAccess);
        } else {
            alreadyAppliedTypeAPI = new AlreadyAppliedTypeAPI(this, new HollowObjectMissingDataAccess(dataAccess, "AlreadyApplied"));
        }
        addTypeAPI(alreadyAppliedTypeAPI);
        factory = factoryOverrides.get("AlreadyApplied");
        if(factory == null)
            factory = new AlreadyAppliedHollowFactory();
        if(cachedTypes.contains("AlreadyApplied")) {
            HollowObjectCacheProvider previousCacheProvider = null;
            if(previousCycleAPI != null && (previousCycleAPI.alreadyAppliedProvider instanceof HollowObjectCacheProvider))
                previousCacheProvider = (HollowObjectCacheProvider) previousCycleAPI.alreadyAppliedProvider;
            alreadyAppliedProvider = new HollowObjectCacheProvider(typeDataAccess, alreadyAppliedTypeAPI, factory, previousCacheProvider);
        } else {
            alreadyAppliedProvider = new HollowObjectFactoryProvider(typeDataAccess, alreadyAppliedTypeAPI, factory);
        }

    }

    public void detachCaches() {
        if(alreadyAppliedProvider instanceof HollowObjectCacheProvider)
            ((HollowObjectCacheProvider)alreadyAppliedProvider).detach();
    }

    public AlreadyAppliedTypeAPI getAlreadyAppliedTypeAPI() {
        return alreadyAppliedTypeAPI;
    }
    public Collection<AlreadyApplied> getAllAlreadyApplied() {
        return new AllHollowRecordCollection<AlreadyApplied>(getDataAccess().getTypeDataAccess("AlreadyApplied").getTypeState()) {
            protected AlreadyApplied getForOrdinal(int ordinal) {
                return getAlreadyApplied(ordinal);
            }
        };
    }
    public AlreadyApplied getAlreadyApplied(int ordinal) {
        objectCreationSampler.recordCreation(0);
        return (AlreadyApplied)alreadyAppliedProvider.getHollowObject(ordinal);
    }
    public void setSamplingDirector(HollowSamplingDirector director) {
        super.setSamplingDirector(director);
        objectCreationSampler.setSamplingDirector(director);
    }

    public Collection<SampleResult> getObjectCreationSamplingResults() {
        return objectCreationSampler.getSampleResults();
    }

}
