package services.generated;

import com.netflix.hollow.api.consumer.HollowConsumer;
import com.netflix.hollow.api.custom.HollowAPI;
import com.netflix.hollow.core.index.HollowHashIndex;
import com.netflix.hollow.core.index.HollowHashIndexResult;
import com.netflix.hollow.core.read.engine.HollowReadStateEngine;
import com.netflix.hollow.core.read.iterator.HollowOrdinalIterator;

import java.util.Collections;
import java.util.Iterator;

public class AlreadyAppliedAPIHashIndex implements HollowConsumer.RefreshListener {

    private HollowHashIndex idx;
    private AlreadyAppliedAPI api;
    private final String queryType;    private final String selectFieldPath;
    private final String matchFieldPaths[];

    public AlreadyAppliedAPIHashIndex(HollowConsumer consumer, String queryType, String selectFieldPath, String... matchFieldPaths) {
        this.queryType = queryType;        this.selectFieldPath = selectFieldPath;
        this.matchFieldPaths = matchFieldPaths;
        consumer.getRefreshLock().lock();
        try {
            this.api = (AlreadyAppliedAPI)consumer.getAPI();
            this.idx = new HollowHashIndex(consumer.getStateEngine(), queryType, selectFieldPath, matchFieldPaths);
            consumer.addRefreshListener(this);
        } catch(ClassCastException cce) {
            throw new ClassCastException("The HollowConsumer provided was not created with the AlreadyAppliedAPI generated API class.");
        } finally {
            consumer.getRefreshLock().unlock();
        }
    }

    public Iterable<AlreadyApplied> findAlreadyAppliedMatches(Object... keys) {
        HollowHashIndexResult matches = idx.findMatches(keys);
        if(matches == null)
            return Collections.emptySet();

        final HollowOrdinalIterator iter = matches.iterator();

        return new Iterable<AlreadyApplied>() {
            public Iterator<AlreadyApplied> iterator() {
                return new Iterator<AlreadyApplied>() {

                    private int next = iter.next();

                    public boolean hasNext() {
                        return next != HollowOrdinalIterator.NO_MORE_ORDINALS;
                    }

                    public AlreadyApplied next() {
                        AlreadyApplied obj = api.getAlreadyApplied(next);
                        next = iter.next();
                        return obj;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    @Override public void deltaUpdateOccurred(HollowAPI api, HollowReadStateEngine stateEngine, long version) throws Exception {
        reindex(stateEngine, api);
    }

    @Override public void snapshotUpdateOccurred(HollowAPI api, HollowReadStateEngine stateEngine, long version) throws Exception {
        reindex(stateEngine, api);
    }

    private void reindex(HollowReadStateEngine stateEngine, HollowAPI api) {
        this.idx = new HollowHashIndex(stateEngine, queryType, selectFieldPath, matchFieldPaths);
        this.api = (AlreadyAppliedAPI) api;
    }

    @Override public void refreshStarted(long currentVersion, long requestedVersion) { }
    @Override public void blobLoaded(HollowConsumer.Blob transition) { }
    @Override public void refreshSuccessful(long beforeVersion, long afterVersion, long requestedVersion) { }
    @Override public void refreshFailed(long beforeVersion, long afterVersion, long requestedVersion, Throwable failureCause) { }

}