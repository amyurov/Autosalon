public class Seller {
    private static final int CAR_PRODUCTION_TIME = 5000; // Время изготовления 1-го авто производителем
    private static final int CHECKING_AVAILABLE_CAR = 1000; // Время проверки наличия авто продавцом
    private static final int SALES_PLAN = 10; // План продаж, либо производственная возможность производителя

    private Autosalon autosalon;
    // Продавец ведет учет проданных авто
    private int sellsCount;

    public Seller(Autosalon autosalon) {
        this.autosalon = autosalon;
    }

    // Объявляем метод sellCar() как synchronized, чтобы определить точку входа в критич. секцию
    public synchronized Car sellCar() {
        // Добавляем переменную Car, чтобы хранить в ней проданный авто
        Car car = null;
        // цикл, чтобы поток не завершался до продажи всех авто
        while (sellsCount < SALES_PLAN) {
            try {
                System.out.printf("%s зашел в автосалон%n", Thread.currentThread().getName());
                // Проверка наличия авто
                while (autosalon.getCars().size() == 0 ) {
                    Thread.sleep(CHECKING_AVAILABLE_CAR);
                    System.out.println("Машин нет, ожидайте " + Thread.currentThread().getName());
                    // Покупатель ждет поступления авто
                    wait();
                }
                // Если авто есть в наличии, и план продаж еще не выполнен, то покупатель (поток) забирает 1 авто
                if (sellsCount < SALES_PLAN) {
                    System.out.printf("%s забрал новую машину%n", Thread.currentThread().getName());
                    car = autosalon.getCars().remove(0);
                    sellsCount++;
                    // Продавец ведет учет количества проданых авто
                    System.out.printf("Продано автомобилей: %d%n", sellsCount);
                } else break; // Если план продаж выполнен, значит машин больше не будет и покупатель может заниматься другими делами
                // Если же план еще не выполнен, значит покупатель может купить еще авто, для этот он заходит обратно в главный цикл

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        System.out.printf("%s покинул автосалон %n", Thread.currentThread().getName());
        return car;
    }

    // Метод приемки авто
    public void receiveCar() {
        try {
            // Пока план продаж не выполнен, производитель производит по 1 авто, затрачивая при этом CAR_PRODUCTION_TIME
            while (sellsCount < SALES_PLAN) {
                Thread.sleep(CAR_PRODUCTION_TIME);
                // Как только авто готово к доставке в автосалон, производителю нужен продавец, поэтому критич. секция
                // начинается тут
                synchronized (this) {
                    // Поставщик узнает нужны ли еще автомобили этому автосалону
                    if (sellsCount == SALES_PLAN) break; // Нет - производство авто прекращается, метод receiveCar() всё обработал
                    // Да - производитель отдает продавцу один авто.
                    System.out.printf("Производитель %s выпустил 1 авто%n", Thread.currentThread().getName());
                    autosalon.getCars().add(new Car());
                    // Продавец оповещает рандомного ожидающего покупателя о поступлении авто
                    notify();
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("План продаж выполнен");
    }
}
