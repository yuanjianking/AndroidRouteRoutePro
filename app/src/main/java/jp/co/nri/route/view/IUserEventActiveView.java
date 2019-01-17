package jp.co.nri.route.view;

import jp.co.yahoo.android.maps.MapView;

public interface IUserEventActiveView {

    MapView getMapView();

    void updateDistanceView(double distance);

    void updateRemainingTime(int hour, int minute);

    void updateSpeed(double speed);

    void setTitle(String title);

}
