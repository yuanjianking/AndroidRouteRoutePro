package jp.co.nri.route.presenter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.co.nri.route.base.BaseObserver;
import jp.co.nri.route.base.BasePresenter;
import jp.co.nri.route.bean.Event;
import jp.co.nri.route.bean.Location;
import jp.co.nri.route.bean.LocationRequest;
import jp.co.nri.route.bean.LocationResult;
import jp.co.nri.route.model.EventModel;
import jp.co.nri.route.view.IOwnerEventActiveView;
import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.PinOverlay;
import jp.co.yahoo.android.maps.PopupOverlay;

public class OwnerEventActivePresenter extends BasePresenter<IOwnerEventActiveView> {

    private EventModel eventModel;

    public OwnerEventActivePresenter(IOwnerEventActiveView view, EventModel eventModel) {
        this.view = view;
        this.eventModel = eventModel;
    }

    private void guestLocations(Event event){
        LocationRequest request = new LocationRequest();
        request.setEventid(event.get_id());
        eventModel.guestLocations(request).subscribe(new BaseObserver<LocationResult>(this) {
            @Override
            public void onNext(LocationResult result) {
                List<Location> list = result.getLocations();
                showMapForUser(list);
            }
        });
    }


    private void showMapForUser(List<Location> list){
        for(Location loc : list){
            GeoPoint gp = new GeoPoint((int)(Double.valueOf(loc.getLatitude()) * 1E6), (int)(Double.valueOf(loc.getLongitude()) * 1E6));
            PinOverlay po = new PinOverlay(PinOverlay.PIN_RED);
            view.getMapView().getOverlays().add(po);
            po.addPoint(gp,"目的地");
            PopupOverlay pop = new PopupOverlay();
            view.getMapView().getOverlays().add(pop);
            po.setOnFocusChangeListener(pop);
        }

        Collections.sort(list, new Comparator<Location>() {
            @Override
            public int compare(Location o1, Location o2) {
                return (int)Double.parseDouble(o1.getLatitude()) - (int)Double.parseDouble(o2.getLatitude());
            }
        });

        Collections.sort(list, new Comparator<Location>() {
            @Override
            public int compare(Location o1, Location o2) {
                return (int)Double.parseDouble(o1.getLongitude()) - (int)Double.parseDouble(o2.getLongitude());
            }
        });

        System.out.println("===");
    }


}
