package services.generated;

import com.netflix.hollow.api.objects.delegate.HollowObjectDelegate;


@SuppressWarnings("all")
public interface AlreadyAppliedDelegate extends HollowObjectDelegate {

    public int getOfferId(int ordinal);

    public Integer getOfferIdBoxed(int ordinal);

    public AlreadyAppliedTypeAPI getTypeAPI();

}