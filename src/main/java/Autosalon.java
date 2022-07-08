import java.util.ArrayList;
import java.util.List;

public class Autosalon {
    // При автосалоне должен быть продавец, реализубщий логику продажи и приемки авто
    Seller seller = new Seller(this);

    List<Car> cars = new ArrayList<>();
    public List<Car> getCars() {
        return cars;
    }

    // Делегируем продавцу приёмку и продажу авто
    public Car sellCar() {
        return seller.sellCar();
    }

    public void receiveCar() {
        seller.receiveCar();
    }
}
