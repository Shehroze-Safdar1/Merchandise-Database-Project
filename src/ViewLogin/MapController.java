package ViewLogin;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.directions.*;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class MapController implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

    @FXML
    private GoogleMapView mapView;
    private GoogleMap map;
    private DirectionsRenderer renderer;
    protected GoogleMapView mapComponent;
    private GeocodingService geocodingService;
    private StringProperty address = new SimpleStringProperty();
    protected DirectionsService directionsService;
    protected DirectionsPane directionsPane;
    protected StringProperty from = new SimpleStringProperty();
    protected StringProperty to = new SimpleStringProperty();
    protected DirectionsRenderer directionsRenderer = null;
    private String username;
    @FXML
    protected TextField fromTextField;
    @FXML
    protected TextField toTextField;

    public MapController() {
    }

    public void initialize(URL url, ResourceBundle rb) {
        this.mapView.addMapInializedListener(this);
        this.address.bind(this.fromTextField.textProperty());
        this.from.bindBidirectional(this.fromTextField.textProperty());
        this.to.bindBidirectional(this.toTextField.textProperty());
    }

    @FXML
    private void toTextFieldAction(ActionEvent event) {
        System.out.println(this.toTextField.getText());
        DirectionsRequest request = new DirectionsRequest(
                (String) this.from.get(),
                (String) this.to.get(),
                TravelModes.DRIVING
        );
        this.directionsService = new DirectionsService();
        this.directionsRenderer = new DirectionsRenderer(true, this.map, this.directionsPane);
        this.directionsService.getRoute(request, this, this.directionsRenderer);
    }

    @FXML
    private void clearDirections(ActionEvent event) {
        this.directionsRenderer.clearDirections();
    }

    @FXML
    private void closeButtonAction(ActionEvent event) throws IOException {
        FXMLLoader bookStoreLoader = new FXMLLoader(this.getClass().getResource("MerchandiseStore.fxml"));
        Parent bookStore = (Parent) bookStoreLoader.load();
        Scene registrationScene = new Scene(bookStore);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        MerchandiseController newUsername = (MerchandiseController) bookStoreLoader.getController();
        newUsername.sendData(this.username);
        window.setScene(registrationScene);
        window.show();
    }

    public void mapInitialized() {
        this.geocodingService = new GeocodingService();
        MapOptions mapOptions = new MapOptions();
        mapOptions.center(new LatLong(33.7525192, -84.3928201))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .zoom(12.0);
        this.map = this.mapView.createMap(mapOptions);
        this.directionsService = new DirectionsService();
        this.directionsPane = this.mapView.getDirec();
    }

    @FXML
    public void addressTextFieldAction(ActionEvent event) {
        this.geocodingService.geocode((String) this.address.get(), (results, status) -> {
            LatLong latLongTo = null;
            LatLong latLongFrom = new LatLong(33.7525192, -84.3928201);
            Alert alert;
            if (status == GeocoderStatus.ZERO_RESULTS) {
                alert = new Alert(AlertType.ERROR, "No matching address found", new ButtonType[0]);
                alert.show();
            } else {
                if (results.length > 1) {
                    alert = new Alert(AlertType.WARNING, "Multiple results found, showing the first one.", new ButtonType[0]);
                    alert.show();
                    latLongTo = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
                } else {
                    latLongTo = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
                }

                MarkerOptions markerOptions = new MarkerOptions();
                DecimalFormat decimalFormat = new DecimalFormat("#.0");
                double distance = latLongFrom.distanceFrom(latLongTo);
                String d = decimalFormat.format(distance / 1609.34);
                markerOptions.position(latLongTo);
                Marker customer = new Marker(markerOptions);
                this.map.addMarker(customer);
                this.map.setCenter(latLongTo);
                InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
                infoWindowOptions.content("<h3>You are " + d + " miles from the bookstore </h3>");
                InfoWindow customerWindow = new InfoWindow(infoWindowOptions);
                customerWindow.open(this.map, customer);
            }
        });
    }

    public void sendData(String username) {
        this.username = username;
    }

    public void directionsReceived(DirectionsResult results, DirectionStatus status) {
        if (status.equals(DirectionStatus.OK)) {
            this.mapComponent.getMap().showDirectionsPane();
            System.out.println("OK");
            DirectionsResult e = results;
            System.out.println("SIZE ROUTES: " + results.getRoutes().size() + "\nORIGIN: " + ((DirectionsLeg) ((DirectionsRoute) results.getRoutes().get(0)).getLegs().get(0)).getStartLocation());
            System.out.println("LEGS SIZE: " + ((DirectionsRoute) results.getRoutes().get(0)).getLegs().size());
            System.out.println("WAYPOINTS " + results.getGeocodedWaypoints().size());

            try {
                System.out.println("Distancia total = " + ((DirectionsLeg) ((DirectionsRoute) e.getRoutes().get(0)).getLegs().get(0)).getDistance().getText());
            } catch (Exception var5) {
                System.out.println("ERROR: " + var5.getMessage());
            }

            System.out.println("LEG(0)");
            System.out.println(((DirectionsLeg) ((DirectionsRoute) results.getRoutes().get(0)).getLegs().get(0)).getSteps().size());
            System.out.println(this.renderer.toString());
        }
    }
}
