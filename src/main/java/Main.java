
public class Main {
    public static void main(String[] args) {

        // Создаем автосалон, работающий с покупателями и поставщиком авто
        Autosalon autosalon = new Autosalon();
        // Потоки покупателей объединим в группу
        ThreadGroup customers = new ThreadGroup("Покупатели");
        new Thread(customers, autosalon::sellCar, "Покупатель-1").start();
        new Thread(customers, autosalon::sellCar, "Покупатель-2").start();
        new Thread(customers, autosalon::sellCar, "Покупатель-3").start();


        // Поток поставщика авто
        Thread salon = new Thread(autosalon::receiveCar, "Toyota");
        salon.start();

        /* Когда поставщик выполнит заказ от автосалона на кол-во машин, то ожидающим потокам будет передан interrupt
         т.к. в ближ. время автомобилей поставляться не будет => делать покупателям больше нечего */
        try {
            salon.join();
            customers.interrupt();
        } catch (InterruptedException ignored) {

        }

    }
}
