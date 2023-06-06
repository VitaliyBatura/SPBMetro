import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class RouteCalculatorTest extends TestCase {

    List<Station> route;
    StationIndex stationIndex = new StationIndex();
    RouteCalculator routeCalculator = new RouteCalculator(stationIndex);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        route = new ArrayList<>();
        Line line1 = new Line(1, "Зелёная");
        Line line2 = new Line(2, "Красная");
        Line line3 = new Line(3, "Третья");

        route.add(new Station("Октябрьская", line1));
        route.add(new Station("Площадь Ленина", line1));
        route.add(new Station("Красный проспект", line1));
        route.add(new Station("Сибирская", line2));
        route.add(new Station("Площадь Гарина-Михайловского", line2));
        route.add(new Station("Берёзовая роща", line2));
        route.add(new Station("Золотая Нива", line3));
        route.add(new Station("Проспект Дзержинского", line3));

        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);

        Station st11 = new Station("Октябрьская", line1);
        Station st12 = new Station("Площадь Ленина", line1);
        Station st13 = new Station("Красный проспект", line1);

        line1.addStation(st11);
        line1.addStation(st12);
        line1.addStation(st13);

        Station st21 = new Station("Сибирская", line2);
        Station st22 = new Station("Площадь Гарина-Михайловского", line2);
        Station st23 = new Station("Берёзовая роща", line2);

        line2.addStation(st21);
        line2.addStation(st22);
        line2.addStation(st23);

        Station st31 = new Station("Золотая Нива", line3);
        Station st32 = new Station("Проспект Дзержинского", line3);

        line3.addStation(st31);
        line3.addStation(st32);

        stationIndex.addStation(st11);
        stationIndex.addStation(st12);
        stationIndex.addStation(st13);
        stationIndex.addStation(st21);
        stationIndex.addStation(st22);
        stationIndex.addStation(st23);
        stationIndex.addStation(st31);
        stationIndex.addStation(st32);

        List<Station> connectionLines1to2 = new ArrayList<>();
        connectionLines1to2.add(st13);
        connectionLines1to2.add(st21);
        stationIndex.addConnection(connectionLines1to2);

        List<Station> connectionLines2to3 = new ArrayList<>();
        connectionLines2to3.add(st23);
        connectionLines2to3.add(st31);
        stationIndex.addConnection(connectionLines2to3);

    }

    public void testCalculateDuration() {
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 19.5;
        assertEquals(expected, actual);
    }

    public void testGetRouteOnTheLine() {
        List<Station> expected = new ArrayList<>();
        expected.add(new Station("Октябрьская", stationIndex.getLine(1)));
        expected.add(new Station("Площадь Ленина", stationIndex.getLine(1)));
        expected.add(new Station("Красный проспект", stationIndex.getLine(1)));
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Октябрьская"),
                stationIndex.getStation("Красный проспект"));
        assertEquals(expected, actual);
    }

    public void testGetRouteWithOneConnection() {
        List<Station> expected = new ArrayList<>();
        expected.add(new Station("Площадь Ленина", stationIndex.getLine(1)));
        expected.add(new Station("Красный проспект", stationIndex.getLine(1)));
        expected.add(new Station("Сибирская", stationIndex.getLine(2)));
        expected.add(new Station("Площадь Гарина-Михайловского", stationIndex.getLine(2)));
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Площадь Ленина"),
                stationIndex.getStation("Площадь Гарина-Михайловского"));
        assertEquals(expected, actual);
    }

    public void testGetRouteWithTwoConnections() {
        List<Station> expected = new ArrayList<>();
        expected.add(new Station("Площадь Ленина", stationIndex.getLine(1)));
        expected.add(new Station("Красный проспект", stationIndex.getLine(1)));
        expected.add(new Station("Сибирская", stationIndex.getLine(2)));
        expected.add(new Station("Площадь Гарина-Михайловского", stationIndex.getLine(2)));
        expected.add(new Station("Берёзовая роща", stationIndex.getLine(2)));
        expected.add(new Station("Золотая Нива", stationIndex.getLine(3)));
        expected.add(new Station("Проспект Дзержинского", stationIndex.getLine(3)));
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Площадь Ленина"),
                stationIndex.getStation("Проспект Дзержинского"));
        assertEquals(expected, actual);
    }

    public void testGetShortestRoute() {
        List<Station> expected = new ArrayList<>();
        expected.add(new Station("Площадь Гарина-Михайловского", stationIndex.getLine(2)));
        expected.add(new Station("Сибирская", stationIndex.getLine(2)));
        expected.add(new Station("Красный проспект", stationIndex.getLine(1)));
        expected.add(new Station("Площадь Ленина", stationIndex.getLine(1)));
        List<Station> actual = routeCalculator.getShortestRoute
                (stationIndex.getStation("Площадь Гарина-Михайловского"),
                        stationIndex.getStation("Площадь Ленина"));
        assertEquals(expected, actual);
    }

    public void testGetRouteViaConnectedLine() {
        List<Station> expected = new ArrayList<>();
        expected.add(new Station("Берёзовая роща", stationIndex.getLine(2)));
        expected.add(new Station("Площадь Гарина-Михайловского", stationIndex.getLine(2)));
        expected.add(new Station("Сибирская", stationIndex.getLine(2)));
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Берёзовая роща"),
                stationIndex.getStation("Сибирская"));
        assertEquals(expected, actual);
    }

    public void testIsConnected() {
        List<Station> expected = new ArrayList<>();
        expected.add(new Station("Золотая нива", stationIndex.getLine(3)));
        expected.add(new Station("Берёзовая роща", stationIndex.getLine(2)));
        expected.add(new Station("Площадь Гарина-Михайловского", stationIndex.getLine(2)));
        expected.add(new Station("Сибирская", stationIndex.getLine(2)));
        expected.add(new Station("Красный проспект", stationIndex.getLine(1)));
        List<Station> actual = routeCalculator.getShortestRoute(stationIndex.getStation("Золотая нива"),
                stationIndex.getStation("Красный проспект"));
        assertEquals(expected, actual);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
