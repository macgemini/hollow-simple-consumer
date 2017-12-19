package services.generated;

import com.netflix.hollow.api.objects.HollowObject;

@SuppressWarnings("all")
public class AlreadyApplied extends HollowObject {

    public AlreadyApplied(AlreadyAppliedDelegate delegate, int ordinal) {
        super(delegate, ordinal);
    }

    public int getOfferId() {
        return delegate().getOfferId(ordinal);
    }

    public Integer getOfferIdBoxed() {
        return delegate().getOfferIdBoxed(ordinal);
    }

    public AlreadyAppliedAPI api() {
        return typeApi().getAPI();
    }

    public AlreadyAppliedTypeAPI typeApi() {
        return delegate().getTypeAPI();
    }

    protected AlreadyAppliedDelegate delegate() {
        return (AlreadyAppliedDelegate)delegate;
    }

}